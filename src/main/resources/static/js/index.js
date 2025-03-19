// Índice actual del artículo que se está mostrando
let indiceActual = 0;

// Almacena los artículos de noticias obtenidos del servidor
let articulos = [];

// Elementos del DOM relacionados con las ciudades y pistas
const selectCiudades = document.getElementById('ciudades'); // Select para elegir ciudades
const contenedorPistas = document.getElementById('contenedor-pistas'); // Contenedor donde se mostrarán las pistas
const btMasPistas = document.getElementById('mas-pistas'); // Botón para cargar más pistas

// Ruta del archivo JSON de ciudades
const urlCiudades = './json/ciudades.json';

// Elementos destacados (features) del frontend
const feat1 = document.getElementById('feat1');
const feat2 = document.getElementById('feat2');
const feat3 = document.getElementById('feat3');
const feat4 = document.getElementById('feat4');

// Variables para la lógica de ciudades y pistas
let ciudadesFiltradas = []; // Lista de ciudades filtradas según criterios
let indicePistas = 0; // Índice para las pistas mostradas
let ciudadSeleccionada = selectCiudades.value.toLowerCase();  // Ciudad seleccionada inicialmente en minúsculas

// Event listeners para los botones de navegación entre artículos
document.getElementById('siguiente').addEventListener('click', mostrarSiguienteArticulo); // Botón siguiente
document.getElementById('anterior').addEventListener('click', mostrarArticuloAnterior);   // Botón anterior

// ZONA DE NOTICIAS 

// Fetch para obtener noticias desde la API
fetch('/api/noticias', {
    headers: {
        'X-API-KEY': apiKey 
    }
})
    .then(response => response.json()) // Convierte la respuesta en JSON
    .then(data => {
        // Filtra los artículos que tienen imágenes válidas según las extensiones permitidas
        articulos = data.articles.filter(articulo => esUrlImagenValida(articulo.urlToImage));
        renderizarArticulos(); // Renderiza los artículos en el DOM
    })
    .catch(error => console.error('Error al obtener las noticias:', error)); // Manejo de errores en la solicitud

/**
 * Renderiza los artículos en la sección de noticias.
 */
function renderizarArticulos() {
    const contenedorNoticias = document.getElementById('contenedor-noticias'); // Contenedor para las noticias
    contenedorNoticias.innerHTML = ''; // Limpia el contenido previo

    // Muestra hasta 3 artículos al mismo tiempo
    for (let i = 0; i < 3; i++) {
        const indiceArticulo = (indiceActual + i) % articulos.length; // Calcula el índice del artículo
        const articulo = articulos[indiceArticulo]; // Obtiene el artículo correspondiente

        // Crea un div para cada artículo
        const articuloDiv = document.createElement('div');
        articuloDiv.classList.add('noticia-articulo'); // Clase base para los artículos

        if (i === 1) {
            articuloDiv.classList.add('activo'); // Marca el artículo central como activo
        }

        // Contenido HTML del artículo
        articuloDiv.innerHTML = `
          <h2>${articulo.title}</h2>
          <img src="${articulo.urlToImage}" alt="${articulo.title}">
          <p>${articulo.description}</p>
          <a href="${articulo.url}" target="_blank">Leer más</a>
        `;

        // Añade el artículo al contenedor de noticias
        contenedorNoticias.appendChild(articuloDiv);
    }
}

/**
 * Muestra el siguiente artículo al desplazarse hacia adelante.
 */
function mostrarSiguienteArticulo() {
    indiceActual = (indiceActual + 1) % articulos.length; // Incrementa el índice y lo reinicia si alcanza el final
    renderizarArticulos(); // Vuelve a renderizar los artículos
}

/**
 * Muestra el siguiente artículo al desplazarse hacia atrás.
 */
function mostrarArticuloAnterior() {
    indiceActual = (indiceActual - 1 + articulos.length) % articulos.length; // Decrementa el índice y maneja el inicio
    renderizarArticulos(); // Vuelve a renderizar los artículos
}

// ÁREA DE PISTAS

/**
 * Carga las ciudades desde un archivo JSON.
 * Filtra y ordena las ciudades, luego actualiza el selector con las ciudades disponibles.
 */
function cargarCiudades() {
    fetch(urlCiudades) // Realiza una solicitud al archivo JSON de ciudades
        .then(response => {
            if (!response.ok) { // Verifica si la respuesta es exitosa
                throw new Error('Error al cargar el archivo JSON');
            }
            return response.json(); // Convierte la respuesta en un objeto JSON
        })
        .then(data => {
            // Filtra las ciudades con más de 150,000 habitantes
            ciudadesFiltradas = data.filter(ciudad => ciudad.habitantes >= 150000);

            // Ordena las ciudades alfabéticamente por su nombre
            ciudadesFiltradas.sort((a, b) => a.poblacion.localeCompare(b.poblacion));

            // Itera sobre las ciudades filtradas y las agrega al selector
            ciudadesFiltradas.forEach(ciudad => {
                const opcion = document.createElement('option'); // Crea un elemento <option>
                opcion.value = ciudad.poblacion.toLowerCase(); // Asigna el valor en minúsculas
                opcion.textContent = `${ciudad.poblacion}`; // Define el texto visible

                // Selecciona Madrid como ciudad predeterminada
                if (ciudad.poblacion.toLowerCase() === "madrid") {
                    opcion.selected = true;
                }

                selectCiudades.appendChild(opcion); // Agrega la opción al selector
            });

            // Actualiza la ciudad seleccionada
            ciudadSeleccionada = selectCiudades.value.toLowerCase();
            // Busca las pistas de la ciudad seleccionada por defecto
            buscarPistas(ciudadSeleccionada);
        })
        .catch(error => {
            console.error('Error al cargar las ciudades:', error); // Manejo de errores
        });
}

// Evento para cargar más pistas al hacer clic en el botón
btMasPistas.addEventListener('click', function () {
    indicePistas += 6;  // Incrementa el índice para cargar más pistas
    buscarPistas(ciudadSeleccionada); // Busca las siguientes pistas
});

// Evento para cambiar la ciudad seleccionada
selectCiudades.addEventListener('change', () => {
    indicePistas = 0;  // Reinicia el índice al cambiar de ciudad
    ciudadSeleccionada = selectCiudades.value.toLowerCase(); // Actualiza la ciudad seleccionada
    contenedorPistas.innerHTML = '';  // Limpia las pistas anteriores del contenedor
    buscarPistas(ciudadSeleccionada); // Busca pistas para la nueva ciudad seleccionada
});

/**
 * Busca pistas en el backend según la ciudad seleccionada.
 * @param {string} ciudad - Nombre de la ciudad seleccionada.
 */
async function buscarPistas(ciudad) {
    const imagenPorDefecto = '../img/pista1.png'; // Imagen predeterminada en caso de error

    try {
        // Busca la ciudad en la lista filtrada
        const ciudadEncontrada = ciudadesFiltradas.find(item => item.poblacion.toLowerCase() === ciudad);
        if (!ciudadEncontrada) { // Verifica si la ciudad existe
            throw new Error('Ciudad no encontrada en el archivo JSON');
        }

        // Construye la URL del backend para obtener las pistas
        const urlBackend = `/api/pistas?latitud=${ciudadEncontrada.latitud}&longitud=${ciudadEncontrada.longitud}`;

        // Realiza la solicitud GET al backend
        const responsePistas = await fetch(urlBackend, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                // 'X-API-KEY': apiKey
            }
        });
        if (!response.ok) {
            const errorData = await response.json(); // Parsea el JSON del error
            throw new Error(`${errorData.error}: ${errorData.cause}`);
        }

        // if (!responsePistas.ok) { // Verifica si la respuesta es exitosa
        //     throw new Error('Error al hacer la solicitud al backend');
        // }
        const dataPistas = await responsePistas.json(); // Convierte la respuesta en JSON

        // Itera sobre las pistas a partir del índice actual y muestra hasta 6
        for (let index = indicePistas; index < Math.min(indicePistas + 6, dataPistas.length); index++) {
            const pistaElemento = document.createElement('div'); // Crea un contenedor para cada pista
            pistaElemento.classList.add('pista'); // Asigna la clase CSS correspondiente

            // Obtiene la imagen de la pista o utiliza la imagen por defecto
            let imagenSrc = (dataPistas[index].images && dataPistas[index].images.length > 0 && dataPistas[index].images[0])
                ? dataPistas[index].images[0]
                : imagenPorDefecto;

            // URL para buscar la pista en Google
            const googleSearchUrl = `https://www.google.com/search?q=${encodeURIComponent(dataPistas[index].tenant_name)}`;

            // Contenido HTML para la pista
            pistaElemento.innerHTML = `
                <h3>${dataPistas[index].tenant_name}</h3>
                <p>Dirección: ${dataPistas[index].address.street}</p>
                <a href="${googleSearchUrl}" target="_blank">
                    <img class="imagen-pista" src="${imagenSrc}" alt="${dataPistas[index].tenant_uid}" onerror="this.onerror=null;this.src='${imagenPorDefecto}';">
                </a>
            `;
            contenedorPistas.appendChild(pistaElemento); // Agrega la pista al contenedor
        }

        // Oculta el botón de "cargar más" si no hay más pistas disponibles
        if (indicePistas + 6 >= dataPistas.length) {
            btMasPistas.style.display = 'none';
        } else {
            btMasPistas.style.display = 'block';
        }

    } catch (error) {
        console.error('Error al hacer la solicitud:', error.message); // Manejo de errores
    }
}
// Carga las ciudades cuando se completa la carga del DOM
document.addEventListener('DOMContentLoaded', cargarCiudades);


// ZONA DE CARACTERÍSTICAS

// Se crea un nuevo IntersectionObserver para detectar cuándo los elementos son visibles en el viewport
const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
        // Verifica si el elemento está intersectando (visible en el viewport)
        if (entry.isIntersecting) {
            // Aplica una animación diferente según el elemento y añade un retraso escalonado
            if (entry.target === feat1) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-derecha"); // Añade clase para animación hacia la derecha
                }, 0); // Sin retraso
            } else if (entry.target === feat2) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-izquierda"); // Añade clase para animación hacia la izquierda
                }, 1000); // Retraso de 1 segundo
            } else if (entry.target === feat3) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-derecha"); // Añade clase para animación hacia la derecha
                }, 2000); // Retraso de 2 segundos
            } else if (entry.target === feat4) {
                setTimeout(() => {
                    entry.target.classList.add("animacion-desplazar-izquierda"); // Añade clase para animación hacia la izquierda
                }, 3000); // Retraso de 3 segundos
            }

            // Deja de observar el elemento una vez que la animación se ha iniciado
            observer.unobserve(entry.target);
        }
    });
});

// Observar cada uno de los elementos objetivo
observer.observe(feat1); // Observa el primer elemento
observer.observe(feat2); // Observa el segundo elemento
observer.observe(feat3); // Observa el tercer elemento
observer.observe(feat4); // Observa el cuarto elemento











