//const patronEmail=/^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\.[a-zA-Z]{2,}$/;
//const patronPassword=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;
const patronNombre = /^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$/;

let nombre, emailRegistro, contrasena1, contrasena2, nivel, fechaNac, genero, fechaActual, fechaFormateada;

$(document).ready(function() {
    $("#enlace-registro").click(function() {
            $(".intro").fadeOut(1000, function () {
                $(this).css("display","none");
                $(".registro").fadeIn(1000);
                $(".registro").css("display","block");
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
            const anio = fechaActual.getFullYear();
            const mes = String(fechaActual.getMonth() + 1).padStart(2, '0'); // Los meses empiezan en 0
            const dia = String(fechaActual.getDate()).padStart(2, '0');

        // Formatear la fecha como YYYY-MM-DD
                fechaFormateada = `${dia}-${mes}-${anio}`;
                nombre=$("#nombre").val();
                emailRegistro=$("#email-registro").val();
                contrasena1=$("#contrasena1").val();
                contrasena2=$("#contrasena2").val();
                nivel=$("#nivel").val();
                fechaNac=$("#fechaNac").val();
                genero = $("input[name='genero']:checked").val();

                if (!patronNombre.test(nombre)) {
                    mensajeError("El campo Nombre no puede contener caracteres especiales ni espacios consecutivos, y debe tener una longitud entre 2 y 50 ");
                    return false;
                }
                if (!patronEmail.test(emailRegistro)) {
                    mensajeError("Introduzca una dirección de correo válida de 60 caracteres máximo");
                    return false;
                }
                if (!patronPassword.test(contrasena1)) {
                    mensajeError("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número");
                    return false;
                }
                if (contrasena1 !== contrasena2) {
                    mensajeError("La contraseña tiene que coincidir en los 2 campos");
                    return false;
                }
                return true;
            }
            function mensajeError(mensaje){
                $(".error-registro").html(`<h3>${mensaje}</h3>`).fadeIn(500).delay(3000).fadeOut(500);
            }

            $("#formulario-registro").submit(function(event) {
                event.preventDefault();
                if (validarRegistro()) {
                    $.ajax({
                        url: '/api/registro',
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
                        success: function(response) {
                            console.log('Success:', response);
                            // Redirigir a área privada
                            window.location.href = 'zonaPrivada';
                        },
                        error: function(xhr) {
                            console.error('Error:', xhr);
                            if (xhr.status === 409) {
                                mensajeError("El email ya está registrado.");
                            } else {
                                mensajeError("Error en el registro. Por favor, inténtalo de nuevo.");
                            }
                        }
                    });
                }
            });
});



