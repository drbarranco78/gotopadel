const patronEmail=/^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\.[a-zA-Z]{2,}$/;
const patronPassword=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;
let emailLogin, passwordLogin;

$(document).ready(function() {
    $("#enlace-login").click(function() {
        $(".intro").fadeOut(1000, function () {
            $(this).css("display","none");
            $(".login").fadeIn(1000);
            $(".login").css("display","block");
        });
    });
    function validarLogin() {
            // let devolver = false;
            emailLogin = $("#email-login").val();
            passwordLogin = $("#contrasena-login").val();

            // Valida el email y la contraseña
            if (patronEmail.test(emailLogin) && patronPassword.test(passwordLogin)) {
                return true;
            } else {
                if (!patronEmail.test(emailLogin)) {
                    // Cambiamos el texto del div de error antes de mostrarlo
                    $(".error-login").html("<h3>Introduzca una dirección de correo válida</h3>");
                } else if (!patronPassword.test(passwordLogin)) {
                    $(".error-login").html("<h3>La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número</h3>");
                }

                // Asegurarse de que el div esté visible y se actualice su contenido
                $(".error-login").fadeIn(500).delay(3000).fadeOut(500);
                return false;
            }



        }
        $("#formulario-login").submit(function(event) {
            event.preventDefault();
            if (validarLogin()) {
                emailLogin = $("#email-login").val();
                passwordLogin = $("#contrasena-login").val();
                $.ajax({
                    url: '/api/login',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({email: emailLogin, password: passwordLogin}),
                    success: function(response) {
                        console.log('Success:', response);
                        // Redirigir a area privada
                        window.location.href = '/zonaPrivada';
                    },
                    error: function(error) {
                        console.error('Error:', error);
                        $(".error-login").html("<h3>Usuario o contraseña incorrectos</h3>");
                        $(".error-login").fadeIn(500).delay(3000).fadeOut(500); // Mostrar el error durante 3 segundos
                        //alert("Error en el inicio de sesión. Por favor, inténtalo de nuevo.");
                    }
                });
            }
        });

});

