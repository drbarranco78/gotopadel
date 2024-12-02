let idUsuario;
let usuario;
let opUsuario = $(".opciones-usuario");
$(document).ready(function () {

    // Hacer una petición AJAX para obtener los datos del usuario desde el backend
    $.ajax({
        url: '/api/usuario/datosUsuario',
        type: 'GET',
        success: function (response) {
            // Mostrar el mensaje de bienvenida con el nombre del usuario
            $("#mensaje-bienvenida").html("Bienvenid" + (response.genero == "Hombre" ? "o, " : "a, ") + response.nombre + " con id " + response.idUsuario);
            idUsuario = response.idUsuario;
            usuario = response;

        },
        error: function (error) {
            console.error('Error:', error);
            // Redirigir al login si no está autenticado
            //window.location.href = 'index.html';
        }
    });
    // $("#enlace-inicio").click(function () {
    //     $('#cerrar-sesion').click();
        
    // })
    $("#enlace-ver").addClass("activo");
    function mostrarSeccion(seccionId) {
        // Oculta todas las secciones
        $(".contenido").hide();
        // Muestra la sección seleccionada
        $(seccionId).show();
    }

    function resaltarEnlace(enlace) {
        $("nav ul li a").removeClass("activo");
        $(enlace).addClass("activo");
    }


    // Evento para el enlace "Ver Partidos"
    $("#enlace-ver").click(function (event) {
        // Previene la acción por defecto del enlace
        event.preventDefault();
        mostrarSeccion("#ver-partidos");
        resaltarEnlace(this);
        // Forzar la recarga del archivo ver-partidos.js
        // Aquí estamos eliminando el script anterior y volviéndolo a cargar
        let scriptTag = document.createElement('script');
        scriptTag.src = 'js/ver-partidos.js';
        document.body.appendChild(scriptTag);
    });

    // Evento para el enlace "Publicar Partidos"
    $("#enlace-publicar").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#publicar-partidos");
        resaltarEnlace(this);
    });

    // Evento para el enlace "Mis Partidos"
    $("#enlace-mispartidos").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#mis-partidos");
        resaltarEnlace(this);
    });

    // Evento para el enlace "Estadísticas"
    $("#enlace-estadisticas").click(function (event) {
        event.preventDefault();
        mostrarSeccion("#estadisticas");
        resaltarEnlace(this);
    });

    // Muestra por defecto la sección "Ver Partidos"
    mostrarSeccion("#ver-partidos");

    $('#icono-usuario').click(function () {
        efectoClick(this);
        // let opUsuario = $(".opciones-usuario");
        if (opUsuario.css("display") === "none") {
            opUsuario.slideDown(300);
            opUsuario.css("display", "flex");
        } else {
            opUsuario.slideUp(300);
            // opUsuario.css("display", "none");
        }
    });
    $(document).click(function (event) {
        // let opUsuario = $(".opciones-usuario");
        
        // Verificar si el elemento está visible y el clic es fuera de él
        if (opUsuario.css("display") !== "none" && !$(event.target).closest(".opciones-usuario, #icono-usuario").length) {
            opUsuario.slideUp(300);  // Ocultar el menú
        }
    });

    $('#ver-perfil').click(function () {
        efectoClick(this);
        let opUsuario = $(".opciones-usuario");
        opUsuario.slideUp(300);
        // limpiarContenedores();
        $('#ver-partidos').css("display", "none");
        $('#mis-partidos').css("display", "none");
        $('#publicar-partidos').css("display", "none");

        $('.datos-usuario').css("display", "flex");
        $('.ficha-usuario').css("display", "flex");
        cargarDatosUsuario(usuario);
    });

    $('#enlace-inicio, #cerrar-sesion, #logo-cabecera').click(function (event) {
        event.preventDefault();
        efectoClick(this);
        mostrarDialogo("Cerrar la sesión y volver al inicio ?")
            .then(() => {
                $.ajax({
                    url: '/api/usuario/logout', // Nueva URL para cerrar la sesión
                    type: 'POST',
                    success: function () {
                        console.log("Sesión cerrada correctamente");
                        window.location.href = '/';
                    },
                    error: function (error) {
                        console.error('Error al cerrar la sesión:', error);
                        mostrarMensaje("Error al cerrar la sesión. Inténtalo de nuevo.", ".mensaje-error");
                    }
                });

            })
            .catch(() => {
                // Si el usuario cancela la acción
                console.log("Acción cancelada");
            });
    });

    function cargarDatosUsuario(usuario) {
        if (usuario.genero === "Mujer") {
            $('.icono-usuario-ficha').attr('src', '../img/ico_usuaria.png');
        } else {
            $('.icono-usuario-ficha').attr('src', '../img/ico_usuario.png');
        }
        $('.nombre-usuario-ficha').text(usuario.nombre);
        $('.fecha-usuario-ficha span').text(usuario.fechaInscripcion);
        $('.id-usuario-ficha span').text(usuario.idUsuario);
        $('.nacimiento-usuario-ficha span').text(usuario.fechaNac);
        $('.genero-usuario-ficha span').text(usuario.genero);
        $('.nivel-usuario-ficha span').text(usuario.nivel);
    }


    $('#ver-partidos-usuario-ficha').click(function () {
        efectoClick(this);
        $("#enlace-mispartidos").click();

    });

    $('#eliminar-cuenta-usuario').click(function () {
        console.log("Botón eliminar cuenta pulsado");
        efectoClick(this);

        mostrarDialogo("Seguro que quieres eliminar tu cuenta de usuario? Esta acción es irreversible")
            .then(() => {
                // Si el usuario confirma la acción
                $.ajax({
                    url: '/api/usuario/eliminarCuenta/' + idUsuario, // Usar la variable `idUsuario`
                    type: 'DELETE',
                    success: function (response) {
                        mostrarMensaje('La cuenta ha sido eliminada', ".mensaje-exito");
                        console.log('Cuenta eliminada correctamente');

                        // Esperar 3 segundos (3000 milisegundos) antes de redirigir
                        setTimeout(function () {
                            window.location.href = '/';
                        }, 3000); // Cambia 3000 por el número de milisegundos que quieras esperar
                    },
                    error: function (error) {
                        console.error('Error:', error);
                        mostrarDialogo("Error al eliminar la cuenta. Inténtalo de nuevo.");
                    }
                });
            })
            .catch(() => {
                // Si el usuario cancela la acción
                console.log("El usuario canceló la eliminación de la cuenta.");
            });
    });


});
