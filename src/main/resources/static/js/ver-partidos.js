document.addEventListener('DOMContentLoaded', function () {
    // Carga los partidos al cargarse el DOM
    cargarPartidos();
});

// Carga los partidos disponibles al hacer click en el enlace "Ver Partidos"
document.getElementById('enlace-ver').addEventListener('click', function () {
    cargarPartidos();
});

/**
 * Carga y muestra los partidos disponibles en el contenedor correspondiente.
 * Realiza una solicitud GET al backend para obtener los partidos y luego genera 
 * dinámicamente la interfaz de usuario con la información de cada partido.
 */
function cargarPartidos() {
    // Limpia los contenedores antes de cargar nuevos partidos
    limpiarContenedores();
    // Realiza una petición GET al backend para obtener los partidos
    fetch('/api/partido', { method: 'GET' })
        // Convierte la respuesta a formato JSON
        .then(response => response.json())
        .then(data => {
            // Si no hay datos o la lista está vacía, muestra el mensaje correspondiente
            if (!data || data.length === 0) {
                mostrarListaVacia(fichaPartidoContainer);
                return;
            }
            // Configura el contenedor principal para mostrar los partidos
            fichaPartidoContainer.classList.add('ver-partidos');
            //fichaPartidoContainer.classList.remove('lista-vacia');
            fichaPartidoContainer.innerHTML = `<h2>Partidos publicados</h2>`
            // Itera sobre cada partido y genera su representación en el DOM
            data.forEach(partido => {
                // Clona la plantilla del partido
                let nuevoPartido = fichaPartidoTemplate.cloneNode(true);
                nuevoPartido.style.display = 'grid';
                // Configura la información básica del partido
                nuevoPartido.querySelector('.partido-usuario').innerHTML = `<img src="img/ico_lapiz.png"/> ${partido.usuario.nombre}`;
                nuevoPartido.querySelector('.partido-ciudad').innerHTML = `<img src="img/ico_ubicacion.png"/> ${partido.ubicacion?.ciudad || 'Ciudad no disponible'}`;
                nuevoPartido.querySelector('.partido-tipo').innerHTML = partido.tipoPartido == "Singles" ?
                    `<img src="img/ico_singles.png"/> ${partido.tipoPartido}` : `<img src="img/ico_dobles2.png"/> ${partido.tipoPartido}`;
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
                // Muestra el nivel del partido como texto
                nuevoPartido.querySelector('.partido-nivel').textContent = partido.nivel;

                // Divide la fecha del partido en formato DD/MM/YYYY
                let [dia, mes, anio] = partido.fechaPartido.split('/');

                // Crea una nueva fecha en formato YYYY-MM-DD
                let fechaPartido = new Date(`${anio}-${mes}-${dia}`);

                // Verifica si la fecha es válida
                if (isNaN(fechaPartido.getTime())) {
                    nuevoPartido.querySelector('.partido-fecha').textContent = 'Fecha no disponible';
                } else {
                    // Convierte la fecha a un formato legible
                    let opcionesFecha = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
                    let fechaFormateada = fechaPartido.toLocaleDateString('es-ES', opcionesFecha);
                    nuevoPartido.querySelector('.partido-fecha').textContent = fechaFormateada;
                }
                // Configura la hora del partido
                nuevoPartido.querySelector('.partido-hora').innerHTML = `<img src="img/ico_reloj.png"/> ${partido.horaPartido}`;
                // Configura la ubicación del partido o muestra un mensaje por defecto
                nuevoPartido.querySelector('.partido-ubicacion').innerHTML = `<img src="img/ico_pista.png"> ${partido.ubicacion?.nombre || 'Ubicación no disponible'}`;
                // Genera las estrellas correspondientes al nivel del partido
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
                for (let i = 0; i < numeroEstrellas; i++) {
                    estrellasHTML += '<i class="fas fa-star"></i>';
                }
                nuevoPartido.querySelector('.partido-estrellas').innerHTML = estrellasHTML;
                // Evento de click del botón de detalles
                let btDetalles = nuevoPartido.querySelector('#boton-detalles');
                btDetalles.addEventListener('click', function () {
                    // Llamada a la función verDetalles indicando que no es organizador ni está inscrito
                    verDetalles(partido, false, false);
                });

                // Agrega el nuevo partido al contenedor
                fichaPartidoContainer.appendChild(nuevoPartido);
            });
        })
        .catch(error => console.error('Error al obtener los partidos:', error));
}

/**
 * Muestra los detalles completos de un partido, con la opción de inscribirse, cancelarlo o archivarlo según el contexto.
 * @param {Object} partido - El objeto que contiene los datos del partido.
 * @param {boolean} inscrito - Indica si el usuario está inscrito en el partido.
 * @param {boolean} organizador - Indica si el usuario es el organizador del partido.
 */
function verDetalles(partido, inscrito, organizador) {
    let detallesClone;

    // Selecciona el contenedor para la ficha según el contexto 
    let fichaPartidoContainer = inscrito || organizador ? document.getElementById('mis-partidos') : document.getElementById('ver-partidos');
    // Limpia el contenedor antes de mostrar los detalles del partido
    fichaPartidoContainer.innerHTML = '';

    // Clonar la ficha correspondiente según el contexto
    detallesClone = verDetallesFicha.cloneNode(true);
    detallesClone.style.display = 'flex';
    fichaPartidoContainer.appendChild(detallesClone);

    // Asigna los valores del partido a los elementos de la ficha de detalles
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

    // Selecciona el botón de inscripción o archivo
    btInscribe = detallesClone.querySelector('#bt-inscribir');

    // Configura el botón según el contexto
    if (organizador) {
        // Si es un partido propio y el usuario es el organizador, permite archivarlo
        btInscribe.innerText = 'Archivar';
        btInscribe.addEventListener('click', function () {
            mostrarConfirmacionArchivado()
                .then(motivo => {
                    // Si el usuario confirma, archiva el partido con el motivo proporcionado
                    archivarPartido(partido.idPartido, motivo, btInscribe);
                })
                .catch(() => {
                    // Si el usuario cancela la acción
                    console.log("Acción cancelada por el usuario.");
                });
        });
    } else if (inscrito) {
        // Si es un partido propio pero no es el organizador, permite cancelar la inscripción
        btInscribe.innerText = 'Cancelar Inscripción';
        btInscribe.addEventListener('click', function () {
            cancelarInscripcion(idUsuarioActivo, partido.idPartido, btInscribe);
        });
    } else {
        // Si no es un partido propio, permite inscribirse si hay vacantes
        getIdUsuario().then(idUsuario => {
            idUsuarioActivo = idUsuario;
            if (!comprobarInscripcion(idUsuarioActivo, partido.idPartido) && partido.vacantes === 0) {
                // Si el partido está completo, desactiva el botón y muestra un mensaje
                btInscribe.innerText = 'Completo';
                btInscribe.style.color = 'red';
            }
        }).catch(error => {
            console.error('Error al obtener el ID del usuario:', error);
        });

        // Acción de click en el botón de la ficha 
        btInscribe.addEventListener('click', function () {
            if (btInscribe.innerText === 'Completo') {
                // Si el partido está completo, muestra un mensaje al usuario
                mostrarMensaje("Este partido está completo", ".mensaje-error");
            } else if (btInscribe.innerText !== 'Inscrito') {
                // Si no está inscrito, muestra un diálogo de confirmación
                mostrarDialogo("Te apuntas al partido de " + partido.usuario.nombre + " ?")
                    .then(() => {
                        // Inscribe al usuario en el partido 
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
        inscrito || organizador ? cargarMisPartidos(idUsuarioActivo) : cargarPartidos();
    });
}

/**
 * Obtiene el ID del usuario actual haciendo una solicitud al backend.
 * Retorna una promesa que se resuelve con el ID del usuario si la solicitud es exitosa,
 * o se rechaza con un error si el ID no está disponible.
 * @returns {Promise<number>} Una promesa que resuelve el ID del usuario.
 */
function getIdUsuario() {
    // Retorna una promesa que intenta obtener el ID del usuario actual
    return new Promise((resolve, reject) => {
        $.ajax({
            // URL para obtener los datos del usuario del backend
            url: '/api/usuario/datosUsuario',
            type: 'GET',
            success: function (response) {
                // Si la respuesta contiene un ID de usuario válido, resuelve la promesa
                if (response && response.idUsuario) {
                    resolve(response.idUsuario);
                } else {
                    // Si no se encuentra el ID de usuario, rechaza la promesa con un error
                    reject(new Error("ID de usuario no disponible"));
                }
            },
            error: function (error) {
                // Si ocurre un error en la solicitud AJAX, lo registra en la consola y rechaza la promesa
                console.error('Error:', error);
                reject(error);
            }
        });
    });
}

/**
 * Verifica si un usuario está inscrito en un partido específico.
 * Realiza una solicitud GET al backend para comprobar la inscripción del usuario en el partido.
 * @param {number} idUsuario - El ID del usuario que se va a verificar.
 * @param {number} idPartido - El ID del partido en el que se va a verificar la inscripción.
 * @returns {boolean} Retorna true si el usuario está inscrito, false si no lo está.
 */
function comprobarInscripcion(idUsuario, idPartido) {

    // Envía una solicitud GET al backend para verificar si el usuario está inscrito en el partido
    fetch(`/api/inscripciones/verificar?idUsuario=${idUsuario}&idPartido=${idPartido}`)
        .then(res => {
            // Comprueba si la respuesta de la API es correcta
            if (!res.ok) {
                throw new Error('Error en la respuesta de la API');
            }
            return res.json(); // Convierte la respuesta a JSON
        })
        .then(data => {
            // Verifica si el usuario ya está inscrito
            if (data.inscrito) {
                // Si está inscrito, actualiza el botón para reflejar el estado
                btInscribe.innerText = 'Inscrito';
                btInscribe.style.color = 'var(--color-verde)';
                return true; // Devuelve true si está inscrito
            } else {
                return false; // Devuelve false si no está inscrito
            }
        })
        .catch(error => {
            console.error('Error al verificar la inscripción:', error);
        });
}

/**
 * Inscribe a un jugador en un partido.
 * Realiza una solicitud POST al backend para registrar la inscripción del jugador en el partido
 * y actualiza la interfaz de usuario según el resultado.
 * @param {number} idUsuario - El ID del usuario que se va a inscribir.
 * @param {number} idPartido - El ID del partido en el que el jugador se va a inscribir.
 * @param {boolean} organizador - Indica si el usuario es el organizador del partido.
 */
function inscribirJugador(idUsuario, idPartido, organizador) {
    // Obtiene la fecha actual en formato yyyy-mm-dd
    let fechaInscripcion = new Date().toISOString().split('T')[0];
    // Formatea la fecha a dd/mm/yyyy
    let fechaFormateada = formatearFecha(fechaInscripcion);
    // Realiza una solicitud POST a la API para inscribir al jugador en el partido
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
            console.log("Objeto inscripcion: " + res.json());
            // Si la respuesta es exitosa:
            // Comprueba si el usuario es el organizador
            // Si no lo es, muestra un mensaje de éxito y actualiza el botón
            if (!organizador) {
                mostrarMensaje("Jugador inscrito en el partido", ".mensaje-exito");
                btInscribe.innerText = 'Inscrito';
                btInscribe.style.color = 'var(--color-verde)';
            }
        } else {
            mostrarMensaje("No se ha podido inscribir al jugador, inténtalo de nuevo", ".mensaje-error");
            // return res.text().then(text => { throw new Error(text); });
        }
    }).catch(error => console.error('Error al inscribir al jugador en el partido:', error));

}


