const patronEmail = /^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\.[a-zA-Z]{2,}$/;
const patronPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;
const patronNombre = /^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$/;

let emailLogin, passwordLogin, nombre, emailRegistro, contrasena1, contrasena2, nivel, fechaNac, genero, fechaActual, fechaFormateada;
let fechaNacimiento = document.getElementById('fechaNac');

// Calcula la fecha máxima permitida (hace 18 años desde hoy)
let hoy = new Date();
hoy.setFullYear(hoy.getFullYear() - 16);
fechaNacimiento.max = hoy.toISOString().split("T")[0];

// Formulario de Login 
$(document).ready(function () {
    $("#enlace-login, #nav-login a, #pie-formularios-registro").click(function (event) {
        efectoClick(this);
        event.preventDefault();
        if ($(".registro").css("display") !== "none") {
            $(".registro").css("display", "none");

        }

        $(".intro").fadeOut(1000, function () {
            $(".login").fadeIn(1000).css("display", "block");
        });
    });

    function validarLogin() {
        // let devolver = false;
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
    $("#formulario-login").submit(function (event) {
        event.preventDefault();
        if (validarLogin()) {
            emailLogin = $("#username").val();
            passwordLogin = $("#current-password").val();
            $.ajax({
                url: '/api/usuario/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ email: emailLogin, password: passwordLogin }),
                success: function (response) {
                    console.log('Success:', response);
                    mostrarMensaje("Login correcto", ".exito-login");
                    // Redirigir a area privada
                    setTimeout(function () {
                        window.location.href = emailLogin === "admin@email.com" ? 'administrador' : 'zonaPrivada';
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

    $("#enlace-registro, #pie-formularios-login").click(function () {
        efectoClick(this);
        $(".intro, .login").fadeOut(1000, function () {
            $(this).css("display", "none");
            $(".registro").fadeIn(1000);
            $(".registro").css("display", "block");
        });
    });
    $("#mostrarPassword1").click(function () {
        mostrarContrasena(1);

    });
    $("#mostrarPassword2").click(function () {
        mostrarContrasena(2);

    });

    function mostrarContrasena(numero) {
        var contrasena = document.getElementById("contrasena" + numero);
        // Cancelamos el evento de enviar el formulario al pulsar el botón
        // evento.preventDefault();
        // Si la contraseña está oculta se muestra al pulsar el botón y viceversa
        if (contrasena.type == "password") {
            contrasena.type = "text";
        } else {
            contrasena.type = "password";
        }
    }

    function validarRegistro() {
        fechaActual = new Date();

        // Obtener año, mes y día
        let anio = fechaActual.getFullYear();
        let mes = String(fechaActual.getMonth() + 1).padStart(2, '0'); // Los meses empiezan en 0
        let dia = String(fechaActual.getDate()).padStart(2, '0');

        // Formatear la fecha como YYYY-MM-DD
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

        if (!patronNombre.test(nombre)) {

            mostrarMensaje("El campo Nombre no puede contener caracteres especiales ni espacios consecutivos, y debe tener una longitud entre 2 y 50", ".error-registro");
            return false;
        }
        if (!patronEmail.test(emailRegistro)) {
            mostrarMensaje("Introduzca una dirección de correo válida de 60 caracteres máximo", ".error-registro");
            return false;
        }
        if (!patronPassword.test(contrasena1)) {
            mostrarMensaje("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número", ".error-registro");
            return false;
        }
        if (contrasena1 !== contrasena2) {
            mostrarMensaje("La contraseña tiene que coincidir en los 2 campos", ".error-registro");
            return false;
        }
        return true;
    }

    $("#formulario-registro").submit(function (event) {
        event.preventDefault();
        if (validarRegistro()) {
            $.ajax({
                url: '/api/usuario/registrarUsuario',
                type: 'POST',
                contentType: 'application/json',
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
                    console.log('Success:', response);
                    mostrarMensaje("Usuario registrado con éxito", ".exito-registro");
                    // Redirigir a área privada
                    setTimeout(function () {
                        window.location.href = 'zonaPrivada';
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