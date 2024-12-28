let verDetallesFicha, fichaPartidoContainer, fichaMiPartidoContainer, fichaPartidoTemplate,
    fichaMiPartidoTemplate,  btInscribeVer, btInscribe, contenedorPrincipal, idUsuarioActivo;
document.addEventListener("DOMContentLoaded", () => {
    // Inicialización de variables
    verDetallesFicha = document.getElementById('ficha-detalles');
    // misDetallesFicha = document.getElementById('ficha-detalles-mis');
    fichaPartidoContainer = document.getElementById('ver-partidos');
    fichaMiPartidoContainer = document.getElementById('mis-partidos');
    fichaPartidoTemplate = document.getElementById('ficha-partido');
    fichaMiPartidoTemplate = document.getElementById('ficha-mis-partidos');
    // btInscribeMis = document.getElementById('bt-inscribir-mis');
    btInscribeVer = document.getElementById('bt-inscribir');
    contenedorPrincipal = document.getElementById('contenedor-principal');

    if (!verDetallesFicha || !fichaMiPartidoTemplate  || !fichaPartidoContainer || !fichaPartidoTemplate) {
        console.error("No se encontraron los elementos requeridos en el DOM.");
    }
});