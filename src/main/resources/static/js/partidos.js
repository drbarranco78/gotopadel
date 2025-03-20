document.addEventListener('DOMContentLoaded', function () {
    // Obtiene el ID del usuario actual
    getUsuarioActivo()
        .then(response => {
            usuarioActivo = response;
            // Almacena el ID del usuario activo
            idUsuarioActivo = response.idUsuario;
            // Carga los partidos del usuario

        })
        .catch(error => console.error('Error al obtener el id del usuario:', error));
    // Carga los partidos al cargarse el DOM
    cargarPartidos();
    // Añade un evento al hacer clic en el enlace "Mis Partidos"
    $("#enlace-mispartidos").click(function () {
        cargarMisPartidos(idUsuarioActivo);

    });
});

// Carga los partidos disponibles al hacer click en el enlace "Ver Partidos"
document.getElementById('enlace-ver').addEventListener('click', function () {
    cargarPartidos();
});

// Mapeamos los niveles de juego disponibles en una constante 
const nivelesEstrellas = {
    'Principiante': 1,
    'Intermedio': 2,
    'Avanzado': 3,
    'Experto': 4,
    'Profesional': 5
};

/**
 * Carga y muestra los partidos disponibles en el contenedor correspondiente.
 * Realiza una solicitud GET al backend para obtener los partidos y luego genera 
 * dinámicamente la interfaz de usuario con la información de cada partido.
 */
function cargarPartidos() {
    // Limpia los contenedores antes de cargar nuevos partidos
    limpiarContenedores();
    // Realiza una petición GET al backend para obtener los partidos
    fetch('/api/partido', {
        method: 'GET',
        headers: {
            'X-API-KEY': apiKey // Asegúrate de que apiKey esté definida
        }
    })
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

                let nivel = partido.nivel;
                let numeroEstrellas = nivelesEstrellas[nivel] || 0;
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
                    archivarPartido(partido, motivo, btInscribe);

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
            cancelarInscripcion(idUsuarioActivo, partido.idPartido, partido, btInscribe);
        });
    } else {
        // Si no es un partido propio, permite inscribirse si hay vacantes

        if (!comprobarInscripcion(idUsuarioActivo, partido.idPartido) && partido.vacantes === 0) {
            // Si el partido está completo, desactiva el botón y muestra un mensaje
            btInscribe.innerText = 'Completo';
            btInscribe.style.color = 'red';
        }

        // Acción de click en el botón de la ficha 
        btInscribe.addEventListener('click', function () {
            if (btInscribe.innerText === 'Completo') {
                // Si el partido está completo, muestra un mensaje al usuario
                mostrarMensaje("Este partido está completo", ".mensaje-error");
            } else if (btInscribe.innerText !== 'Inscrito') {
                if (nivelesEstrellas[usuario.nivel] >= nivelesEstrellas[partido.nivel]) {
                    // Si el nivel es adecuado, muestra el diálogo de confirmación
                    mostrarDialogo("Te apuntas al partido de " + partido.usuario.nombre + " ?")
                        .then(() => {
                            // Inscribe al usuario en el partido 
                            inscribirJugador(idUsuarioActivo, partido, false);
                        })
                        .catch(() => {
                            // Si el usuario cancela la acción, registra un mensaje en la consola
                            console.log("Acción cancelada");
                        });
                } else {
                    // Si el nivel del usuario es inferior, muestra un mensaje de error
                    mostrarMensaje("No puedes inscribirte. Tu nivel es inferior al requerido.", ".mensaje-error");
                }

            } else {
                mostrarMensaje("Ya estás inscrito en el partido. Si quieres cancelarlo, ve a 'Mis Partidos'.", ".mensaje-exito");
            }
        });
    }
    // Evento de cierre de la ficha
    detallesClone.querySelector('.bt-cerrar-ficha').addEventListener('click', function () {
        inscrito || organizador ? cargarMisPartidos(idUsuarioActivo) : cargarPartidos();
    });

    /**
     * Evento de clic para mostrar los jugadores inscritos en un partido.
     * 
     * - Limpia el contenedor de fichas de jugadores.
     * - Muestra el título con la información del partido.
     * - Obtiene la lista de inscritos y genera las fichas correspondientes.
     * - Si el usuario activo es el organizador, permite cancelar inscripciones.
     * 
     * @event click
     */
    $(document).off('click', '#ver-jugadores-inscritos').on('click', '#ver-jugadores-inscritos', function () {

        // Vacía solo el contenedor de las fichas
        $('#div-jugadores-inscritos .fichas-container').empty();

        // Actualiza el título con el ID del partido
        $('#div-jugadores-inscritos h3').text("Jugadores inscritos en el partido " + partido.idPartido + " organizado por " + partido.usuario.nombre);

        obtenerInscritos(partido.idPartido, true)
            .then((usuariosInscritos) => {

                usuariosInscritos.forEach(usuario => {

                    let fichaInscrito = $('.ficha-usuario').clone(true, true).removeAttr("id");
                    // Vacía y oculta los botones dentro de la ficha clonada
                    let botonesFicha = fichaInscrito.find('.botones-ficha-usuario');
                    botonesFicha.empty().hide();
                    fichaInscrito.find('span').text("");
                    cargarDatosUsuario(usuario, fichaInscrito);
                    fichaInscrito.removeClass("ficha-usuario").addClass("ficha-usuario-inscrito");

                    // Lógica para mostrar "Organizador", "Inscrito" o "Cancelar inscripción"
                    if (idUsuarioActivo === partido.usuario.idUsuario) {
                        // Si el usuario activo es el organizador
                        if (usuario.idUsuario === idUsuarioActivo) {
                            // Ficha del organizador
                            let linkOrganizador = $('<p>')
                                .text("Organizador")
                                .addClass("ficha-inscrito-organizador")
                                .attr("data-id-usuario", usuario.idUsuario);
                            fichaInscrito.append(linkOrganizador);
                        } else {
                            // Ficha de un inscrito (no organizador)
                            let linkCancelar = $('<p>')
                                .text("Cancelar inscripción")
                                .addClass("ficha-inscrito-cancelar")
                                .attr("data-id-usuario", usuario.idUsuario)
                                .click(function () {
                                    cancelarInscripcion(usuario.idUsuario, partido.idPartido, partido, null, true);
                                });
                            fichaInscrito.append(linkCancelar);
                        }
                    } else {
                        // Si el usuario activo no es el organizador
                        if (usuario.idUsuario === partido.usuario.idUsuario) {
                            // Ficha del organizador
                            let linkOrganizador = $('<p>')
                                .text("Organizador")
                                .addClass("ficha-inscrito-organizador")
                                .attr("data-id-usuario", usuario.idUsuario);
                            fichaInscrito.append(linkOrganizador);
                        } else {
                            // Ficha de un inscrito normal
                            let linkInscrito = $('<p>')
                                .text("Inscrito")
                                .addClass("ficha-inscrito-organizador")
                                .attr("data-id-usuario", usuario.idUsuario);
                            fichaInscrito.append(linkInscrito);
                        }
                    }

                    fichaInscrito.show();
                    // Añade las fichas al contenedor específico
                    $('#div-jugadores-inscritos .fichas-container').append(fichaInscrito);
                });

                // Muestra el contenedor principal con display: grid
                $('#div-jugadores-inscritos').show().css("display", "grid");
            })
            .catch((error) => {
                mostrarMensaje('Ha habido un error al obtener los usuarios inscritos', ".mensaje-error");
            });
    });

    /**
     * Cierra el contenedor de jugadores inscritos si se hace click fuera de él
     * @param {Object} event - El evento de clic
     */
    $(document).click(function (event) {
        let contenedor = $('#div-jugadores-inscritos');
        if (contenedor.css("display") !== "none" && !$(event.target).closest(contenedor).length) {
            contenedor.slideUp(300);
        }
    });

    $(document).on('click', '.cerrar-inscritos', function () {
        $('#div-jugadores-inscritos').hide();
    });
}

/**
 * Obtiene el ID del usuario actual haciendo una solicitud al backend.
 * Retorna una promesa que se resuelve con el ID del usuario si la solicitud es exitosa,
 * o se rechaza con un error si el ID no está disponible.
 * @returns {Promise<number>} Una promesa que resuelve el ID del usuario.
 */
function getUsuarioActivo() {
    // Retorna una promesa que intenta obtener el ID del usuario actual
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/api/usuario/datosUsuario',
            type: 'GET',
            headers: {
                'X-API-KEY': apiKey
            },
            success: function (response) {
                // Si la respuesta contiene un ID de usuario válido, resuelve la promesa
                if (response && response.idUsuario) {
                    resolve(response);
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
    fetch(`/api/inscripciones/verificar?idUsuario=${idUsuario}&idPartido=${idPartido}`, {
        headers: {
            'X-API-KEY': apiKey
        }
    })
        .then(res => {
            if (!res.ok) {
                throw new Error('Error en la respuesta de la API');
            }
            return res.json();
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
function inscribirJugador(idUsuario, partido, organizador) {
    let idPartido = partido.idPartido;
    // Obtiene la fecha actual en formato yyyy-mm-dd
    let fechaInscripcion = new Date().toISOString().split('T')[0];
    // Formatea la fecha a dd/mm/yyyy
    let fechaFormateada = formatearFecha(fechaInscripcion);
    // Realiza una solicitud POST a la API para inscribir al jugador en el partido
    fetch("/api/inscripciones", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-API-KEY': apiKey
        },
        body: JSON.stringify({
            usuario: { idUsuario },
            partido: { idPartido },
            fechaIns: fechaFormateada
        })

    }).then(res => {
        if (res.ok) {
            // Si la respuesta es exitosa:
            // Comprueba si el usuario es el organizador
            // Si no lo es, envía la notificación al organizador y actualiza el botón
            if (!organizador) {
                let mensajeNotificacion = `<strong>${usuarioActivo.nombre || 'Usuario desconocido'}</strong> se ha inscrito en tu partido
                    <i class="ver-perfil-inscrito fas fa-eye" data-usuario='${JSON.stringify(usuarioActivo || {})}'></i>
                    <p>Email: ${usuarioActivo.email || 'No disponible'}</p>
                    <p data-partido='${JSON.stringify({ idPartido: partido.idPartido })}'>
                        Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'} 
                        el ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}
                    </p>`;

                mostrarMensaje("Jugador inscrito en el partido", ".mensaje-exito");
                btInscribe.innerText = 'Inscrito';
                btInscribe.style.color = 'var(--color-verde)';
                crearNotificacion(idUsuarioActivo, partido.usuario.idUsuario, mensajeNotificacion, "inscripcion");
                cargarPartidos();
            }
        } else {
            mostrarMensaje("No se ha podido inscribir al jugador, inténtalo de nuevo", ".mensaje-error");

        }
    }).catch(error => console.error('Error al inscribir al jugador en el partido:', error));

}

/**
 * Crea una notificación enviando una solicitud POST al servidor.
 * 
 * @param {number} idEmisor - ID del usuario que envía la notificación.
 * @param {number} idReceptor - ID del usuario que recibe la notificación.
 * @param {string} mensaje - Contenido de la notificación.
 * @param {string} tipo - Tipo de notificación (ej. "inscripcion", "cancelacion").
 * @returns {Promise<void>} - Retorna una promesa que se resuelve si la notificación se crea correctamente.
 */
function crearNotificacion(idEmisor, idReceptor, mensaje, tipo) {

    return fetch("/api/notificaciones/crear", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-API-KEY": apiKey
        },
        body: new URLSearchParams({
            idEmisor: idEmisor,
            idReceptor: idReceptor,
            mensaje: mensaje,
            tipo: tipo
        })
    })
        .then(response => {
            if (response.ok) {                
                return;
            } else {
                return response.text().then(text => {
                    throw new Error(`Error al generar la notificación: ${response.status} ${response.statusText} - ${text}`);
                });
            }
        })
        .catch(error => {
            console.error("Error al generar la notificación:", error);
            throw error;
        });
}

/**
 * Carga los partidos del usuario y los muestra en la interfaz.
 * Si no hay partidos, se muestra una lista vacía.
 * @param {number} idUsuario - El ID del usuario para obtener sus partidos.
 */
function cargarMisPartidos(idUsuario) {
    // Limpia el contenedor y establece el título de la sección
    fichaMiPartidoContainer.innerHTML = `<h2>Mis Partidos</h2>`;

    // Solicita los partidos del usuario al backend
    fetch(`/api/partido/misPartidos?usuario=${idUsuario}`, {
        headers: {
            'X-API-KEY': apiKey
        }
    })
        .then(res => res.json())
        .then(partidos => {
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
function archivarPartido(partido, motivoArchivado, btInscribe) {

    obtenerInscritos(partido.idPartido)
        .then((usuariosInscritos) => {

            let mensajeNotificacion = `Un partido donde estabas inscrito ha sido <strong>cancelado</strong> por su organizador             
            <p>Organizador: ${usuarioActivo.nombre || 'Usuario desconocido'} (${usuarioActivo.email || 'No disponible'})</p>
            <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'}</p>
            <p>Fecha original: ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>`;
            if (usuariosInscritos.length > 0 && motivoArchivado === 2) {
                let promesas = usuariosInscritos
                    .filter(inscrito => idUsuarioActivo !== inscrito.idUsuario) // Evita que el creador se notifique a sí mismo
                    .map(inscrito => crearNotificacion(idUsuarioActivo, inscrito.idUsuario, mensajeNotificacion, "cancelacion"));

                // Esperar a que todas las notificaciones se envíen antes de archivar el partido
                return Promise.all(promesas).then(() => usuariosInscritos);
            } else {
                return Promise.resolve(usuariosInscritos);
            }
        })
        .then(() => {
            const url = `/api/archivo?idPartido=${partido.idPartido}&motivoArchivado=${motivoArchivado}`;
            return fetch(url, {
                method: 'POST',
                headers: {
                    'X-API-KEY': apiKey
                }
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('No se ha podido archivar el partido');
            }

            // Si el archivado es exitoso
            mostrarMensaje('Partido archivado correctamente', ".mensaje-exito");
            btInscribe.innerText = "Archivado";
            btInscribe.style.color = 'var(--color-resaltado)';
            btInscribe.disabled = true;

            $('#ver-jugadores-inscritos').hide();
        })
        .catch((error) => {
            console.error('Error en el proceso de archivado:', error);
            mostrarMensaje('No se ha podido archivar. Inténtalo de nuevo', ".mensaje-error");
        });
}

/**
 * Obtiene la lista de usuarios inscritos en un partido.
 * 
 * @param {number} idPartido - ID del partido del que se desean obtener los inscritos.
 * @returns {Promise<Array>} - Promesa con la lista de usuarios inscritos.
 */
function obtenerInscritos(idPartido) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/api/inscripciones/partido/${idPartido}/usuarios`,
            type: 'GET',
            headers: {
                'X-API-KEY': apiKey
            },
            success: function (usuarios) {
                resolve(usuarios);
            },
            error: function (error) {
                console.error("Error al obtener los usuarios inscritos:", error.responseText);
                reject(error);
            }
        });
    });
}


/**
 * Cancela la inscripción de un usuario en un partido después de confirmación.
 * @param {number} idUsuario - El ID del usuario cuya inscripción se cancelará.
 * @param {number} idPartido - El ID del partido en el cual el usuario está inscrito.
 * @param {HTMLElement} btInscribe - El botón que se actualizará tras la cancelación.
 */
function cancelarInscripcion(idUsuario, idPartido, partido = null, btInscribe = null, rechazo = false) {
    // Muestra un diálogo de confirmación para la cancelación
    mostrarDialogo("¿Seguro que quieres cancelar la inscripción a este partido?")
        .then(() => {
            // Si el usuario confirma la acción, realiza la solicitud DELETE
            fetch(`/api/inscripciones/cancelar?idUsuario=${idUsuario}&idPartido=${idPartido}`, {
                method: 'DELETE',
                headers: {
                    'X-API-KEY': apiKey
                }
            })
                .then(response => {
                    if (response.status === 200) {
                        mostrarMensaje('Inscripción cancelada correctamente', ".mensaje-exito");
                        // Se crea la notificación correspondiente según quién haya cancelado la inscripción
                        if (partido && !rechazo) {
                            let mensajeNotificacion = `<strong>${usuarioActivo.nombre || 'Usuario desconocido'}</strong> ha cancelado su inscripción a tu partido de 
                                <strong>${partido.tipoPartido || 'desconocido'}</strong>.
                                <p>Usuario: ${usuarioActivo.nombre || 'Usuario desconocido'} (${usuarioActivo.email || 'No disponible'})</p>
                                <p>Organizador: ${partido.usuario.nombre || 'Organizador desconocido'} (${partido.usuario.email || 'No disponible'})</p>
                                <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'}</p>
                                <p>Fecha original: ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>`;
                            crearNotificacion(idUsuario, partido.usuario.idUsuario, mensajeNotificacion, "cancelado-usuario");
                        }
                        // Si ha cancelado el propio jugador inscrito se actualiza el botón de inscripción
                        if (btInscribe) {
                            btInscribe.innerText = "Inscripción Cancelada";
                            btInscribe.style.color = 'var(--color-rojo)';
                            btInscribe.disabled = true;
                        }
                        if (rechazo) {
                            let mensajeNotificacion = `
                                Tu inscripción al partido <strong>${partido.tipoPartido || 'desconocido'}</strong> 
                                ha sido rechazada por el organizador.
                                <p>Organizador: ${partido.usuario.nombre || 'Usuario desconocido'} (${partido.usuario.email || 'No disponible'})</p>
                                <p>Partido: ${partido.tipoPartido || 'desconocido'} en ${partido.ubicacion?.nombre || 'ubicación desconocida'}</p>
                                <p>Fecha original: ${partido.fechaPartido || 'fecha desconocida'} a las ${partido.horaPartido || 'hora desconocida'}</p>
                            `;
                            crearNotificacion(partido.usuario.idUsuario, idUsuario, mensajeNotificacion, "rechazo");

                        }
                        $('#enlace-ver').click();
                        cargarPartidos();

                    } else if (response.status === 204) {
                        // Si el usuario inscrito ya canceló la inscripción                        
                        mostrarMensaje('La inscripción ya ha sido cancelada', ".mensaje-exito");
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


