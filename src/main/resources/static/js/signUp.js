$(document).ready(function () {

    var current_fs, next_fs, previous_fs; //fieldsets
    var opacity;

    $(".next").click(function () {

        current_fs = $(this).parent();
        next_fs = $(this).parent().next();

//Add Class Active
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

//show the next fieldset
        next_fs.show();
//hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
// for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                next_fs.css({'opacity': opacity});
            },
            duration: 600
        });
    });

    $(".previous").click(function () {

        current_fs = $(this).parent();
        previous_fs = $(this).parent().prev();

//Remove class active
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

//show the previous fieldset
        previous_fs.show();

//hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
// for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                previous_fs.css({'opacity': opacity});
            },
            duration: 600
        });
    });

    $('.radio-group .radio').click(function () {
        $(this).parent().find('.radio').removeClass('selected');
        $(this).addClass('selected');
    });

    $(".submit").click(function () {
        return false;
    })


    // $('.registerForm').on('submit', function (event) {
        $('.registerForm').submit(function (event) {

        var sendData = $('.registerForm').val();//serialize();
        var param = {"username" : sendData};

        console.log(sendData.attr("username"))

        $.ajax({
            type: "POST",
            url: "/confirm",
            data:  {"username" : sendData},//sendData,//.attr("username"),
            contentType: "application/json; charset=UTF-8",
            success: function (data) {
                console.log("success data : "+data);
                alert('Login Success!!');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })
});

document.addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    }
}, true);


$(document).ready(function () {
    $('.registerForm').on('submit', function (event) {

        var sendData = $('.registerForm').val();
        console.log(sendData)

        $.ajax({
            type: "POST",
            url: "/confirm",
            data:  {"username" : sendData},
            async: false,
            contentType: "application/json; charset=UTF-8",
            success: function (data) {
                console.log("success data : "+data);
                alert('회원 가입 완료');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("ERROR : " + textStatus + " : " + errorThrown);
                alert("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })
});
