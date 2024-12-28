//let idUsuario; // Almacena el ID del usuario actual
let usuario;   // Almacena los datos del usuario actual
let opUsuario = $(".opciones-usuario"); // Referencia al menú de opciones del usuario

$(document).ready(function () {
    // Asegurar que el usuario cierra sesión al salir de la página   
    window.onbeforeunload = function (event) {
        navigator.sendBeacon('/api/usuario/logout');        
        event.preventDefault();
        event.returnValue = ''; // Necesario en algunos navegadores como Chrome
    };
    // Obtener datos del usuario desde el backend
    $.ajax({
        url: '/api/usuario/datosUsuario', // Endpoint para obtener datos del usuario
        type: 'GET',
        success: function (response) {
            // Mostrar mensaje de bienvenida con el nombre y género del usuario
            $("#mensaje-bienvenida").html("Bienvenid" + (response.genero === "Hombre" ? "o, " : "a, ") + response.nombre + " con id " + response.idUsuario);

            // Guardar los datos completos del usuario
            usuario = response;
        },
        error: function (error) {
            console.error('Error:', error);
            // Redirigir al login si ocurre un error de autenticación
            // window.location.href = '/';
        }
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
        mostrarSeccion('.ficha-usuario');
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
    function cargarDatosUsuario(usuario) {
        $('.icono-usuario-ficha').attr('src', usuario.genero === "Mujer" ? '../img/ico_usuaria.png' : '../img/ico_usuario.png');
        $('.nombre-usuario-ficha').text(usuario.nombre);
        $('.fecha-usuario-ficha span').text(usuario.fechaInscripcion);
        $('.id-usuario-ficha span').text(usuario.idUsuario);
        $('.nacimiento-usuario-ficha span').text(usuario.fechaNac);
        $('.genero-usuario-ficha span').text(usuario.genero);
        $('.nivel-usuario-ficha span').text(usuario.nivel);
        $.get(`/api/usuario/${usuario.idUsuario}/publicados/count`, function (count) {
            $('.publicados-usuario-ficha span').text(count);
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
