// Expresión regular para validar el formato del email
const patronEmail = /^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\.[a-zA-Z]{2,}$/;
// Expresión regular para validar la contraseña (mínimo 8 caracteres, una mayúscula, una minúscula y un número)
const patronPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;
// Expresión regular para validar el nombre (solo letras y espacios)
const patronNombre = /^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$/;

let emailLogin, passwordLogin, nombre, emailRegistro, contrasena1, contrasena2, nivel, fechaNac, genero, fechaActual, fechaFormateada;
let fechaNacimiento = document.getElementById('fechaNac');

// Calcula la fecha máxima permitida para registrarse (hace 18 años desde hoy)
let hoy = new Date();
hoy.setFullYear(hoy.getFullYear() - 18);
fechaNacimiento.max = hoy.toISOString().split("T")[0]; // Establece el valor máximo de fecha de nacimiento al calcular la fecha de hace 18 años

// Formulario de Login 
$(document).ready(function () {
    /**
     * Maneja el evento de clic para mostrar el formulario de login
     * Se oculta la sección de registro y muestra la sección de login con un efecto de desvanecimiento
     */
    $("#enlace-login, #nav-login a, #pie-formularios-registro").click(function (event) {
        efectoClick(this); // Aplica un efecto visual al hacer clic
        event.preventDefault(); // Previene el comportamiento por defecto del enlace
        if ($(".registro").css("display") !== "none") {
            $(".registro").css("display", "none"); // Oculta la sección de registro si está visible
        }
        $(".intro").fadeOut(1000, function () {
            $(".login").fadeIn(1000).css("display", "block");
        });
    });

    /**
     * Valida los campos del formulario de login
     * @returns {boolean} true si los campos son válidos, false si hay errores
     */
    function validarLogin() {
        emailLogin = $("#username").val();
        passwordLogin = $("#current-password").val();
        // Valida el email y la contraseña
        if (patronEmail.test(emailLogin) && patronPassword.test(passwordLogin)) {
            return true;
        } else {
            if (!patronEmail.test(emailLogin)) {
                // Cambiamos el texto del div de error antes de mostrarlo
                mostrarMensaje("Introduzca una dirección de correo válida", ".error-login");
            } else if (!patronPassword.test(passwordLogin)) {
                mostrarMensaje("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número", ".error-login");
            }
            return false;
        }
    }

    /**
     * Envia el formulario de login
     * @param {Event} event - El evento de envío del formulario
     */
    $("#formulario-login").submit(function (event) {
        event.preventDefault();        
        if (validarLogin()) {
            emailLogin = $("#username").val();
            passwordLogin = $("#current-password").val();
            $.ajax({
                url: '/api/usuario/login',
                type: 'POST',
                contentType: 'application/json',
                headers: {
                    'X-API-KEY': apiKey
                },
                data: JSON.stringify({ email: emailLogin, password: passwordLogin }),
                success: function (response) {                    
                    mostrarMensaje("Login correcto", ".exito-login");
                    // Redirige al área privada
                    setTimeout(function () {
                        window.location.href = emailLogin === "admin@email.com" ? 'admin' : 'private';
                    }, 2000);
                },
                error: function (error) {
                    console.error('Error:', error);
                    mostrarMensaje("Usuario o contraseña incorrectos", ".error-login");
                }
            });
        }
    });

    // Formulario de Registro 

    /**
     * Muestra la sección de registro al hacer clic en los enlaces correspondientes
     */
    $("#enlace-registro, #pie-formularios-login").click(function () {
        efectoClick(this); // Aplica un efecto visual al hacer clic
        $(".intro, .login").fadeOut(1000, function () {
            $(this).css("display", "none");
            $(".registro").fadeIn(1000); // Muestra la sección de registro
            $(".registro").css("display", "block");
        });
    });

    /**
     * Muestra u oculta las contraseñas en el formulario de registro
     * @param {number} numero - El número del campo de contraseña (1 o 2)
     */
    $("#mostrarPassword1").click(function () {
        mostrarContrasena(1);
    });
    $("#mostrarPassword2").click(function () {
        mostrarContrasena(2);
    });

    /**
     * Función para mostrar u ocultar las contraseñas
     * @param {number} numero - El número del campo de contraseña (1 o 2)
     */
    function mostrarContrasena(numero) {
        var contrasena = document.getElementById("contrasena" + numero);
        // Si la contraseña está oculta se muestra al pulsar el botón y viceversa
        if (contrasena.type == "password") {
            contrasena.type = "text";
        } else {
            contrasena.type = "password";
        }
    }

    /**
     * Función para validar el formulario de registro
     * @returns {boolean} true si todos los campos son válidos, false si hay errores
     */
    function validarRegistro() {
        fechaActual = new Date();
        // Obtener año, mes y día
        let anio = fechaActual.getFullYear();
        let mes = String(fechaActual.getMonth() + 1).padStart(2, '0');
        let dia = String(fechaActual.getDate()).padStart(2, '0');

        // Formatear la fecha como DD-MM-YYYY
        fechaFormateada = `${dia}/${mes}/${anio}`;
        nombre = $("#nombre").val();
        emailRegistro = $("#email-registro").val();
        contrasena1 = $("#contrasena1").val();
        contrasena2 = $("#contrasena2").val();
        nivel = $("#nivel").val();
        fechaNac = $("#fechaNac").val();
        let [anioNac, mesNac, diaNac] = fechaNac.split('-');
        fechaNac = `${diaNac}/${mesNac}/${anioNac}`;
        genero = $("input[name='genero']:checked").val();

        // Validación del nombre
        if (!patronNombre.test(nombre)) {
            mostrarMensaje("El campo Nombre no puede contener caracteres especiales ni espacios consecutivos, y debe tener una longitud entre 2 y 50", ".error-registro");
            return false;
        }

        // Validación del email
        if (!patronEmail.test(emailRegistro)) {
            mostrarMensaje("Introduzca una dirección de correo válida de 60 caracteres máximo", ".error-registro");
            return false;
        }

        // Validación de la contraseña
        if (!patronPassword.test(contrasena1)) {
            mostrarMensaje("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número", ".error-registro");
            return false;
        }

        // Validación de la confirmación de la contraseña
        if (contrasena1 !== contrasena2) {
            mostrarMensaje("La contraseña tiene que coincidir en los 2 campos", ".error-registro");
            return false;
        }

        return true;
    }

    /**
     * Envia el formulario de registro
     * @param {Event} event - El evento de envío del formulario
     */
    $("#formulario-registro").submit(function (event) {
        event.preventDefault();
        if (validarRegistro()) {
            $.ajax({
                url: '/api/usuario/registrarUsuario',
                type: 'POST',
                contentType: 'application/json',
                headers: {
                    'X-API-KEY': apiKey
                },
                data: JSON.stringify({
                    nombre: nombre,
                    email: emailRegistro,
                    password: contrasena1,
                    nivel: nivel,
                    fechaNac: fechaNac,
                    genero: genero,
                    fechaInscripcion: fechaFormateada
                }),
                success: function (response) {                   
                    mostrarMensaje("Usuario registrado con éxito", ".exito-registro");
                    // Redirige al área privada
                    setTimeout(function () {
                        window.location.href = 'private';
                    }, 2000);
                },
                error: function (xhr) {
                    console.error('Error:', xhr);
                    if (xhr.status === 409) {
                        mostrarMensaje("El email ya está registrado", ".error-registro");
                    } else {
                        mostrarMensaje("Error en el registro. Por favor, inténtalo de nuevo", ".error-registro");
                    }
                }
            });
        }
    });
});
