
function mostrarMensaje(mensaje, selector) {
    $(selector).html(`<h3>${mensaje}</h3>`).fadeIn(500).delay(2500).fadeOut(500);

}

function formatearFecha(fecha) {
    let [anioNac, mesNac, diaNac] = fecha.split('-');
    fecha = `${diaNac}/${mesNac}/${anioNac}`;
    return fecha;
}

function limpiarContenedores() {
    
    if (fichaPartidoContainer) fichaPartidoContainer.innerHTML = '';
    fichaPartidoContainer.classList.remove('lista-vacia');

    if (fichaMiPartidoContainer) fichaMiPartidoContainer.innerHTML = '';
    fichaMiPartidoContainer.classList.remove('lista-vacia');
    fichaMiPartidoContainer.classList.remove('mis-partidos');
}

function mostrarListaVacia(container,esAdmin=false) {
    if (!esAdmin) {
        limpiarContenedores(container);
        
    }
    let contenedor = container[0] || container;    
    contenedor.classList.remove('ver-partidos');
    //container.className = '';  // Esto elimina todas las clases previas del contenedor (ERA LO QUE CAUSABA EL PROBLEMA)
    contenedor.classList.add('lista-vacia');  // Agrega la clase específica para lista vacía

    // Crea la imagen y el mensaje para la lista vacía
    let imgListaVacia = document.createElement('img');
    imgListaVacia.src = 'img/ico_lista_vacia.png';
    imgListaVacia.alt = 'No hay partidos disponibles';
    imgListaVacia.style.margin = '50px auto';


    let textoListaVacia = document.createElement('p');
    textoListaVacia.textContent = 'Aquí aparecerán los partidos publicados';

    // Agrega los elementos al contenedor
    contenedor.appendChild(imgListaVacia);
    contenedor.appendChild(textoListaVacia);
    //container.replaceChildren();
}

function efectoClick(boton) {
    boton.classList.add('boton-click');
    setTimeout(() => {
        boton.classList.remove('boton-click');
    }, 100);
}

function mostrarDialogo(pregunta) {
    return new Promise((resolve, reject) => {
        const $dialogo = $('#confirmar-accion');
        
        // Mostrar el diálogo
        $dialogo.css("display", "flex");
        $dialogo.find('p').text(pregunta);

        // Evento para capturar clics fuera del diálogo
        $(document).on('mousedown', function (e) {
            if (!$(e.target).closest('#confirmar-accion').length) {
                $dialogo.css("display", "none");
                //$(document).off('mousedown.dialogo'); // Desvincular el evento
                reject(); // Rechazar la promesa
            }
        });

        // Cuando el usuario hace clic en "Cancelar"
        $('#cancelar-accion').off('click').on('click', function () {
            $dialogo.css("display", "none");
            //$(document).off('mousedown.dialogo'); // Desvincular el evento
            reject(); // Rechazar la promesa
        });

        // Cuando el usuario hace clic en "Aceptar"
        $('#aceptar-accion').off('click').on('click', function () {
            $dialogo.css("display", "none");
            //$(document).off('mousedown.dialogo'); // Desvincular el evento
            resolve(); // Resolver la promesa
        });
    });
}



function mostrarConfirmacionArchivado() {
    return new Promise((resolve, reject) => {
        // Mostrar el contenedor
        $('#confirmar-archivado').css('display', 'flex');

        // Evento para "Partido jugado"
        $('#archivar-jugado').off('click').click(() => {
            $('#confirmar-archivado').css('display', 'none');
            resolve(1); // Devuelve 1 si se selecciona "Partido jugado"
        });

        // Evento para "Partido cancelado"
        $('#archivar-cancelado').off('click').click(() => {
            $('#confirmar-archivado').css('display', 'none');
            resolve(2); // Devuelve 2 si se selecciona "Partido cancelado"
        });

        // Evento para "Cancelar"
        $('#cancelar-archivado').off('click').click(() => {
            $('#confirmar-archivado').css('display', 'none');
            reject('Archivado cancelado por el usuario.');
        });

        // Evento para cerrar si se hace clic fuera
        $(document).on('mousedown', function (e) {
            if (!$(e.target).closest('#confirmar-archivado').length) {
                $('#confirmar-archivado').css("display", "none");
                reject('Archivado cancelado por clic fuera del contenedor.');
                $(document).off('mousedown'); // Desvincular evento
            }
        });
    });
}


