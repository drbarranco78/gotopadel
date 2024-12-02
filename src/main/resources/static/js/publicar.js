//let idUsuario, organizador, tipo_partido, vacantes, nivel, fecha_partido;
const selectCiudades = document.getElementById('ciudades');
const contenedorPistas = document.getElementById('contenedor-pistas');
const urlCiudades = './json/ciudades.json';
let ciudadSeleccionada;
let formulario = document.getElementById('form-publicar');
const selectPistas = document.getElementById('pista');
  
formulario.reset();

function cargarCiudades() {

    fetch(urlCiudades)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar el archivo JSON');
            }
            return response.json();
        })
        .then(data => {
            const ciudadesFiltradas = data.filter(ciudad => ciudad.habitantes >= 50000);

            ciudadesFiltradas.sort((a, b) => a.poblacion.localeCompare(b.poblacion));

            ciudadesFiltradas.forEach(ciudad => {
                const opcion = document.createElement('option');
                opcion.value = ciudad.poblacion.toLowerCase(); // Valor en minúsculas para la búsqueda
                opcion.textContent = `${ciudad.poblacion}`; // Texto visible en el selector
                if (ciudad.poblacion.toLowerCase() === "madrid") {
                    opcion.selected = true; // Seleccionar Madrid por defecto
                }
                selectCiudades.appendChild(opcion);
            });

            ciudadSeleccionada = selectCiudades.value; // Actualizar la ciudad seleccionada
            cargarPistas(ciudadSeleccionada); // Buscar pistas para la ciudad seleccionada por defecto
        })
        .catch(error => {
            console.error('Error al cargar las ciudades:', error);
        });
}
// Evento para cambiar la ciudad seleccionada (reutilizado)
selectCiudades.addEventListener('change', () => {
    ciudadSeleccionada = selectCiudades.value; // Actualizar la ciudad seleccionada
    selectPistas.innerHTML = ''; // Limpiar las pistas anteriores
    cargarPistas(ciudadSeleccionada); // Cargar las pistas para la nueva ciudad seleccionada
});

async function cargarPistas(ciudad) {
    selectPistas.innerHTML = '';
    //const urlBase = 'https://api.playtomic.io/v1/tenants';
    //const parametros = `?user_id=me&playtomic_status=ACTIVE&with_properties=ALLOWS_CASH_PAYMENT&sport_id=PADEL&radius=50000&size=40&page=0&coordinate=`;

    try {
        const responseCiudades = await fetch(urlCiudades);
        if (!responseCiudades.ok) {
            throw new Error('Error al cargar el archivo JSON');
        }
        const dataCiudades = await responseCiudades.json();

        const ciudadEncontrada = dataCiudades.find(item => item.poblacion.toLowerCase() === ciudad);
        if (!ciudadEncontrada) {
            throw new Error('Ciudad no encontrada en el archivo JSON');
        }
        //const coordenadas = `${ciudadEncontrada.latitud},${ciudadEncontrada.longitud}`;
        //const url = `${urlBase}${parametros}${coordenadas}`;
        const urlBackend = `/api/pistas?latitud=${ciudadEncontrada.latitud}&longitud=${ciudadEncontrada.longitud}`;

        const responsePistas = await fetch(urlBackend, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!responsePistas.ok) {
            throw new Error('Error al hacer la solicitud a Playtomic');
        }
        const dataPistas = await responsePistas.json();
        // Agregar las pistas al select
        dataPistas.forEach(pista => {
            const opcionPista = document.createElement('option');
            opcionPista.value = pista.tenant_name;
            opcionPista.textContent = pista.tenant_name; // Mostrar el nombre de la pista
            selectPistas.appendChild(opcionPista);
        });

    } catch (error) {
        console.error('Error al hacer la solicitud de pistas:', error);
    }
}
// Cargar ciudades al iniciar
document.addEventListener('DOMContentLoaded', cargarCiudades);

document.getElementById('form-publicar').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario

    fetch('/api/usuario/datosUsuario')
        .then(response => response.json())
        .then(usuario => {
            if (usuario) {
                let nombreUbicacion = document.getElementById('pista').value;
                let ciudadUbicacion = document.getElementById('ciudades').value;

                function capitalizarPrimeraLetra(cadena) {
                    return cadena.charAt(0).toUpperCase() + cadena.slice(1).toLowerCase();
                }

                let ciudadFormateada = capitalizarPrimeraLetra(ciudadUbicacion);

                // Verificar o crear la ubicación antes de enviar los datos del partido
                fetch('/api/ubicacion/check?nombre=' + encodeURIComponent(nombreUbicacion) + '&ciudad=' + encodeURIComponent(ciudadFormateada), {
                    method: 'POST'
                })
                    .then(response => response.json())
                    .then(ubicacion => {
                        if (!ubicacion) {
                            mostrarMensaje("Ha habido un error al crear la ubicación. Inténtelo de nuevo", ".mensaje-error");
                            return;

                        }
                        const partidoData = {
                            usuario: usuario,
                            tipoPartido: document.querySelector('input[name="modalidad"]:checked').value,
                            vacantes: document.getElementById('numVacantes').value,
                            nivel: document.getElementById('nivel').value,
                            fechaPartido: formatearFecha(document.getElementById('fechaPartido').value),
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
                            .then(res => res.json()) // Obtener el objeto del partido recién creado
                            .then(partidoCreado => {
                                mostrarMensaje("Partido publicado con éxito", ".mensaje-exito");
                                console.log(partidoCreado);
                                

                                // Limpiar y recargar la interfaz después de unos segundos
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
