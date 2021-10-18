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

/*
    // $('.registerForm').on('submit', function (event) {
    $('.registerForm').submit(function (event) {

        var sendData = $('.registerForm').val();//serialize();
        var param = {"username": sendData};

        console.log(sendData.attr("username"))

        $.ajax({
            type: "POST",
            url: "/confirm",
            data: {"username": sendData},//sendData,//.attr("username"),
            contentType: "application/json; charset=UTF-8",
            success: function (data) {
                console.log("success data : " + data);
                alert('Login Success!!');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })*/
});

document.addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    }
}, true);


$(document).ready(function () {
    $('#userId-btn').click(function () {
        if ($('#username').val == ""){
            alert("이름을 입력해주세요")
            $('#username').focus();
            return false;
        }

        if ($('#userId').val().length == 0){
            alert("ID를 입력해주세요")
            $('#userId').focus();
            return false;
        }
        if ($('#password').val().length == 0){
            alert("비밀번호를 입력해주세요")
            $('#password').focus();
            return false;
        }
        if ($('#address').val().length == 0){
            alert("주소를 입력해주세요")
            $('#address').focus();
            return false;
        }

    });

    $('.registerForm').on('click', function (event) {

        var sendData = $('.registerForm').serialize();
        console.log(sendData)
        console.log(JSON.stringify(sendData))

        $.ajax({
            type: "POST",
            url: "/confirm",
            data: JSON.stringify(sendData),
            async: false,
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                if (JSON.parse(data) == 1) {
                    alert('회원 가입 완료');
                    location.replace("/login")
                }
                else {
                    alert("메일 인증을 완료해주세요")
                }
            },
            error: function (request, status, error, jqXHR, textStatus, errorThrown) {
                alert("메일 확인 중 문제 발생!!")
                //console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                console.log("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })


    $('#userId').on('blur', function (event) {

        var userId = $('#userId').val();
        var sendData = {"userId" : userId};
        console.log(sendData)
        console.log(JSON.stringify(sendData))

        $.ajax({
            type: "POST",
            url: "/idCheck",
            data: JSON.stringify(sendData),
            async: false,
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                if (JSON.parse(data) > 0) {
                    console.log(JSON.parse(data));
                    $("#id_check").css("color", "red");
                    $("#id_check").css("margin-bottom", "15px");
                    $("#id_check").text("사용할 수 없는 아이디입니다.");
                    $("#userId").css("margin-bottom", "0px");
                    $("#userId-btn").attr("disabled", true);
                } else {
                    console.log(JSON.parse(data));
                    $("#id_check").css("color", "blue");
                    $("#id_check").css("margin-bottom", "15px");
                    $("#id_check").text("사용할 수 있는 아이디입니다.");
                    $("#userId").css("margin-bottom", "0px");
                    $("#userId-btn").attr("disabled", false);
                }

            },
            error: function (request, status, error, jqXHR, textStatus, errorThrown) {
                console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                //console.log("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })
});
