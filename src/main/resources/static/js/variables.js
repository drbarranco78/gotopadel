let verDetallesFicha, fichaPartidoContainer, fichaMiPartidoContainer, fichaPartidoTemplate,
    fichaMiPartidoTemplate,  btInscribeVer, btInscribe, contenedorPrincipal, usuarioActivo, idUsuarioActivo, licencia, privacidad, ayuda;
document.addEventListener("DOMContentLoaded", () => {
    // Inicialización de variables
    verDetallesFicha = document.getElementById('ficha-detalles');
    // misDetallesFicha = document.getElementById('ficha-detalles-mis');
    fichaPartidoContainer = document.getElementById('ver-partidos');
    fichaMiPartidoContainer = document.getElementById('mis-partidos');
    fichaPartidoTemplate = document.getElementById('ficha-partido');
    fichaMiPartidoTemplate = document.getElementById('ficha-mis-partidos');
    // btInscribeMis = document.getElementById('bt-inscribir-mis');
    btInscribeVer = document.getElementById('bt-inscribir');
    contenedorPrincipal = document.getElementById('contenedor-principal');

    licencia='<p xmlns:cc="http://creativecommons.org/ns#" xmlns:dct="http://purl.org/dc/terms/" style="font-size: 12px; color: gray; border-top:1px solid black;"><span property="dct:title">GoToPádel</span> by <span property="cc:attributionName">Daniel Rodríguez Barranco</span> is licensed under <a href="https://creativecommons.org/licenses/by-nc-nd/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International<br><img style="height:22px!important;margin-top:10px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nd.svg?ref=chooser-v1" alt=""></a></p>';
    ayuda=`<p>El objetivo de la aplicación es facilitar la organización de partidos de Pádel, permitiendo a los usuarios publicar encuentros y unirse a los ya existentes.</p>
            <p>Para comenzar, el usuario debe registrarse completando el formulario accesible desde el botón <strong>Registro</strong>. 
            Una vez registrado, podrá iniciar sesión en su área personal mediante el botón <strong>Login</strong>, introduciendo su correo electrónico y contraseña.</p>
            <p>Una vez dentro, tendrá acceso a las siguientes secciones:</p>
            <ul>
                <li><i class="fa-solid fa-calendar-plus"></i> <strong class="m-left">Publicar un partido:</strong> Permite crear un nuevo partido indicando la modalidad (individual o dobles), el nivel mínimo requerido, 
                el número de vacantes, la fecha, la hora y la ubicación, que puede seleccionarse desde la lista desplegable.</li>

                <li><i class="m-20 fa-solid fa-magnifying-glass"></i> <strong>Buscar partidos:</strong> Aquí se listan los partidos publicados. Cada ficha muestra el nombre del organizador, 
                la modalidad, la localidad, la pista de juego, el nivel requerido (representado con estrellas), el número de vacantes disponibles y un botón para ver más detalles. 
                En la ficha ampliada, si hay vacantes y el nivel del usuario es adecuado, podrá inscribirse en el partido.</li>

                <li><i class="fa-solid fa-clipboard-list"></i> <strong class="m-left">Mis partidos:</strong> Muestra un resumen de los partidos en los que el usuario está involucrado, ya sea como organizador o inscrito. 
                Desde aquí, al pulsar el botón "Ver detalles", se accede a la ficha ampliada del partido, donde podrá archivar el encuentro si es el organizador o cancelar su inscripción si está inscrito.</li>

                <li><i class="fa-solid fa-user"></i> <strong>Mi cuenta:</strong> Pulsando el icono de usuario en la parte superior derecha, se despliega un menú con opciones para cerrar sesión o ver el perfil. 
                En el perfil se muestran detalles como el número de inscripción, correo electrónico, antigüedad, partidos publicados e inscripciones activas. 
                También se ofrece la opción de eliminar la cuenta, lo que cancelará y archivará automáticamente los partidos publicados y anulará las inscripciones activas.</li>
            </ul>`;
    privacidad = `    
            <p>En <strong>GoToPádel</strong>, nos tomamos muy en serio la privacidad de nuestros usuarios. Esta Política de Privacidad describe cómo recopilamos, utilizamos y protegemos la información personal que nos proporcionas al usar nuestra plataforma. Además, también detallamos cómo gestionamos el uso de recursos y servicios de terceros.</p>
            <h3>Recopilación de Datos</h3>
            <p>Al registrarte y usar nuestra aplicación, recopilamos ciertos datos personales, como tu nombre, correo electrónico y preferencias de usuario, los cuales son utilizados para proporcionarte una mejor experiencia. Cumplimos con la <strong>Ley General de Protección de Datos (LGPD)</strong>, asegurando que tu información está protegida y solo se utiliza para fines legítimos.</p>
            <h3>Uso de Recursos Externos</h3>
            <p>Nuestra plataforma utiliza varios recursos de terceros que son esenciales para su funcionamiento y presentación. A continuación, te informamos sobre los recursos que empleamos y sus respectivas licencias:</p>
            <ul>
                <li><strong>Iconos:</strong> Para los iconos utilizados en la interfaz de usuario, hemos empleado los iconos de <strong>Font Awesome</strong>. Estos iconos son de uso gratuito bajo la <strong>licencia Font Awesome Free</strong>. Algunos iconos son proporcionados por <strong>Freepik</strong> y requieren atribución adecuada. Puedes consultar más información en su <a href="https://www.freepik.com" target="_blank">sitio web</a>.</li>
                <li><strong>Imágenes:</strong> El logo de <strong>GoToPádel</strong> y las imágenes en nuestra plataforma han sido generadas por <strong>DALL-E</strong>, asegurando contenido original y creativo para nuestros usuarios.</li>
                <li><strong>Noticias:</strong> Las noticias en la página principal se obtienen de <a href="https://newsapi.org" target="_blank">NewsAPI.org</a>, un servicio que nos permite acceder a fuentes de noticias confiables y actualizadas.</li>
                <li><strong>Pistas de Pádel:</strong> Para ofrecer información sobre las ubicaciones de las pistas de pádel, utilizamos datos proporcionados por <a href="https://api.playtomic.io/" target="_blank">Playtomic API</a>, una plataforma que agrupa información sobre centros deportivos y pistas de pádel.</li>
            </ul>
            <h3>Uso de tus Datos</h3>
            <p>Utilizamos los datos personales que nos proporcionas para las siguientes finalidades:</p>
            <ul>
                <li>Gestión de tu cuenta y tus preferencias en la plataforma.</li>
                <li>Notificación de novedades y eventos relevantes.</li>
                <li>Mejora de la calidad de la experiencia del usuario.</li>
            </ul>
            <h3>Protección de Datos</h3>
            <p>Nos comprometemos a proteger tu información personal mediante el uso de tecnologías de seguridad avanzadas y prácticas para evitar accesos no autorizados. Sin embargo, aunque tomamos medidas adecuadas para proteger tu información, no podemos garantizar una seguridad absoluta en Internet.</p>
            <h3>Tus Derechos</h3>
            <p>Tienes derecho a acceder, corregir, actualizar y eliminar tu información personal en cualquier momento. Para hacerlo, puedes acceder a tu perfil de usuario y realizar los cambios correspondientes. Si tienes alguna pregunta o inquietud sobre el manejo de tus datos, puedes ponerte en contacto con nosotros a través de nuestros canales de soporte.</p>
            <h3>Cumplimiento con la LGPD</h3>
            <p>Nos aseguramos de cumplir con la <strong>Ley General de Protección de Datos (LGPD)</strong>, garantizando que tus datos sean tratados de forma legal, transparente y de acuerdo con tus derechos. Puedes consultar más detalles sobre cómo gestionamos tus datos en nuestra política de privacidad.</p>
            <h3>Modificaciones a esta Política</h3>
            <p>Nos reservamos el derecho de actualizar y modificar esta política en cualquier momento. Las actualizaciones serán reflejadas en esta página y, si es necesario, te informaremos de los cambios importantes.</p>
        `;
    // if (!verDetallesFicha || !fichaMiPartidoTemplate  || !fichaPartidoContainer || !fichaPartidoTemplate) {
    //     console.error("No se encontraron los elementos requeridos en el DOM.");
    // }
});