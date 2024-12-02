
const extensionesValidas = ['jpeg', 'jpg', 'png'];
let indiceActual = 0;
let articulos = [];
const selectCiudades = document.getElementById('ciudades');
const contenedorPistas = document.getElementById('contenedor-pistas');
const btMasPistas = document.getElementById('mas-pistas');
const urlCiudades = './json/ciudades.json';
const feat1 = document.getElementById('feat1');
const feat2 = document.getElementById('feat2');
const feat3 = document.getElementById('feat3');
const feat4 = document.getElementById('feat4');
let ciudadesFiltradas = [];
let indicePistas = 0;
let ciudadSeleccionada = selectCiudades.value.toLowerCase();  // Ciudad seleccionada inicialmente

// Event listeners para los botones de navegación
document.getElementById('siguiente').addEventListener('click', mostrarSiguienteArticulo);
document.getElementById('anterior').addEventListener('click', mostrarArticuloAnterior);

fetch('/api/noticias')
    .then(response => response.json())
    .then(data => {
        articulos = data.articles.filter(articulo => esUrlImagenValida(articulo.urlToImage));
        renderizarArticulos();
    })
    .catch(error => console.error('Error al obtener las noticias:', error));

function esUrlImagenValida(url) {
    return extensionesValidas.some(ext => url.endsWith(ext));
}

function renderizarArticulos() {
    const contenedorNoticias = document.getElementById('contenedor-noticias');
    contenedorNoticias.innerHTML = '';

    for (let i = 0; i < 3; i++) {
        const indiceArticulo = (indiceActual + i) % articulos.length;
        const articulo = articulos[indiceArticulo];

        const articuloDiv = document.createElement('div');
        articuloDiv.classList.add('noticia-articulo');
        if (i === 1) {
            articuloDiv.classList.add('activo');
        }

        articuloDiv.innerHTML = `
      <h2>${articulo.title}</h2>
      <img src="${articulo.urlToImage}" alt="${articulo.title}">
      <p>${articulo.description}</p>
      <a href="${articulo.url}" target="_blank">Leer más</a>
    `;

        contenedorNoticias.appendChild(articuloDiv);
    }
}

function mostrarSiguienteArticulo() {
    indiceActual = (indiceActual + 1) % articulos.length;
    renderizarArticulos();
}

function mostrarArticuloAnterior() {
    indiceActual = (indiceActual - 1 + articulos.length) % articulos.length;
    renderizarArticulos();
}

// Area de pistas 

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
btMasPistas.addEventListener('click', function () {
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
            btMasPistas.style.display = 'none';
        } else {
            btMasPistas.style.display = 'block';
        }

    } catch (error) {
        console.error('Error al hacer la solicitud:', error);
    }
}
// Cargar ciudades al iniciar
document.addEventListener('DOMContentLoaded', cargarCiudades);

// Zona de características 

const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            // Aplicar la animación con un retraso escalonado
            if (entry.target === feat1) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-derecha");
                }, 0); // Sin retraso
            } else if (entry.target === feat2) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-izquierda");
                }, 1000); // Retraso de 1.5s
            } else if (entry.target === feat3) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-derecha");
                }, 2000); // Retraso de 3s
            } else if (entry.target === feat4) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-izquierda");
                }, 3000); // Retraso de 4.5s
            }
            observer.unobserve(entry.target); // Dejar de observar una vez que la animación haya empezado
        }
    });
});

// Observar todos los elementos
observer.observe(feat1);
observer.observe(feat2);
observer.observe(feat3);
observer.observe(feat4);










