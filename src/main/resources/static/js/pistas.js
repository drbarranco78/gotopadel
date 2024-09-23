const selectCiudades = document.getElementById('ciudades');
const contenedorPistas = document.getElementById('contenedor-pistas');
const botonMas = document.getElementById('mas');
const urlCiudades = './json/ciudades.json';
let ciudadesFiltradas=[];
let indicePistas = 0;
let ciudadSeleccionada = selectCiudades.value.toLowerCase();  // Ciudad seleccionada inicialmente

// Función para cargar las ciudades desde el archivo JSON
function cargarCiudades() {
    fetch(urlCiudades)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar el archivo JSON');
            }
            return response.json();
        })
        .then(data => {
            ciudadesFiltradas = data.filter(ciudad => ciudad.habitantes >= 150000);

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

            ciudadSeleccionada = selectCiudades.value.toLowerCase(); // Actualizar la ciudad seleccionada
            buscarPistas(ciudadSeleccionada); // Buscar pistas para la ciudad seleccionada por defecto
        })
        .catch(error => {
            console.error('Error al cargar las ciudades:', error);
        });
}

// Evento para cargar más pistas al hacer clic en el botón
botonMas.addEventListener('click', function () {
    indicePistas += 6;  // Incrementar índice para cargar más
    buscarPistas(ciudadSeleccionada);
});

// Evento para cambiar la ciudad seleccionada
selectCiudades.addEventListener('change', () => {
    indicePistas = 0;  // Reiniciar índice al cambiar de ciudad
    ciudadSeleccionada = selectCiudades.value.toLowerCase(); // Actualizar la ciudad seleccionada
    contenedorPistas.innerHTML = '';  // Limpiar pistas anteriores
    buscarPistas(ciudadSeleccionada); // Buscar pistas para la nueva ciudad seleccionada
});

async function buscarPistas(ciudad) {
    const imagenPorDefecto = '../img/pista1.png';

    try {
        //const responseCiudades = await fetch(urlCiudades);
        //if (!responseCiudades.ok) {
            //throw new Error('Error al cargar el archivo JSON');
        //}
        //const dataCiudades = await responseCiudades.json();

        const ciudadEncontrada = ciudadesFiltradas.find(item => item.poblacion.toLowerCase() === ciudad);
        if (!ciudadEncontrada) {
            throw new Error('Ciudad no encontrada en el archivo JSON');
        }

        const urlBackend = `/api/pistas?latitud=${ciudadEncontrada.latitud}&longitud=${ciudadEncontrada.longitud}`;

        const responsePistas = await fetch(urlBackend, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!responsePistas.ok) {
            throw new Error('Error al hacer la solicitud al backend');
        }
        const dataPistas = await responsePistas.json();

        for (let index = indicePistas; index < Math.min(indicePistas + 6, dataPistas.length); index++) {
            const pistaElemento = document.createElement('div');
            pistaElemento.classList.add('pista');

            let imagenSrc = (dataPistas[index].images && dataPistas[index].images.length > 0 && dataPistas[index].images[0])
                ? dataPistas[index].images[0]
                : imagenPorDefecto;

            const googleSearchUrl = `https://www.google.com/search?q=${encodeURIComponent(dataPistas[index].tenant_name)}`;

            pistaElemento.innerHTML = `
                <h3>${dataPistas[index].tenant_name}</h3>
                <p>Dirección: ${dataPistas[index].address.street}</p>
                <a href="${googleSearchUrl}" target="_blank">
                    <img class="imagen-pista" src="${imagenSrc}" alt="${dataPistas[index].tenant_uid}" onerror="this.onerror=null;this.src='${imagenPorDefecto}';">
                </a>
            `;
            contenedorPistas.appendChild(pistaElemento);
        }

        if (indicePistas + 6 >= dataPistas.length) {
            botonMas.style.display = 'none';
        } else {
            botonMas.style.display = 'block';
        }

    } catch (error) {
        console.error('Error al hacer la solicitud:', error);
    }
}


// Cargar ciudades al iniciar
document.addEventListener('DOMContentLoaded', cargarCiudades);
