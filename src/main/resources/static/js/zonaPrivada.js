$(document).ready(function() {
    // Hacer una petición AJAX para obtener los datos del usuario desde el backend
    $.ajax({
        url: '/api/privado/datosUsuario',
        type: 'GET',
        success: function(response) {
            // Mostrar el mensaje de bienvenida con el nombre del usuario
            $("#mensaje-bienvenida").html("Bienvenido, " + response.nombreUsuario);
        },
        error: function(error) {
            console.error('Error:', error);
            // Redirigir al login si no está autenticado
            window.location.href = 'index.html';
        }
    });
});
