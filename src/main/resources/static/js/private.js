//let idUsuario; // Almacena el ID del usuario actual
let usuario;   // Almacena los datos del usuario actual
let opUsuario = $(".opciones-usuario"); // Referencia al menú de opciones del usuario
let notificacionesObtenidas;

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
            //obtenerNotificacionesCancelaciones(usuario.idUsuario);
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
    function obtenerNotificaciones(usuario) {
        console.log("ID de usuario en la app web:", usuario.idUsuario);

        $.ajax({
            url: '/api/notificaciones/usuario/' + usuario.idUsuario,
            type: 'POST',
            success: function (response) {
                notificacionesObtenidas = response; // Asignamos el valor globalmente

                // Eliminar solo las notificaciones previas, no el header
                $('.detalle-notificaciones .notificacion').remove();

                if (notificacionesObtenidas.length > 0) {
                    $(".notificaciones").show();
                    console.log("Inscripciones notificadas: ", notificacionesObtenidas);
                } else {
                    console.log("No hay notificaciones pendientes.");
                    $(".notificaciones").hide();
                }
            },
            error: function (error) {
                console.error('Error:', error);
            }
        });
    }

    function obtenerNotificacionesCancelaciones(idUsuario) {
        $.get(`/api/notificaciones/usuario/${idUsuario}`)
        .done(function (notificaciones) {
            console.log("Notificaciones recibidas:", notificaciones);
            //mostrarNotificaciones(notificaciones); 
        })
        .fail(function (error) {
            console.error("Error al obtener notificaciones:", error.responseText);
        });
    }
    
    // Evento de clic para mostrar notificaciones
    $(".notificaciones").click(function (event) {
        event.preventDefault();
        cargarNotificaciones(notificacionesObtenidas);


    });
    window.marcarNotificacionesComoLeidas=function(usuario) {
        $.ajax({
            url: '/api/inscripciones/marcarComoLeidas/' + usuario.idUsuario,
            type: 'POST',
            success: function (response) {
                console.log("Notificaciones marcadas como leídas");
            },
            error: function (error) {
                console.error('Error al marcar notificaciones:', error);
            }
        });
    }


    function cargarNotificaciones(notificacionesObtenidas) {
        $('.detalle-notificaciones .notificacion').remove();
        let html = '';
    
        notificacionesObtenidas.forEach((notificacion, index) => {
            let idNotificacion = notificacion.id;
            let emisor = notificacion.emisor || {};
            let partido = notificacion.partido || {};
            let organizador = partido.usuario || {}; // Usamos partido.usuario como el organizador
            let mensaje = '';
    
            console.log("Notificación recibida:", notificacion);
    
            switch (notificacion.tipo) {
                case "inscripción":
                    mensaje = `<strong>${emisor.nombre || 'Usuario desconocido'}</strong> se ha inscrito en tu partido
                    <i class="ver-perfil-inscrito fas fa-eye" data-usuario='${JSON.stringify(notificacion.emisor || {})}'></i>`;
                    break;
                case "rechazo":
                    mensaje = `Tu inscripción al partido <strong>${partido.tipoPartido || 'desconocido'}</strong> ha sido rechazada por el organizador <strong>${emisor.nombre || 'Usuario desconocido'}</strong>.`;
                    break;
                case "cancelación":
                    mensaje = `El partido <strong>${partido.tipoPartido || 'desconocido'}</strong> ha sido cancelado por el organizador <strong>${emisor.nombre || 'Usuario desconocido'}</strong>.`;
                    break;
                case "cancelado-usuario":
                    mensaje = `<strong>${emisor.nombre || 'Usuario desconocido'}</strong> ha cancelado su inscripción al partido <strong>${partido.tipoPartido || 'desconocido'}</strong> organizado por <strong>${organizador.nombre || 'Organizador desconocido'}</strong>.`;
                    break;
            }
    
            html += `
                <div class="notificacion" data-id="${idNotificacion}" data-usuario-id="${emisor.idUsuario || ''}" data-partido-id="${partido.idPartido || ''}">
                    <p>${mensaje}</p>
                    <p>Fecha: ${new Date(notificacion.fechaCreacion).toLocaleString()}</p>
            `;
    
            if (notificacion.tipo === "inscripción") {
                html += `
                    <p>Email: ${emisor.email || 'No disponible'}</p>
                    <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'} el ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>
                    <button class="btn-aceptar" data-id="${idNotificacion}">Aceptar</button>
                    <button class="btn-rechazar" data-id="${idNotificacion}">Rechazar</button>
                `;
            } else if (notificacion.tipo === "rechazo" || notificacion.tipo === "cancelación") {
                html += `
                    <p>Organizador: ${emisor.nombre || 'Usuario desconocido'} (${emisor.email || 'No disponible'})</p>
                    <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'}</p>
                    <p>Fecha original: ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>
                    <button class="btn-aceptar" data-id="${idNotificacion}">Aceptar</button>
                `;
            } else if (notificacion.tipo === "cancelado-usuario") {
                html += `
                    <p>Usuario: ${emisor.nombre || 'Usuario desconocido'} (${emisor.email || 'No disponible'})</p>
                    <p>Organizador: ${organizador.nombre || 'Organizador desconocido'} (${organizador.email || 'No disponible'})</p>
                    <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'}</p>
                    <p>Fecha original: ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>
                    <button class="btn-aceptar" data-id="${idNotificacion}">Aceptar</button>
                `;
            }
    
            html += `</div>`;
        });
    
        $('.detalle-notificaciones').append(html);
        $('.detalle-notificaciones').show();
    }
    
    // Delegación de eventos para manejar elementos dinámicos
    $(document).on('click', '.btn-aceptar', function () {
        let idNotificacion = $(this).attr("data-id");
        console.log("Aceptando notificación:", idNotificacion);
        // $(`.notificacion[data-id="${idNotificacion}"]`).remove();

        // // Verificamos si no hay más notificaciones y ocultamos el contenedor si es necesario
        // if ($('.detalle-notificaciones .notificacion').length === 0) {
        //     $(".notificaciones").hide();
        //     $(".detalle-notificaciones").hide();
        // }
        eliminarNotificacion(idNotificacion);
        //marcarNotificacionesComoLeidas(usuario);
    });
    $(document).on('click', '.btn-rechazar', function () {
        let idNotificacion = $(this).attr("data-id");
    
        // Buscar la notificación en el DOM y obtener los datos
        let notificacionElement = $(`.notificacion[data-id="${idNotificacion}"]`);
        let idUsuario = notificacionElement.data("usuario-id");
        let idPartido = notificacionElement.data("partido-id");
    
        //$(`.notificacion[data-id="${idNotificacion}"]`).remove();
        
        mostrarDialogo("¿Seguro que quieres cancelar esta inscripción?")
        .then(() => {
            let idEmisor = usuario.idUsuario; 
    
            // Primero creamos la notificación
            $.post("/api/notificaciones/crear", {
                idEmisor: idEmisor,
                idReceptor: idUsuario,
                idPartido: idPartido,
                tipo: "rechazo"
            }).done(() => {
                eliminarNotificacion(idNotificacion);
                console.log("Notificación creada con éxito.");
                // Luego cancelamos la inscripción
                cancelarInscripcion(idUsuario, idPartido);
            }).fail((error) => {
                console.error("Error al crear la notificación:", error.responseText);
            });
        })
        .catch(() => {
            console.log("Acción cancelada");
        });
    
        // if ($('.detalle-notificaciones .notificacion').length === 0) {
        //     $(".notificaciones").hide();
        //     $(".detalle-notificaciones").hide();
        // }
    });

    function eliminarNotificacion(idNotificacion) {
        $.ajax({
            url: `/api/notificaciones/eliminar/${idNotificacion}`,
            type: 'DELETE',
            success: function(response) {
                // Eliminar la notificación del DOM
                $(`.notificacion[data-id="${idNotificacion}"]`).remove();
                
                // Opcional: Verificar si ya no hay notificaciones
                if ($('.detalle-notificaciones .notificacion').length === 0) {
                    $(".notificaciones").hide();
                    $('.detalle-notificaciones').hide();
                }
                
                console.log(`Notificación ${idNotificacion} eliminada con éxito`);
            },
            error: function(xhr, status, error) {
                console.error('Error al eliminar la notificación:', error);
                // Opcional: Mostrar mensaje de error al usuario
                alert('No se pudo eliminar la notificación. Por favor, intenta de nuevo.');
            }
        });
    }
    
    

    // Cerrar notificaciones
    $(document).on('click', '#cerrar-notificaciones', function () {
        $('.detalle-notificaciones').hide();
        $('.ficha-usuario').hide();
    });
    $(document).on('click', '.ver-perfil-inscrito', function () {
        // Recuperar el objeto usuario completo desde el atributo 'data-usuario'
        let usuario = JSON.parse($(this).attr("data-usuario"));
        console.log("Ver perfil del usuario:", usuario);
        $(".detalle-notificaciones").hide();
        // Llamamos a la función cargarDatosUsuario pasando el objeto usuario completo
        cargarDatosUsuario(usuario);
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
     * @param {Object} event - El evento de clic
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

        cargarDatosUsuario(usuario); // Cargar datos del usuario en la ficha
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
                        console.log("Sesión cerrada correctamente");
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
     * @param {Object} usuario - Los datos del usuario a cargar
     */
    function cargarDatosUsuario(user) {
        $('.ficha-usuario').show();
        //mostrarSeccion('.ficha-usuario');
        if (usuario.idUsuario !== user.idUsuario) {
            $('.botones-ficha-usuario').hide();
        } else {
            $('.botones-ficha-usuario').show();
        }
        $('.icono-usuario-ficha').attr('src', user.genero === "Mujer" ? '../img/ico_usuaria.png' : '../img/ico_usuario.png');
        $('.nombre-usuario-ficha').text(user.nombre);
        $('.ficha-usuario-fecha span').text(user.fechaInscripcion);
        $('.id-usuario-ficha span').text(user.idUsuario);
        $('.nacimiento-usuario-ficha span').text(user.fechaNac);
        $('.genero-usuario-ficha span').text(user.genero);
        $('.nivel-usuario-ficha span').text(user.nivel);
        $.get(`/api/usuario/${user.idUsuario}/publicados/count`, function (count) {
            $('.publicados-usuario-ficha span').text(count);
        });
        $.get(`/api/inscripciones/cantidad/${user.idUsuario}`, function (count) {
            $('.inscrito-usuario-ficha span').text(count);
        });
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
