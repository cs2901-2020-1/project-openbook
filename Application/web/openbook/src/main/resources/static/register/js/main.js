(function ($) {
    "use strict";

    /*==================================================================
    [ Focus Contact2 ]*/
    $('.input100').each(function(){
        $(this).on('blur', function(){
            if($(this).val().trim() != "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })
    })

    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit',function(){
        var check = true;

        for(var i=0; i<input.length; i++) {
            if(validate(input[i]) == false){
                showValidate(input[i]);
                check=false;
            }
        }

        return check;
    });


    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
            hideValidate(this);
        });
    });

    function validate (input) {
        if($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if($(input).val().trim() == ''){
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }

})(jQuery);


function student_registration(){

    $("#student_button").css("background-color","white");
    $("#student_button").css("color","black");


    $("#profesor_button").css("background-color","#009ada");
    $("#profesor_button").css("color","white");


    content = '<form action="/do_register_student" method="post" class="contact100-form validate-form">\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Nombres :</span>\
                    <input class="input100" type="text" name="name" placeholder="Ingresa tus nombres">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Apellidos :</span>\
                    <input class="input100" type="text" name="surname" placeholder="Ingresa tus apellidos">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Fecha de nacimiento :</span>\
                    <input class="input100" type="date" name="date" placeholder="Ingresa tu fecha de nacimiento">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate = "Ingresa un email valido: ex@abc.xyz">\
                    <span class="label-input100">Email:</span>\
                    <input maxlength="30" class="input100" type="email" name="email" placeholder="Ingrese su correo electrónico">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Password:</span>\
                    <input class="input100" type="password" name="password" placeholder="Ingrese una contraseña">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="container-contact100-form-btn">\
     \
                   <input type="submit" class="contact100-form-btn" value="Register">\
                   </div>\
            </form>';

    $("#form").html(content);
}

function profesor_registration(){

    $("#student_button").css("background-color","#009ada");
    $("#student_button").css("color","white");

    $("#profesor_button").css("background-color","white");

    $("#profesor_button").css("color","black");

    content = '<form action="/do_register_profesor" method="post" class="contact100-form validate-form">\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Nombres :</span>\
                    <input class="input100" type="text" name="name" placeholder="Ingresa tus nombres">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Apellidos :</span>\
                    <input class="input100" type="text" name="surname" placeholder="Ingresa tus apellidos">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate = "Ingresa un email valido: ex@abc.xyz">\
                    <span class="label-input100">Email:</span>\
                    <input maxlength="30" class="input100" type="email" name="email" placeholder="Ingrese su correo electrónico">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="wrap-input100 validate-input" data-validate="Ups...Olvidaste llenar este campo">\
                    <span class="label-input100">Password:</span>\
                    <input class="input100" type="password" name="password" placeholder="Ingrese una contraseña">\
                    <span class="focus-input100"></span>\
                </div>\
\
                <div class="container-contact100-form-btn">\
     \
                   <input type="submit" class="contact100-form-btn" value="Register">\
                   </div>\
            </form>';


    $("#form").html(content);
}