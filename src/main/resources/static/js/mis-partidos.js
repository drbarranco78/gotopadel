$(document).ready(function () {
    // Añade un evento al hacer clic en el enlace "Mis Partidos"
    $("#enlace-mispartidos").click(function () {
        // Obtiene el ID del usuario actual
        getIdUsuario()
            .then(idUsuario => {
                // Almacena el ID del usuario activo
                idUsuarioActivo = idUsuario;
                // Carga los partidos del usuario
                cargarMisPartidos(idUsuarioActivo);
            })
            .catch(error => console.error('Error al obtener el id del usuario:', error));
    });
});

/**
 * Carga los partidos del usuario y los muestra en la interfaz.
 * Si no hay partidos, se muestra una lista vacía.
 * @param {number} idUsuario - El ID del usuario para obtener sus partidos.
 */
function cargarMisPartidos(idUsuario) {
    // Limpia el contenedor y establece el título de la sección
    fichaMiPartidoContainer.innerHTML = `<h2>Mis Partidos</h2>`;

    // Solicita los partidos del usuario al backend
    fetch(`/api/partido/misPartidos?usuario=${idUsuario}`)
        .then(res => res.json()) // Convierte la respuesta en JSON
        .then(partidos => {
            // Si no hay partidos, muestra la pantalla de "lista vacía"
            if (!partidos || partidos.length === 0) {
                fichaMiPartidoContainer.style.display = 'flex';
                mostrarListaVacia(fichaMiPartidoContainer);
                return;
            }

            // Configura el diseño del contenedor para mostrar las fichas de partidos
            fichaMiPartidoContainer.style.display = 'grid';
            fichaMiPartidoContainer.classList.add('mis-partidos');
            fichaMiPartidoContainer.classList.remove('mis-partidos-detalle');
            fichaMiPartidoContainer.classList.remove('lista-vacia');

            // Itera sobre cada partido y crea una ficha para mostrarlo
            partidos.forEach(partido => {
                // Clona el elemento de la plantilla para las fichas de partidos
                let fichaMiPartido = fichaMiPartidoTemplate.cloneNode(true);
                fichaMiPartido.style.display = 'block';

                // Determina si el usuario actual es el organizador del partido
                let organizador = idUsuario == partido.usuario.idUsuario;

                // Asigna los valores correspondientes a los elementos de la ficha
                fichaMiPartido.querySelector('.numero-partido').innerHTML = partido.idPartido;
                fichaMiPartido.querySelector('.organizador-partido span').innerHTML = partido.usuario.nombre;
                fichaMiPartido.querySelector('.fecha-partido span').innerHTML = partido.fechaPartido;
                fichaMiPartido.querySelector('.lugar-partido span').innerHTML = partido.ubicacion.nombre;

                // Configura el icono y el texto según si el usuario es organizador o inscrito
                if (organizador) {
                    fichaMiPartido.querySelector('.div-icono-ficha img').setAttribute("src", "../img/ico_usuario.png");
                    fichaMiPartido.querySelector('.div-icono-ficha .texto-icono').innerText = "Organizador";
                } else {
                    fichaMiPartido.querySelector('.div-icono-ficha img').setAttribute("src", "../img/ico_check.png");
                    fichaMiPartido.querySelector('.div-icono-ficha .texto-icono').innerText = "Inscrito";
                }

                // Añade un evento al botón para ver detalles del partido
                fichaMiPartido.querySelector('.bt-ver-detalles').addEventListener('click', function () {

                    verDetalles(partido, true, organizador);
                });

                // Añade la ficha del partido al contenedor
                fichaMiPartidoContainer.appendChild(fichaMiPartido);
            });
        })
        .catch(error => console.error('Error al cargar mis partidos:', error));
}

/**
 * Archiva un partido con un motivo específico.
 * @param {number} idPartido - El ID del partido a archivar.
 * @param {string} motivoArchivado - El motivo por el cual el partido es archivado.
 * @param {HTMLElement} btInscribe - El botón que se deshabilitará al archivar el partido.
 */
function archivarPartido(idPartido, motivoArchivado, btInscribe) {
    const url = `/api/archivo?idPartido=${idPartido}&motivoArchivado=${motivoArchivado}`;

    fetch(url, {
        method: 'POST' // Solicitud POST para archivar el partido
    })
        .then(response => {
            if (response.ok) {
                // Si el archivado es exitoso, muestra un mensaje y deshabilita el botón
                mostrarMensaje('Partido archivado correctamente', ".mensaje-exito");
                btInscribe.innerText = "Archivado"; // Cambia el texto del botón
                btInscribe.style.color = 'var(--color-resaltado)'; // Cambia el color del texto
                btInscribe.disabled = true; // Deshabilita el botón
            } else {
                // Muestra un mensaje de error si falla el archivado
                mostrarMensaje('No se ha podido archivar. Inténtalo de nuevo', ".mensaje-error");
            }
        })
        .catch(error => {
            console.error('Error al archivar el partido. Inténtalo de nuevo', error);
        });
}

/**
 * Cancela la inscripción de un usuario en un partido después de confirmación.
 * @param {number} idUsuario - El ID del usuario cuya inscripción se cancelará.
 * @param {number} idPartido - El ID del partido en el cual el usuario está inscrito.
 * @param {HTMLElement} btInscribe - El botón que se actualizará tras la cancelación.
 */
function cancelarInscripcion(idUsuario, idPartido, btInscribe = null) {
    // Muestra un diálogo de confirmación para la cancelación
    mostrarDialogo("¿Seguro que quieres cancelar la inscripción a este partido?")
        .then(() => {
            // Si el usuario confirma la acción, realiza la solicitud DELETE
            fetch(`/api/inscripciones/cancelar?idUsuario=${idUsuario}&idPartido=${idPartido}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {
                        // Si se cancela correctamente, actualiza el botón y muestra un mensaje
                        mostrarMensaje('Inscripción cancelada correctamente', ".mensaje-exito");
                        if (btInscribe) {
                            btInscribe.innerText = "Inscripción Cancelada";
                            btInscribe.style.color = 'var(--color-rojo)';
                            btInscribe.disabled = true;
                        }

                    } else {
                        // Muestra un mensaje de error si no se puede cancelar la inscripción
                        mostrarMensaje('No se ha podido cancelar la inscripción. Inténtalo de nuevo', ".mensaje-error");
                    }
                })
                .catch(error => {
                    console.error('Ha habido un error al cancelar la inscripción, inténtalo de nuevo', error);
                });
        })
        .catch(() => {
            // Si el usuario cancela la acción, registra un mensaje en la consola
            console.log("Acción cancelada");
        });
}
