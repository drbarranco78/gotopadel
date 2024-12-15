
document.addEventListener('DOMContentLoaded', function () {
    cargarPartidos();  // Cargar los partidos solo cuando el DOM esté completamente cargado
});

document.getElementById('enlace-ver').addEventListener('click', function () {
    cargarPartidos();
}, { once: true });

function cargarPartidos() {
    limpiarContenedores();

    fetch('/api/partido', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            if (!data || data.length === 0) {
                mostrarListaVacia(fichaPartidoContainer);
                return;
            }
            fichaPartidoContainer.classList.add('ver-partidos');
            data.forEach(partido => {
                if (!fichaPartidoTemplate) {
                    console.warn("No se encontró el elemento #ficha-partido en el DOM, saltando la clonación.");

                    return;  // Saltar esta iteración si no se encuentra la plantilla
                }

                let nuevoPartido = fichaPartidoTemplate.cloneNode(true);
                nuevoPartido.style.display = 'grid';

                // Configura la información básica del partido
                nuevoPartido.querySelector('.partido-usuario').innerHTML = `<img src="img/ico_lapiz.png"/> ${partido.usuario.nombre}`;
                nuevoPartido.querySelector('.partido-ciudad').innerHTML = `<img src="img/ico_ubicacion.png"/> ${partido.ubicacion?.ciudad || 'Ciudad no disponible'}`;
                nuevoPartido.querySelector('.partido-tipo').innerHTML = partido.tipoPartido=="Singles"?
                `<img src="img/ico_singles.png"/> ${partido.tipoPartido}`:`<img src="img/ico_dobles2.png"/> ${partido.tipoPartido}`;

                let vacantesHTML = '';
                const totalJugadores = partido.tipoPartido === "Singles" ? 2 : 4;

                // Genera las imágenes de acuerdo con las vacantes
                for (let i = 0; i < totalJugadores; i++) {
                    if (i < totalJugadores - partido.vacantes) {
                        vacantesHTML += `<img src="img/ico_jugador.png"/>`;
                    } else {
                        vacantesHTML += `<img src="img/ico_raqueta.png"/>`;
                    }
                }

                // Añade el texto adecuado según el número de vacantes
                if (partido.vacantes === 0) {
                    vacantesHTML += " Completo";
                    nuevoPartido.querySelector('.partido-vacantes').style.color = "var(--color-rojo)";
                } else {
                    vacantesHTML += ` ${partido.vacantes} Vacante${partido.vacantes > 1 ? 's' : ''}`;
                    nuevoPartido.querySelector('.partido-vacantes').style.color = "var(--color-verde)";
                }
                
                nuevoPartido.querySelector('.partido-vacantes').innerHTML = vacantesHTML;
                nuevoPartido.querySelector('.partido-nivel').textContent = partido.nivel;

                // Convertir la fecha a un formato legible
                // Dividir la fecha en formato DD/MM/YYYY
                let [dia, mes, anio] = partido.fechaPartido.split('/');

                // Crear una nueva fecha en formato YYYY-MM-DD
                let fechaPartido = new Date(`${anio}-${mes}-${dia}`);

                // Verificar si la fecha es válida
                if (isNaN(fechaPartido.getTime())) {
                    nuevoPartido.querySelector('.partido-fecha').textContent = 'Fecha no disponible';
                } else {
                    let opcionesFecha = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
                    let fechaFormateada = fechaPartido.toLocaleDateString('es-ES', opcionesFecha);
                    nuevoPartido.querySelector('.partido-fecha').textContent = fechaFormateada;
                }

                nuevoPartido.querySelector('.partido-hora').innerHTML = `<img src="img/ico_reloj.png"/> ${partido.horaPartido}`;
                nuevoPartido.querySelector('.partido-ubicacion').innerHTML = `<img src="img/ico_pista.png"> ${partido.ubicacion?.nombre || 'Ubicación no disponible'}`;

                const nivelesEstrellas = {
                    'Principiante': 1,
                    'Intermedio': 2,
                    'Avanzado': 3,
                    'Experto': 4,
                    'Profesional': 5
                };

                let nivel = partido.nivel;

                let numeroEstrellas = nivelesEstrellas[nivel] || 0; // Usa 0 si el nivel no está definido
                let estrellasHTML = '';

                // Genera las estrellas de acuerdo con el nivel
                for (let i = 0; i < numeroEstrellas; i++) {
                    estrellasHTML += '<i class="fas fa-star"></i>';
                }

                // Añadir evento al botón de detalles
                let btDetalles = nuevoPartido.querySelector('#boton-detalles');
                btDetalles.addEventListener('click', function () {

                    verDetalles(partido, false, false); // Llamada a la función verDetalles

                });

                nuevoPartido.querySelector('.partido-estrellas').innerHTML = estrellasHTML;
                fichaPartidoContainer.appendChild(nuevoPartido);
            });
        })
        .catch(error => console.error('Error al obtener los partidos:', error));
}

function verDetalles(partido, miPartido, organizador) {
    let detallesClone;
    let fichaPartidoContainer = miPartido ? document.getElementById('mis-partidos') : document.getElementById('ver-partidos');

    if (!fichaPartidoContainer) {
        console.error("No se encontró el contenedor de destino");
        return;
    }

    fichaPartidoContainer.innerHTML = '';

    // Clonar la ficha correspondiente según el contexto
    detallesClone = miPartido ? misDetallesFicha.cloneNode(true) : verDetallesFicha.cloneNode(true);
    detallesClone.style.display = 'flex';
    fichaPartidoContainer.appendChild(detallesClone);

    // Asignar valores del partido
    detallesClone.querySelector('.numero-partido').innerText = partido.idPartido;
    detallesClone.querySelector('.organizador-partido').innerText = partido.usuario.nombre;
    detallesClone.querySelector('.fecha-publicacion').innerText = partido.fechaPublicacion;
    detallesClone.querySelector('.fecha-partido').innerText = partido.fechaPartido;
    detallesClone.querySelector('.hora-partido').innerText = partido.horaPartido;
    detallesClone.querySelector('.lugar-partido').innerText = partido.ubicacion.nombre;
    detallesClone.querySelector('.ciudad-partido').innerText = partido.ubicacion.ciudad;
    detallesClone.querySelector('.tipo-partido').innerText = partido.tipoPartido;

    let inscritos = partido.tipoPartido === "Singles" ? 2 - partido.vacantes : 4 - partido.vacantes;
    detallesClone.querySelector('.jugadores-inscritos').innerText = inscritos;
    detallesClone.querySelector('.vacantes-partido').innerText = partido.vacantes;
    detallesClone.querySelector('.nivel-partido').innerText = partido.nivel;
    detallesClone.querySelector('.comentarios-partido').innerText = partido.comentarios || "No hay comentarios";

    // Seleccionar el botón de inscripción/archivo adecuado
    btInscribe = detallesClone.querySelector(miPartido ? '#bt-inscribir-mis' : '#bt-inscribir');

    // Configurar el botón según el contexto
    if (miPartido && organizador) {
        btInscribe.innerText = 'Archivar';
        btInscribe.addEventListener('click', function () {
            mostrarConfirmacionArchivado()
                .then(motivo => {
                    archivarPartido(partido.idPartido, motivo, btInscribe);
                })
                .catch(() => {
                    console.log("Acción cancelada por el usuario.");
                });
        });
    } else if (miPartido && !organizador) {
        btInscribe.innerText = 'Cancelar Inscripción';
        btInscribe.addEventListener('click', function () {
            cancelarInscripcion(idUsuarioActivo, partido.idPartido, btInscribe);
        });
    } else {
        // Si no es mi partido, comprobar inscripción e inscribir si no está inscrito
        getIdUsuario().then(idUsuario => {
            idUsuarioActivo = idUsuario;
            if (!comprobarInscripcion(idUsuarioActivo, partido.idPartido) && partido.vacantes === 0) {
                btInscribe.innerText = 'Completo';
                btInscribe.style.color = 'red';

            }
        }).catch(error => {
            console.error('Error al obtener el ID del usuario:', error);
        });

        btInscribe.addEventListener('click', function () {
            if (btInscribe.innerText === 'Completo') {

                mostrarMensaje("Este partido está completo", ".mensaje-error");
            } else if (btInscribe.innerText !== 'Inscrito') {
                mostrarDialogo("Te apuntas al partido de " + partido.usuario.nombre + " ?")
                    .then(() => {
                        inscribirJugador(idUsuarioActivo, partido.idPartido, false);
                    })
                    .catch(() => {
                        // Si el usuario cancela la acción
                        console.log("El usuario canceló la inscripción");
                    });

            } else {
                mostrarMensaje("Ya estás inscrito en el partido. Si quieres cancelarlo, ve a 'Mis Partidos'.", ".mensaje-exito");
            }
        });

    }

    // Evento de cierre de la ficha
    detallesClone.querySelector('.bt-cerrar-ficha').addEventListener('click', function () {
        miPartido ? cargarMisPartidos(idUsuarioActivo) : cargarPartidos();
    });
}

function getIdUsuario() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/api/usuario/datosUsuario',
            type: 'GET',
            success: function (response) {
                if (response && response.idUsuario) {
                    resolve(response.idUsuario);
                } else {
                    reject(new Error("ID de usuario no disponible"));
                }
            },
            error: function (error) {
                console.error('Error:', error);
                reject(error);
            }
        });
    });
}
function comprobarInscripcion(idUsuario, idPartido) {
    fetch(`/api/inscripciones/verificar?idUsuario=${idUsuario}&idPartido=${idPartido}`)
        .then(res => {
            if (!res.ok) {
                throw new Error('Error en la respuesta de la API');
            }
            return res.json();
        })
        .then(data => {
            if (data.inscrito) {
                // Al cargar la ficha, actualizamos el botón si ya está inscrito                
                btInscribe.innerText = 'Inscrito';
                btInscribe.style.color = 'var(--color-verde)';
                return true;

            } else {
                return false;

            }
        })
        .catch(error => console.error('Error al verificar la inscripción:', error));
}

function inscribirJugador(idUsuario, idPartido, organizador) {
    let fechaInscripcion = new Date().toISOString().split('T')[0]; // Obtener la fecha actual en formato yyyy-mm-dd
    let fechaFormateada = formatearFecha(fechaInscripcion); // Formatear la fecha a dd/mm/yyyy

    console.log('Datos de inscripción:', { idUsuario, idPartido, fechaFormateada });  // Verifica los datos antes de enviar

    fetch("/api/inscripciones", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            usuario: { idUsuario },  // Enviamos un objeto para el usuario
            partido: { idPartido },  // Enviamos un objeto para el partido
            fechaIns: fechaFormateada // Añadimos la fecha de inscripción
        })
    }).then(res => {
        if (res.ok) {
            if (!organizador) {

                mostrarMensaje("Jugador inscrito en el partido", ".mensaje-exito");
                btInscribe.innerText = 'Inscrito';
                btInscribe.style.color = 'var(--color-verde)';
                //cambiarBotonInscribe();
            }

        } else {
            return res.text().then(text => { throw new Error(text); });
        }
    }).catch(error => console.error('Error al inscribir al jugador en el partido:', error));

}



// function cambiarBotonInscribe() {
//
//     btInscribe.innerText = 'Inscrito';
//     btInscribe.style.color = '#69f88d';
// }


