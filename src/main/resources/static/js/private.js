let usuario;   // Almacena los datos del usuario actual
let opUsuario = $(".opciones-usuario"); // Referencia al menú de opciones del usuario
let notificacionesObtenidas;
let detallePartido;

$(document).ready(function () {
    // Asegurar que el usuario cierra sesión al salir de la página   
    // window.onbeforeunload = function (event) {
    //     navigator.sendBeacon('/api/usuario/logout');
    //     event.preventDefault();
    //     event.returnValue = ''; // Necesario en algunos navegadores como Chrome
    // };

    // Obtener datos del usuario desde el backend
    $.ajax({
        url: '/api/usuario/datosUsuario',
        type: 'GET',
        success: function (response) {
            usuario = response;
            $("#mensaje-bienvenida").html("Bienvenid" + (usuario.genero === "Hombre" ? "o, " : "a, ") + usuario.nombre + " con id " + usuario.idUsuario);
            obtenerNotificaciones(usuario);

        },
        error: function (error) {
            console.error('Error:', error);
        }
    });

    /**
     * Obtiene las notificaciones de un usuario y las muestra en la interfaz.
     * Realiza una solicitud AJAX al backend para recuperar las notificaciones asociadas al usuario.
     * 
     * @param {Object} usuario - Objeto que representa al usuario actual. Debe contener el ID del usuario.
     */
    function obtenerNotificaciones(usuario) {

        $.ajax({
            url: '/api/notificaciones/usuario/' + usuario.idUsuario,
            type: 'POST',
            success: function (response) {
                notificacionesObtenidas = response; // Asignamos el valor globalmente

                // Eliminar solo las notificaciones previas, no el header
                $('.detalle-notificaciones .notificacion').remove();

                if (notificacionesObtenidas.length > 0) {
                    $(".notificaciones").show();                    
                } else {                    
                    $(".notificaciones").hide();
                }
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    }

    /**
     * Evento de clic para mostrar las notificaciones al usuario.
     * Cuando el usuario hace clic en el icono de notificaciones, se cargan y muestran en pantalla.    
     */
    $(".notificaciones").click(function (event) {
        event.preventDefault();
        cargarNotificaciones(notificacionesObtenidas);
    });

    /**
     * Carga y muestra las notificaciones en la interfaz de usuario.
     * Recorre la lista de notificaciones y las agrega al contenedor correspondiente,
     * incluyendo botones de acción según el tipo de notificación.
     * 
     * @param {Array} notificacionesObtenidas - Lista de notificaciones obtenidas para el usuario actual.
     */
    function cargarNotificaciones(notificacionesObtenidas) {
        $('.detalle-notificaciones .notificacion').remove();
        let html = '';

        notificacionesObtenidas.forEach((notificacion, index) => {
            let idNotificacion = notificacion.id;
            let emisor = notificacion.emisor || {};
            let partido = notificacion.partido || {};           
            let mensaje = notificacion.mensaje;
            html += `
                <div class="notificacion" data-id="${idNotificacion}" data-usuario="${emisor.idUsuario || ''}" data-partido="${partido.idPartido || ''}">
                    <p>${mensaje}</p>
                    <p>Fecha: ${new Date(notificacion.fechaCreacion).toLocaleString()}</p>
            `;
            // Si es una inscripción se incluye un botón para rechazarla
            if (notificacion.tipo === "inscripcion") {
                html += `                    
                    <button class="btn-aceptar" data-id="${idNotificacion}">Aceptar</button>
                    <button class="btn-rechazar" data-id="${idNotificacion}">Rechazar</button>
                `;
            } else {
                html += `                    
                    <button class="btn-aceptar" data-id="${idNotificacion}">Aceptar</button>
                `;
            }
            html += `</div>`;
        });

        $('.detalle-notificaciones').append(html);
        $('.detalle-notificaciones').show();
    }

    /**
     * Evento de clic para aceptar una notificación.
     * Cuando el usuario hace clic en "Aceptar", se elimina la notificación correspondiente.     
     */
    $(document).on('click', '.btn-aceptar', function () {
        let idNotificacion = $(this).attr("data-id");        
        eliminarNotificacion(idNotificacion);
    });
    /**
     * Evento de clic para rechazar una notificación.
     * Recupera la notificación del servidor, extrae información relevante del partido,
     * y genera una nueva notificación informando el rechazo de la inscripción.   
     */
    $(document).on('click', '.btn-rechazar', function () {
        let idNotificacion = $(this).attr("data-id");
        $.ajax({
            url: `/api/notificaciones/${idNotificacion}`,
            method: 'GET',
            success: function (notificacion) {
                let mensajeRecibido = notificacion.mensaje;

                // Creamos un contenedor temporal para convertir el string HTML en elementos jQuery
                let $tempContainer = $('<div>').html(mensajeRecibido);

                // Buscamos el párrafo que contiene el atributo data-partido
                let $partidoP = $tempContainer.find('p[data-partido]');
                let idPartido = null;
                let detallePartido = "Información no disponible";

                if ($partidoP.length > 0) {
                    // Obtenemos el atributo data-partido y lo parseamos como JSON
                    let dataPartidoAttr = $partidoP.attr('data-partido');
                    try {
                        let dataPartido = JSON.parse(dataPartidoAttr);
                        idPartido = dataPartido.idPartido;
                    } catch (error) {
                        console.error("Error al parsear data-partido:", error);
                    }
                    // Extraemos el contenido HTML del párrafo (la descripción del partido)
                    detallePartido = $partidoP.html().trim();
                }

                let mensajeNotificacion = `
                    Tu inscripción al <strong>${detallePartido}</strong> 
                    ha sido rechazada por el organizador 
                    <p>Organizador: ${notificacion.receptor.nombre || 'Usuario desconocido'} (${notificacion.receptor.email || 'No disponible'})</p>
                `;

                // Llamar a la función cancelar inscripción y crear la notificación de rechazo
                if (idPartido) {
                    cancelarInscripcion(notificacion.emisor.idUsuario, idPartido);
                    crearNotificacion(notificacion.receptor.idUsuario, notificacion.emisor.idUsuario, mensajeNotificacion, "rechazo");
                    eliminarNotificacion(idNotificacion);
                } else {
                    console.log("No se encontró el ID del partido.");
                }
            },
            error: function () {
                alert("Error al obtener la notificación.");
            }
        });
    });

    /**
     * Elimina una notificación específica mediante una petición AJAX al servidor.
     * Una vez eliminada, se elimina también del DOM. Si no quedan más notificaciones,
     * oculta la sección de notificaciones.
     * 
     * @param {number} idNotificacion - ID de la notificación a eliminar.
     */
    function eliminarNotificacion(idNotificacion) {
        $.ajax({
            url: `/api/notificaciones/eliminar/${idNotificacion}`,
            type: 'DELETE',
            success: function (response) {
                // Eliminar la notificación del DOM
                $(`.notificacion[data-id="${idNotificacion}"]`).remove();
                
                if ($('.detalle-notificaciones .notificacion').length === 0) {
                    $(".notificaciones").hide();
                    $('.detalle-notificaciones').hide();
                }
            },
            error: function (error) {
                console.error('Error al eliminar la notificación:', error);                
                alert('No se pudo eliminar la notificación. Por favor, intenta de nuevo.');
            }
        });
    }

    /**
     * Evento de clic para cerrar el panel de notificaciones.
     * Oculta tanto la lista de notificaciones como la ficha del usuario.     
     */
    $(document).on('click', '#cerrar-notificaciones', function () {
        $('.detalle-notificaciones').hide();
        $('.ficha-usuario').hide();
    });

    /**
     * Evento de clic para ver el perfil de un usuario inscrito.
     * Recupera la información del usuario desde un atributo `data-usuario`,
     * la convierte a un objeto JSON y llama a la función `cargarDatosUsuario`.    
     */
    $(document).on('click', '.ver-perfil-inscrito', function () {
        // Recuperar el objeto usuario completo desde el atributo 'data-usuario'
        let usuario = JSON.parse($(this).attr("data-usuario"));        
        $(".detalle-notificaciones").hide();
        // Llamamos a la función cargarDatosUsuario pasando el objeto usuario completo
        cargarDatosUsuario(usuario, $('.ficha-usuario'));
    });

    // Añadir clase activa al enlace predeterminado
    $("#enlace-ver").addClass("activo");

    /**
     * Muestra una sección específica y oculta el resto de las secciones.
     * @param {string} seccionId - El ID de la sección a mostrar
     */
    function mostrarSeccion(seccionId) {
        $(".contenido").hide(); // Ocultar todas las secciones
        $(seccionId).show();   // Mostrar la sección seleccionada
    }

    /**
     * Resalta el enlace activo en la barra de navegación.
     * @param {Object} enlace - El enlace a resaltar
     */
    function resaltarEnlace(enlace) {
        $("nav ul li a").removeClass("activo");
        $(enlace).addClass("activo");
    }

    // Eventos para gestionar los enlaces de navegación
    $("#enlace-ver").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#ver-partidos"); // Mostrar la sección de "Ver Partidos"
        resaltarEnlace(this);
    });

    $("#enlace-publicar").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#publicar-partidos"); // Mostrar la sección de "Publicar Partidos"
        resaltarEnlace(this);
    });

    $("#enlace-mispartidos").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#mis-partidos"); // Mostrar la sección de "Mis Partidos"
        resaltarEnlace(this);
    });

    // Mostrar por defecto la sección "Ver Partidos"
    mostrarSeccion("#ver-partidos");

    /**
     * Gestiona la apertura y cierre del menú de opciones del usuario.
     */
    $('#icono-usuario').click(function () {
        efectoClick(this);
        if (opUsuario.css("display") === "none") {
            opUsuario.slideDown(300); // Mostrar menú con animación
            opUsuario.css("display", "flex");
        } else {
            opUsuario.slideUp(300); // Ocultar menú con animación
        }
    });

    /**
     * Cierra el menú si se hace clic fuera de él.     
     */
    $(document).click(function (event) {
        if (opUsuario.css("display") !== "none" && !$(event.target).closest(".opciones-usuario, #icono-usuario").length) {
            opUsuario.slideUp(300);
        }
    });

    /**
     * Muestra la ficha del usuario al hacer clic en "Ver Perfil".
     */
    $('#ver-perfil').click(function () {
        efectoClick(this);        
        opUsuario.slideUp(300);
        cargarDatosUsuario(usuario, $('.ficha-usuario'));
    });

    /**
     * Cierra sesión con una confirmación antes de redirigir al inicio.
     */
    $('#enlace-inicio, #cerrar-sesion, #logo-cabecera, #logo-cabecera-admin').click(function (event) {
        event.preventDefault();
        efectoClick(this);
        mostrarDialogo("Cerrar la sesión y volver al inicio?")
            .then(() => {
                $.ajax({
                    url: '/api/usuario/logout', // Endpoint para cerrar sesión
                    type: 'POST',
                    success: function () {                       
                        window.location.href = '/'; // Redirigir al inicio
                    },
                    error: function () {
                        mostrarMensaje("Error al cerrar la sesión. Inténtalo de nuevo.", ".mensaje-error");
                    }
                });
            })
            .catch(() => {
                console.log("Acción cancelada");
            });
    });
    /**
     * Carga los datos del usuario en su ficha.
     * @param {Object} user - Los datos del usuario a cargar
     * @param {Object} ficha - El contenedor de los datos
     */
    window.cargarDatosUsuario = function (user, ficha) {     

        if (usuario.idUsuario !== user.idUsuario) {
            $('.botones-ficha-usuario').hide();
        } else {
            $('.botones-ficha-usuario').show();
        }
        ficha.find('.icono-usuario-ficha').attr('src', user.genero === "Mujer" ? '../img/ico_usuaria.png' : '../img/ico_usuario.png');
        ficha.find('.nombre-usuario-ficha').text(user.nombre);
        ficha.find('.ficha-usuario-fecha-span').text(user.fechaInscripcion);
        ficha.find('.id-usuario-ficha span').text(user.idUsuario);
        ficha.find('.nacimiento-usuario-ficha span').text(user.fechaNac);
        ficha.find('.genero-usuario-ficha span').text(user.genero);
        ficha.find('.nivel-usuario-ficha span').text(user.nivel);

        $.get(`/api/usuario/${user.idUsuario}/publicados/count`, function (count) {
            ficha.find('.publicados-usuario-ficha span').text(count);
        });

        $.get(`/api/inscripciones/cantidad/${user.idUsuario}`, function (count) {
            ficha.find('.inscrito-usuario-ficha span').text(count);
        });

        ficha.show();
    }

    /**
     * Muestra los partidos del usuario desde su ficha.
     */
    $('#ver-partidos-usuario-ficha').click(function () {
        efectoClick(this);
        $("#enlace-mispartidos").click(); // Simula clic en "Mis Partidos"
    });

    /**
     * Elimina la cuenta del usuario con confirmación antes de proceder.
     */
    $('#eliminar-cuenta-usuario').click(function () {
        efectoClick(this);
        mostrarDialogo("Seguro que quieres eliminar tu cuenta de usuario? Esta acción es irreversible")
            .then(() => {
                $.ajax({
                    url: '/api/usuario/eliminarCuenta/' + usuario.idUsuario, // Endpoint para eliminar cuenta
                    type: 'DELETE',
                    success: function () {
                        mostrarMensaje('La cuenta ha sido eliminada', ".mensaje-exito");
                        setTimeout(function () {
                            window.location.href = '/'; // Redirigir tras eliminar cuenta
                        }, 3000); // Esperar 3 segundos
                    },
                    error: function () {
                        mostrarDialogo("Error al eliminar la cuenta. Inténtalo de nuevo.");
                    }
                });
            })
            .catch(() => {
                console.log("El usuario canceló la eliminación de la cuenta.");
            });
    });


});
