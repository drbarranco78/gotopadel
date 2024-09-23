//const  claveApi = '0f69ce3efa49459ea3b53fb57d7ec630';
//const url = `https://newsapi.org/v2/everything?qInTitle=padel&language=es&apiKey=${claveApi}`;

const extensionesValidas = ['jpeg', 'jpg', 'png'];
const urlInterna = '/api/noticias';
let indiceActual = 0;
let articulos = [];

// Event listeners para los botones de navegación
document.getElementById('siguiente').addEventListener('click', mostrarSiguienteArticulo);
document.getElementById('anterior').addEventListener('click', mostrarArticuloAnterior);

fetch(urlInterna)
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






