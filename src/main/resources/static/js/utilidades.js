/**
 * Verifica si la URL de una imagen tiene una extensión válida.
 * Esta función comprueba si la URL proporcionada termina con una de las extensiones de imagen válidas.
 * @param {string} url - La URL de la imagen a verificar.
 * @returns {boolean} Retorna true si la URL tiene una extensión válida (jpeg, jpg, png), de lo contrario, false.
 */
function esUrlImagenValida(url) {
    // Lista de extensiones válidas para las imágenes
    const extensionesValidas = ['jpeg', 'jpg', 'png'];
    return extensionesValidas.some(ext => url.endsWith(ext)); // Comprueba si termina con una extensión permitida
}

/**
 * Muestra un mensaje temporal en un elemento seleccionado, animándolo con fadeIn y fadeOut.
 * @param {string} mensaje - El mensaje a mostrar.
 * @param {string} selector - El selector del elemento donde mostrar el mensaje.
 */
function mostrarMensaje(mensaje, selector) {
    $(selector).html(`<h3>${mensaje}</h3>`).fadeIn(500).delay(2000).fadeOut(500);
}

/**
 * Formatea una fecha de formato 'yyyy-mm-dd' a 'dd/mm/yyyy'.
 * @param {string} fecha - La fecha a formatear.
 * @returns {string} - La fecha formateada.
 */
function formatearFecha(fecha) {
    let [anioNac, mesNac, diaNac] = fecha.split('-');
    fecha = `${diaNac}/${mesNac}/${anioNac}`;
    return fecha;
}

/**
 * Limpia el contenido y elimina las clases específicas de los contenedores de partidos.
 */
function limpiarContenedores() {
    if (fichaPartidoContainer) fichaPartidoContainer.innerHTML = '';
    fichaPartidoContainer.classList.remove('lista-vacia');

    if (fichaMiPartidoContainer) fichaMiPartidoContainer.innerHTML = '';
    fichaMiPartidoContainer.classList.remove('lista-vacia');
    fichaMiPartidoContainer.classList.remove('mis-partidos');
}

/**
 * Muestra un mensaje visual indicando que la lista está vacía y estiliza el contenedor.
 * @param {HTMLElement} container - El contenedor donde mostrar el mensaje.
 * @param {boolean} esAdmin - Indica si el usuario es administrador (opcional).
 */
function mostrarListaVacia(container, esAdmin = false) {
    if (!esAdmin) {
        limpiarContenedores(container);
    }
    let contenedor = container[0] || container;
    contenedor.classList.remove('ver-partidos');
    contenedor.classList.add('lista-vacia');

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
}

/**
 * Añade un efecto visual de clic a un botón.
 * @param {HTMLElement} boton - El botón al que aplicar el efecto.
 */
function efectoClick(boton) {
    boton.classList.add('boton-click');
    setTimeout(() => {
        boton.classList.remove('boton-click');
    }, 100);
}

/**
 * Muestra un cuadro de diálogo de confirmación con una promesa.
 * @param {string} pregunta - La pregunta a mostrar en el cuadro de diálogo.
 * @returns {Promise} - Se resuelve si el usuario acepta, se rechaza si cancela.
 */
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
                reject(); // Rechazar la promesa
            }
        });

        // Cuando el usuario hace clic en "Cancelar"
        $('#cancelar-accion').off('click').on('click', function () {
            $dialogo.css("display", "none");
            reject(); // Rechazar la promesa
        });

        // Cuando el usuario hace clic en "Aceptar"
        $('#aceptar-accion').off('click').on('click', function () {
            $dialogo.css("display", "none");
            resolve(); // Resolver la promesa
        });
    });
}


$('.cerrar-dialogo').click(function () {
    $('#confirmar-archivado').hide();
})
/**
 * Muestra un cuadro de confirmación exclusivo para archivar partidos.
 * @returns {Promise} - Se resuelve con un valor dependiendo de la selección del usuario.
 */
function mostrarConfirmacionArchivado() {
    return new Promise((resolve, reject) => {
        // Mostrar el contenedor
        $('#confirmar-archivado').css('display', 'flex');

        // Evento para "Partido jugado"
        $('#archivar-jugado').off('click').click(() => {
            $('#confirmar-archivado').css('display', 'none');
            resolve(1); // Devuelve 1 si se selecciona "Partido jugado"
            // $('#enlace-ver').click();
        });

        // Evento para "Partido cancelado"
        $('#archivar-cancelado').off('click').click(() => {
            $('#confirmar-archivado').css('display', 'none');
            resolve(2); // Devuelve 2 si se selecciona "Partido cancelado"
            // $('#enlace-ver').click();
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

/**
 * Maneja el evento de clic en los elementos con ID 'privacidad', 'ayuda' y 'acerca-de'.
 * Previene el comportamiento por defecto del enlace y llama a la función mostrarInfo
 * pasando el tipo de información a mostrar.
 * 
 * @param {Event} event - El evento de clic.
 */
$('#privacidad , #ayuda, #acerca-de').click(function (event) {
    event.preventDefault();
    mostrarInfo($(this).data('tipo'));
});

/**
 * Maneja el evento de clic en los botones de cierre ('cerrar-info' y 'btn-cerrar-info').
 * Al hacer clic, elimina la clase 'activo' del elemento con la clase 'bloqueo-pantalla',
 * cerrando así la ventana emergente de información.
 * 
 * @param {Event} event - El evento de clic.
 */
$('.cerrar-info, .btn-cerrar-info').click(function (event) {
    event.preventDefault();
    $('.bloqueo-pantalla').removeClass('activo');
});

/**
 * Muestra el contenido correspondiente según la sección seleccionada.
 * 
 * @param {string} seccion - El tipo de sección a mostrar. Puede ser "privacidad", "ayuda" o "acerca-de".
 */
function mostrarInfo(seccion) {
    let contenido = $('.contenido-info');
    contenido.empty();
    $('.btn-cerrar-info').show();
    contenido.removeClass('align-center');
    if (seccion === "privacidad") {
        contenido.html(`<h2>Política de Privacidad</h2>`);
        contenido.append(privacidad);
    } else if (seccion === "ayuda") {
        contenido.html(`<h2>Ayuda</h2>`); 
        contenido.append(ayuda);              
    } else if (seccion === "acerca-de") {
        $('.btn-cerrar-info').hide();
        contenido.addClass('align-center');
        contenido.html(`
            <h2>Acerca de</h2>
            <h3><strong>GoToPádel</strong></h3>
            <p>Proyecto final del Ciclo Formativo de Grado Superior de Desarrollo de Aplicaciones Web.</p>
            <p>Curso académico 2024/2025.</p>
            <p><strong>Autor:</strong><br>Daniel Rodríguez Barranco.</p>
            <p><strong>Tutor:</strong><br>José Javier Bermúdez Hernández.</p><br>            
        `);
        contenido.append(licencia);
    }

    $('.bloqueo-pantalla').addClass('activo');

}

