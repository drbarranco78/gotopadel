let idUsuarioActivo;
// let fichaPartidoDetalles;
// let nuevoPartido;

$(document).ready(function () {

    $("#enlace-mispartidos").click(function () {
        getIdUsuario()
            .then(idUsuario => {
                idUsuarioActivo = idUsuario;
                cargarMisPartidos(idUsuarioActivo); // Llama aquí después de obtener el ID
            })
            .catch(error => console.error('Error al obtener el id del usuario:', error));
    });
});

function cargarMisPartidos(idUsuario) {

    fichaMiPartidoContainer.innerHTML = `<h2>Mis Partidos</h2>`; // Limpia el contenedor antes de cargar

    fetch(`/api/partido/misPartidos?usuario=${idUsuario}`)
        .then(res => res.json())
        .then(partidos => {
            if (!partidos || partidos.length === 0) {
                fichaMiPartidoContainer.style.display = 'flex';
                mostrarListaVacia(fichaMiPartidoContainer);
                return;
            }

            fichaMiPartidoContainer.style.display = 'grid';
            fichaMiPartidoContainer.classList.add('mis-partidos');
            fichaMiPartidoContainer.classList.remove('mis-partidos-detalle');
            fichaMiPartidoContainer.classList.remove('lista-vacia');

            partidos.forEach(partido => {
                // Clona el elemento de la plantilla
                let fichaMiPartido = fichaMiPartidoTemplate.cloneNode(true);
                fichaMiPartido.style.display = 'block';

                // Determina si el usuario actual es el organizador del partido
                let organizador = idUsuario == partido.usuario.idUsuario;

                // Asigna los valores correspondientes
                fichaMiPartido.querySelector('.numero-partido').innerHTML = partido.idPartido;
                fichaMiPartido.querySelector('.organizador-partido span').innerHTML = partido.usuario.nombre;
                fichaMiPartido.querySelector('.fecha-partido span').innerHTML = partido.fechaPartido;
                fichaMiPartido.querySelector('.lugar-partido span').innerHTML = partido.ubicacion.nombre;

                // Configura el icono y el texto de acuerdo a si el usuario es organizador o inscrito
                if (organizador) {
                    fichaMiPartido.querySelector('.div-icono-ficha img').setAttribute("src", "../img/ico_usuario.png");
                    fichaMiPartido.querySelector('.div-icono-ficha .texto-icono').innerText = "Organizador";

                } else {
                    fichaMiPartido.querySelector('.div-icono-ficha img').setAttribute("src", "../img/ico_check.png");
                    fichaMiPartido.querySelector('.div-icono-ficha .texto-icono').innerText = "Inscrito";
                }

                // Evento para ver detalles del partido
                fichaMiPartido.querySelector('.bt-ver-detalles').addEventListener('click', function () {
                    // fichaMiPartidoContainer.classList.remove('mis-partidos');
                    // fichaMiPartidoContainer.classList.add('mis-partidos-detalle');
                    verDetalles(partido, true, organizador);
                });

                // Añade la ficha clonada al contenedor
                fichaMiPartidoContainer.appendChild(fichaMiPartido);
            });
        })
        .catch(error => console.error('Error al cargar mis partidos:', error));
}

function archivarPartido(idPartido, motivoArchivado, btInscribe) {
    console.log("ID Partido:", idPartido);
    console.log("Motivo Archivado:", motivoArchivado);
    const url = `/api/archivo?idPartido=${idPartido}&motivoArchivado=${motivoArchivado}`;

    fetch(url, {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                mostrarMensaje('Partido archivado correctamente', ".mensaje-exito");
                console.log('Partido archivado correctamente');
                if (btInscribe) {
                    btInscribe.innerText = "Archivado";
                    btInscribe.style.color = 'blue';
                    btInscribe.style.opacity = '0.8';
                    btInscribe.disabled = true;

                } else {
                    console.error("btInscribeMis no está en el DOM al intentar archivar.");
                }

            } else {
                throw new Error('Error al archivar el partido');
            }
        })
        .catch(error => {
            console.error('Error al archivar el partido. Inténtalo de nuevo', error);
        });
}


function cancelarInscripcion(idUsuario, idPartido, btInscribe) {

    mostrarDialogo("Seguro que quieres cancelar la inscripción a este partido?")
        .then(() => {
            // Si el usuario confirma la acción
            fetch(`/api/inscripciones/cancelar?idUsuario=${idUsuario}&idPartido=${idPartido}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {

                        mostrarMensaje('Inscripción cancelada correctamente', ".mensaje-exito");
                        btInscribe.innerText = "Inscripción Cancelada";
                        btInscribe.style.color = 'red';
                        btInscribe.style.opacity = '0.8';
                        btInscribe.disabled = true;

                    } else {
                        throw new Error('Error al cancelar la inscripción');
                    }
                })
                .catch(error => {
                    mostrarMensaje('Ha habido un error al cancelar la inscripción, inténtalo de nuevo', ".mensaje-error");
                });
        })
        .catch(() => {
            // Si el usuario cancela la acción
            console.log("Acción cancelada");
        });

}


