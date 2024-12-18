// Carga por defecto los partidos disponibles al cargar el documento 
$(document).ready(function () {
    $('#admin-ver-partidos').click();
});
// window.addEventListener('unload', function () {
//     navigator.sendBeacon('/api/usuario/logout'); // Usa sendBeacon para asegurar que la solicitud se envía
// });

$('#admin-ver-partidos').click(function () {
    $.ajax({
        url: '/api/partido',  // URL del endpoint del backend
        type: 'GET',
        success: function (partidos) {
            // Limpiar el contenedor antes de cargar nuevos partidos
            $('.listados-admin').empty();
            if (partidos.length === 0) {
                mostrarListaVacia($('.listados-admin'), true);
            } else {

                let partidoHTML = `<h2 class="listados-admin-titulo">Partidos Disponibles</h2>`;
                // Recorrer la lista de partidos y crear elementos HTML para mostrarlos
                partidos.forEach(function (partido) {
                    partidoHTML += `
                    <div class="partido-item" id="partido-${partido.idPartido}">
                        <p>ID Partido: ${partido.idPartido}</p>
                        <p>ID del Organizador: ${partido.usuario.idUsuario}</p>
                        <p>Nombre del Organizador: ${partido.usuario.nombre}</p>
                        <p>Email del Organizador: ${partido.usuario.email}</p>
                        <p>Tipo de Partido: ${partido.tipoPartido}</p>
                        <p>Nº de Vacantes: ${partido.vacantes}</p>
                        <p>Nivel de Juego: ${partido.nivel}</p>
                        <p>Fecha del Partido: ${partido.fechaPartido}</p>
                        <p>Fecha de la Publicación: ${partido.fechaPublicacion}</p>
                        <p>Hora del Partido: ${partido.horaPartido}</p>
                        <p>Comentarios: ${partido.comentarios}</p>
                        <p>Ubicación: ${partido.ubicacion.nombre} (${partido.ubicacion.ciudad})</p>                       
                        <button class="eliminar-partido" data-id="${partido.idPartido}">Eliminar</button>
                    </div>
                `;                   
                });

                // Agregar el partido al contenedor de la lista
                $('.listados-admin').append(partidoHTML);
            }
        },
        error: function (error) {
            console.error("Error al cargar los partidos:", error);
        }
    });
});
$(document).on('click', '.eliminar-partido', function () {
    let idPartido = $(this).data('id'); // Obtener el ID del partido del atributo data-id

    // Llama a la función en Utilidades.js para confirmar la eliminación
    mostrarDialogo("¿Estás seguro de eliminar este partido?")
        .then(() => {
            // Si el usuario confirma, hace una petición AJAX para eliminar el partido
            $.ajax({
                url: '/api/partido/' + idPartido,
                type: 'DELETE',
                success: function () {
                    // Eliminar el elemento del DOM
                    $('#partido-' + idPartido).remove();
                    //alert("El partido con id " + idPartido + " ha sido eliminado");
                    mostrarMensaje("Partido eliminado correctamente", ".mensaje-exito");
                },
                error: function (error) {
                    console.error("Error al eliminar el partido:", error);
                    mostrarMensaje("Error al eliminar el partido", ".mensaje-error");
                }
            });
        })
        .catch(() => {
            console.log("Eliminación cancelada por el usuario");
        });
});
$('#admin-ver-usuarios').click(function () {
    $.ajax({
        url: '/api/usuario/listaUsuarios', // Endpoint para obtener usuarios
        type: 'GET',
        success: function (usuarios) {
            // Limpiar el contenedor antes de agregar nuevos usuarios
            $('.listados-admin').empty();

            if (usuarios.length === 0) {
                mostrarListaVacia($('.listados-admin'), true);
            } else {
                // Generar el título y el contenido de los usuarios
                let usuarioHTML = `<h2 class="listados-admin-titulo">Usuarios Registrados</h2>`;
                for (let index = 1; index < usuarios.length; index++) { // Comienza en 1 para omitir al administrador
                    let usuario = usuarios[index];

                    usuarioHTML += `
                    <div class="usuario-item" id="usuario-${usuario.idUsuario}">
                        <p>ID: ${usuario.idUsuario}</p>
                        <p>Nombre: ${usuario.nombre}</p>
                        <p>Email: ${usuario.email}</p>
                        <p>Fecha de Nacimiento: ${usuario.fechaNac}</p>
                        <p>Género: ${usuario.genero}</p>
                        <p>Nivel de Juego: ${usuario.nivel}</p>
                        <p>Fecha de inscripción: ${usuario.fechaInscripcion}</p>
                        <button class="eliminar-usuario" data-id="${usuario.idUsuario}">Eliminar</button>
                    </div>
                `;
                }
                // Añadir todo el HTML generado de una vez
                $('.listados-admin').html(usuarioHTML);
            }
        },
        error: function (error) {
            console.error("Error al cargar los usuarios:", error);
        }
    });
});


$(document).on('click', '.eliminar-usuario', function () {
    let idUsuario = $(this).data('id');

    mostrarDialogo("¿Estás seguro de eliminar este usuario?")
        .then(() => {
            $.ajax({
                url: '/api/usuario/adminEliminaUsuario/' + idUsuario,
                type: 'DELETE',
                success: function () {
                    // Eliminar el usuario del DOM
                    $('#usuario-' + idUsuario).remove();
                    alert("El usuario " + idUsuario + " ha sido eliminado");
                    mostrarMensaje("Usuario eliminado correctamente", ".mensaje-exito");
                },
                error: function (error) {
                    console.error("Error al eliminar el usuario:", error);
                    mostrarMensaje("Error al eliminar el usuario", ".mensaje-error");
                }
            });
        })
        .catch(() => {
            console.log("Eliminación cancelada por el usuario");
        });
});

$('#admin-ver-archivo').click(function () {
    $.ajax({
        url: '/api/archivo/obtenerPartidosArchivados',
        type: 'GET',
        success: function (archivados) {
            $('.listados-admin').empty();
            if (archivados.length === 0) {
                mostrarListaVacia($('.listados-admin'), true);
            } else {
                // Agregar título solo una vez
                let archivadoHTML = `<h2 class="listados-admin-titulo">Partidos Archivados</h2>`;
                archivados.forEach(function (archivado) {
                    archivadoHTML += `
                        <div class="archivo-item" id="archivado-${archivado.idPartido}">
                            <p>ID del partido: ${archivado.idPartido}</p>
                            <p>ID del organizador: ${archivado.idUsuario}</p>
                            <p>Tipo de partido: ${archivado.tipoPartido}</p>
                            <p>Nivel de Juego: ${archivado.nivel}</p>
                            <p>Fecha del partido: ${archivado.fechaPartido}</p>
                            <p>Fecha de Publicación: ${archivado.fechaPublicacion}</p>
                            <p>Tipo de partido: ${archivado.tipoPartido}</p>
                            <p>ID de Ubicación: ${archivado.ubicacion}</p>
                            <p>Fecha de Archivado: ${archivado.fechaArchivo}</p>
                            <p>Motivo de Archivado: ${archivado.motivoArchivado}</p>
                            <p>Comentarios: ${archivado.comentarios}</p>
                            <button class="eliminar-archivado" data-id="${archivado.idPartido}">Eliminar</button>
                        </div>
                    `;
                });
                // Añadir todo el HTML generado de una vez
                $('.listados-admin').html(archivadoHTML);
            }
        },



        error: function (error) {
            console.error("Error al cargar los partidos archivados:", error);
        }
    });
});

$('.listados-admin').on('click', '.eliminar-archivado', function () {
    const idPartido = $(this).data('id');

    mostrarDialogo("¿Estás seguro de eliminar este partido del archivo?")
        .then(() => {
            $.ajax({
                url: `/api/archivo/eliminarPartidoArchivado/${idPartido}`,
                type: 'DELETE',
                success: function () {
                    $(`#archivado-${idPartido}`).remove(); // Eliminar del DOM
                    mostrarMensaje("Partido archivado eliminado con éxito", ".mensaje-exito");                    
                },
                error: function (error) {
                    mostrarMensaje("Hubo un error al eliminar el partido archivado", ".mensaje-exito");                   
                }
            });
        })
        .catch(() => {
            console.log("Eliminación cancelada por el usuario");
        });
});