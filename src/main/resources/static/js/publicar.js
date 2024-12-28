// URL del archivo JSON que contiene las ciudades
const urlCiudades = './json/ciudades.json';

// Selección de elementos del DOM
let selectCiudades = document.getElementById('ciudades'); // Selector de ciudades
let contenedorPistas = document.getElementById('contenedor-pistas'); // Contenedor de pistas
let ciudadSeleccionada; // Variable para almacenar la ciudad seleccionada
let formulario = document.getElementById('form-publicar'); // Formulario para publicar partidos
let selectPistas = document.getElementById('pista'); // Selector de pistas
let limpiarFormulario = document.getElementById('limpiar'); // Botón para limpiar el formulario
let fechaPartido = document.getElementById('fechaPartido'); // Campo de fecha del partido

// Establece la fecha mínima para el partido (hoy)
fechaPartido.min = new Date().toISOString().split("T")[0];

// Cargar las ciudades al cargar la página
document.addEventListener('DOMContentLoaded', cargarCiudades, formulario.reset());

// Evento para limpiar el formulario y recargar las ciudades
limpiarFormulario.addEventListener('click', function () {
    cargarCiudades();
});

// Selector de modalidad (Singles o Dobles) y número de vacantes
let modalidadRadios = document.querySelectorAll('input[name="modalidad"]');
let vacantes = document.getElementById('numVacantes');

/**
 * Función para actualizar el número máximo de vacantes según la modalidad
 * 
 */
actualizarMaxVacantes();
function actualizarMaxVacantes() {
    let tipoPartido = document.querySelector('input[name="modalidad"]:checked').value;
    if (tipoPartido === "Singles") {
        vacantes.max = 1;
        vacantes.value = 1;
    } else {
        vacantes.max = 3;
        vacantes.value = 3;
    }
}

// Escuchar cambios en las opciones de modalidad
modalidadRadios.forEach(radio => {
    radio.addEventListener('change', actualizarMaxVacantes);
});

/**
 * Función para cargar las ciudades desde el archivo JSON
 * 
 */
function cargarCiudades() {
    fetch(urlCiudades)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar el archivo JSON');
            }
            return response.json();
        })
        .then(data => {
            // Filtrar ciudades con más de 50,000 habitantes
            let ciudadesFiltradas = data.filter(ciudad => ciudad.habitantes >= 50000);
            ciudadesFiltradas.sort((a, b) => a.poblacion.localeCompare(b.poblacion));

            // Agregar opciones al selector
            ciudadesFiltradas.forEach(ciudad => {
                let opcion = document.createElement('option');
                opcion.value = ciudad.poblacion.toLowerCase(); // Valor en minúsculas para la búsqueda
                opcion.textContent = `${ciudad.poblacion}`; // Texto visible en el selector
                if (ciudad.poblacion.toLowerCase() === "madrid") {
                    opcion.selected = true; // Seleccionar Madrid por defecto
                }
                selectCiudades.appendChild(opcion);
            });

            ciudadSeleccionada = selectCiudades.value; // Actualizar la ciudad seleccionada
            cargarPistas(ciudadSeleccionada); // Cargar pistas para la ciudad seleccionada por defecto
        })
        .catch(error => {
            console.error('Error al cargar las ciudades:', error);
        });
}

// Evento para actualizar las pistas al cambiar de ciudad
selectCiudades.addEventListener('change', () => {
    ciudadSeleccionada = selectCiudades.value; // Actualizar la ciudad seleccionada
    selectPistas.innerHTML = ''; // Limpiar las pistas anteriores
    cargarPistas(ciudadSeleccionada); // Cargar las pistas para la nueva ciudad
});

/**
 * Función para cargar las pistas según la ciudad seleccionada 
 * @param {string} ciudad - Nombre de la ciudad seleccionada
 */
async function cargarPistas(ciudad) {
    selectPistas.innerHTML = '';
    try {
        let responseCiudades = await fetch(urlCiudades);
        if (!responseCiudades.ok) {
            throw new Error('Error al cargar el archivo JSON');
        }
        let dataCiudades = await responseCiudades.json();

        // Buscar la ciudad en el archivo JSON
        let ciudadEncontrada = dataCiudades.find(item => item.poblacion.toLowerCase() === ciudad);
        if (!ciudadEncontrada) {
            throw new Error('Ciudad no encontrada en el archivo JSON');
        }

        // Solicitar las pistas al backend
        let urlBackend = `/api/pistas?latitud=${ciudadEncontrada.latitud}&longitud=${ciudadEncontrada.longitud}`;
        let responsePistas = await fetch(urlBackend, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!responsePistas.ok) {
            throw new Error('Error al hacer la solicitud a Playtomic');
        }
        let dataPistas = await responsePistas.json();

        // Agregar las pistas al selector
        dataPistas.forEach(pista => {
            let opcionPista = document.createElement('option');
            opcionPista.value = pista.tenant_name;
            opcionPista.textContent = pista.tenant_name; // Nombre de la pista
            selectPistas.appendChild(opcionPista);
        });

    } catch (error) {
        console.error('Error al hacer la solicitud de pistas:', error);
    }
}

/**
 * Manejar el envío del formulario para publicar el partido
 * @event
 */
document.getElementById('form-publicar').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario

    // Obtener datos del usuario logueado
    fetch('/api/usuario/datosUsuario')
        .then(response => response.json())
        .then(usuario => {
            if (usuario) {
                let nombreUbicacion = document.getElementById('pista').value;
                let ciudadUbicacion = document.getElementById('ciudades').value;

                // Capitalizar la primera letra de la ciudad
                function capitalizarPrimeraLetra(cadena) {
                    return cadena.charAt(0).toUpperCase() + cadena.slice(1).toLowerCase();
                }
                let ciudadFormateada = capitalizarPrimeraLetra(ciudadUbicacion);

                // Verificar o crear la ubicación
                fetch(`/api/ubicacion/check?nombre=${encodeURIComponent(nombreUbicacion)}&ciudad=${encodeURIComponent(ciudadFormateada)}`, {
                    method: 'POST'
                })
                    .then(response => response.json())
                    .then(ubicacion => {
                        if (!ubicacion) {
                            mostrarMensaje("Ha habido un error al crear la ubicación. Inténtelo de nuevo", ".mensaje-error");
                            return;
                        }
                        // Preparar datos del partido
                        let partidoData = {
                            usuario: usuario,
                            tipoPartido: document.querySelector('input[name="modalidad"]:checked').value,
                            vacantes: document.getElementById('numVacantes').value,
                            nivel: document.getElementById('nivel').value,
                            fechaPartido: formatearFecha(fechaPartido.value),
                            fechaPublicacion: formatearFecha(new Date().toISOString().split('T')[0]),
                            horaPartido: document.getElementById('horaPartido').value,
                            ubicacion: ubicacion,
                            comentarios: document.getElementById('comentarios').value
                        };

                        // Publicar el partido
                        fetch('/api/partido', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(partidoData)
                        })
                            .then(res => res.json())
                            .then(partidoCreado => {
                                mostrarMensaje("Partido publicado con éxito", ".mensaje-exito");
                                console.log(partidoCreado);

                                // Limpiar el formulario y recargar la interfaz
                                setTimeout(() => {
                                    formulario.reset();
                                    cargarCiudades();
                                    cargarPistas("madrid");
                                    document.querySelector("#enlace-ver").click();
                                }, 2000);
                            })
                            .catch(error => console.error('Error al publicar el partido:', error));
                    })
                    .catch(error => console.error('Error al verificar/crear la ubicación:', error));
            }
        })
        .catch(error => console.error('Error al obtener los datos del usuario:', error));
});
