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
});

document.addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    }
}, true);


$(document).ready(function () {
    $('#sendMail').click(function () {
        var mail = $('#email').val();
        console.log("이메일 ===" + mail)
        var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;

        if ($('#email').val().length == 0) {
            alert("이메일을 입력해주세요")
            $('#email').focus();
            return false;
        } else {
            if (!reg_email.test(mail)) {
                alert("이메일 형식을 확인해주세요")
                return false;
            } else {
                return true;
            }
        }
    });

    $('#userId-btn').click(function () {
        if ($('#username').val().length == 0) {
            alert("이름을 입력해주세요")
            $('#username').focus();
            return false;
        }

        if ($('#userId').val().length == 0) {
            alert("ID를 입력해주세요")
            $('#userId').focus();
            return false;
        }
        if ($('#password').val().length == 0) {
            alert("비밀번호를 입력해주세요")
            $('#password').focus();
            return false;
        }
        if ($('#address').val().length == 0) {
            alert("주소를 입력해주세요")
            $('#address').focus();
            return false;
        }
        if ($('#detailAddress').val().length == 0) {
            alert("주소를 입력해주세요")
            $('#detailAddress').focus();
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
                } else {
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


    $('#username').on('blur', function (event) {

        var username = $('#username').val();
        var sendData = {"username": username};
        console.log(sendData)
        console.log(username)
        console.log(JSON.stringify(sendData))

        $.ajax({
            type: "POST",
            url: "/idCheck",
            // data: JSON.stringify(sendData),
            data: JSON.stringify(username),
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
                    $("#username").css("margin-bottom", "0px");
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


    $('#updateUsername').on('blur', function (event) {

        var updateUserId = $('#updateUsername').val();
        var sendData = {"updateUserId": updateUserId};
        console.log(sendData)
        console.log(updateUserId)
        console.log(JSON.stringify(sendData))

        $.ajax({
            type: "POST",
            url: "/updateIdCheck",
            // data: JSON.stringify(sendData),
            data: JSON.stringify(updateUserId),
            async: false,
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                if (JSON.parse(data) > 0) {
                    console.log(JSON.parse(data));
                    $("#update_id_check").css("color", "red");
                    $("#update_id_check").css("margin-bottom", "15px");
                    $("#update_id_check").text("사용할 수 없는 아이디입니다.");
                    $("#updateUserId").css("margin-bottom", "0px");
                    $("#updateUserId-btn").attr("disabled", true);
                } else {
                    console.log(JSON.parse(data));
                    $("#update_id_check").css("color", "blue");
                    $("#update_id_check").css("margin-bottom", "15px");
                    $("#update_id_check").text("사용할 수 있는 아이디입니다.");
                    $("#updateUsername").css("margin-bottom", "0px");
                    $("#updateUserId-btn").attr("disabled", false);
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

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("address").value = extraAddr;

            } else {
                document.getElementById("address").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('address').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            // document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

var timerID;

/*$(document).ready(function () {
    $('#loginCheck').on('click',function(e){
        e.preventDefault();
        updateData();
    });

    window.onload = function() {
        document.getElementById('loginCheck').click();
    }
});*/

var i = 1;
if (i === 1)
    timerID = setTimeout("updateData()", 10); // 0.01초 단위로 갱신 처리
i++;
if (i !== 1) {
    console.log("my Ajax 1 ===== " + i);
    timerID = setTimeout("updateData()", 3000); // 1초 단위로 갱신 처리
    console.log("my Ajax 2 ===== " + i);
}

function updateData() {

    $.ajax({
        url: "/loginCheck",
        type: "get",
        data: '',
        dataType: 'json',
        async: false,
        contentType: "application/json; charset=utf-8",
        cache: false,
        success: function (data) {
            if (JSON.parse(data) != -1) {
                $("#loginVerify").text("로그아웃");
                $("#loginVerify").css('cursor', 'pointer');
                $("#loginVerify").css('color', 'blue');
                // $("#loginVerify").attr("href","/user/out");
                $('#loginVerify').on('click', function (e) {
                    e.preventDefault();
                    location.replace("/user/out")
                });

                $("#myInfo").text("내 정보");
                $("#myInfo").css('cursor', 'pointer');
                $("#myInfo").css('color', 'blue');
                $('#myInfo').on('click', function (e) {
                    e.preventDefault();
                    location.replace("/myPage")
                });

                console.log("auth js in data =====" + data);
                /*if(data === 0){
                    $("#count-message").css('display', 'none');
                }
                else*/ if (data > 0) {
                    $("#count-message").text(data);
                    $("#plus").css('display', '');
                }
                else
                    $("#plus").css('display', 'none');

                $("#count-message").css('cursor', 'pointer');
                $("#count-message").css('color', 'red');

                /*if (data > 0) {
                    $("#unReadMessages").text('메세지 +');
                } else {
                    $("#unReadMessages").text('메세지');
                }*/
                $("#unReadMessages").text('메세지');

                $("#enter-chatRoom").css('cursor', 'pointer');
                $("#enter-chatRoom").css('color', 'blue');
                $('#enter-chatRoom').on('click', function (e) {
                    e.preventDefault();
                    location.replace("/chat/myChatRoom");
                });


            } else {
                $("#plus").css('display', 'none');

                $("#loginVerify").text("로그인");
                $("#loginVerify").css('cursor', 'pointer');
                $("#loginVerify").css('color', 'blue');
                // $("#loginVerify").attr("href","/login");
                $('#loginVerify').on('click', function (e) {
                    e.preventDefault();
                    location.replace("/login")
                });

                $("#signUp").text("회원가입");
                $("#signUp").css('cursor', 'pointer');
                $("#signUp").css('color', 'blue');
                $('#signUp').on('click', function (e) {
                    e.preventDefault();
                    location.replace("/signup")
                });

            }
        },
    });

}
