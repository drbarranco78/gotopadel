const patronEmail=/^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\.[a-zA-Z]{2,}$/;
const patronPassword=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;
const patronNombre = /^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$/;
let emailLogin, contrasenaLogin, nombre, emailRegistro, contrasena1, contrasena2, nivel, genero;

$(document).ready(function() {
    $("#enlace-login").click(function() {
        $(".intro").fadeOut(1000, function () {
            $(this).css("display","none");
            $(".login").fadeIn(1000);
            $(".login").css("display","block");
        });        
    });
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

    function validarLogin() {
        // let devolver = false;        
        emailLogin = $("#email-login").val();
        contrasenaLogin = $("#contrasena-login").val();
    
        // Valida el email y la contraseña
        if (patronEmail.test(emailLogin) && patronPassword.test(contrasenaLogin)) {
            return true;
        } else if (!patronEmail.test(emailLogin)) {
            alert("Introduzca una dirección de correo válida");
            return false;            
        } else if (!patronPassword.test(contrasenaLogin)) {
            alert("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número");
            return false;             
        }      
        
    }
    $("#formulario-login").submit(function(event) {
        event.preventDefault();
        if (validarLogin()) {
            emailLogin = $("#email-login").val();
            contrasenaLogin = $("#contrasena-login").val();
            $.ajax({
                url: '/api/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({email: emailLogin, contrasena: contrasenaLogin}),
                success: function(response) {
                    console.log('Success:', response);
                    // Redirigir a area privada
                    window.location.href = 'zonaPrivada.html';
                },
                error: function(error) {
                    console.error('Error:', error);
                    alert("Error en el inicio de sesión. Por favor, inténtalo de nuevo.");
                }
            });            
        }
    });

    function validarRegistro() {
        nombre=$("#nombre").val();
        emailRegistro=$("#email-registro").val();
        contrasena1=$("#contrasena1").val();
        contrasena2=$("#contrasena2").val();
        nivel=$("#nivel").val();
        genero = $("input[name='genero']:checked").val();

        if (!patronNombre.test(nombre)) {
            alert("El campo Nombre no puede contener caracteres especiales ni espacios consecutivos, y debe tener una longitud entre 2 y 50 ");
            return false;            
        }
        if (!patronEmail.test(emailRegistro)) {
            alert("Introduzca una dirección de correo válida de 60 caracteres máximo");
            return false;            
        }
        if (!patronPassword.test(contrasena1)) {
            alert("La contraseña debe tener entre 8 y 20 caracteres, y al menos una mayúscula, una minúscula y un número");
            return false;            
        }
        if (contrasena1 !== contrasena2) {
            alert("La contraseña tiene que coincidir en los 2 campos");
            return false;            
        }   
        return true;     
    }

    $("#formulario-registro").submit(function(event) {
        event.preventDefault();
        if (validarRegistro()) {
            $.ajax({
                url: '/registro',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    nombre: nombre, 
                    email: emailRegistro, 
                    contrasena: contrasena1, 
                    nivel: nivel, 
                    genero: genero
                }),
                success: function(response) {
                    console.log('Success:', response);
                    // Redirigir a área privada
                    window.location.href = 'zonaPrivada.html';
                },
                error: function(error) {
                    console.error('Error:', error);
                    alert("Error en el registro. Por favor, inténtalo de nuevo.");
                }
            });
        }
    });
});