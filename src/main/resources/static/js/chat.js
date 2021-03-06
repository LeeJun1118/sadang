var socket = null;
var stompClient = null;
var data = null;

var webSocket;
var nickname;
var roomId = document.getElementById("roomId").value;

var get_input = $("#roomList input[type=text]");
var get_span = $(".unRead span[type=text]");


// hidden();
connect();

/*function hidden(){
    $.each(get_span, function (index, value) {
        if($(value).text() == 0){
            $(value).style.visibility = 'hidden';
        }
    })
}*/

document.getElementById("send").addEventListener("click", function () {
    nickname = document.getElementById("nickname").value;
    send();
})

function connect() {
    var element = document.getElementById('message-history');
    element.scrollTop = element.scrollHeight;

    socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {

        $.each(get_input, function (index, value) {

            stompClient.subscribe('/sub/chat/room/' + $(value).val(), function (message) {
                console.log("roomIdList[i].roomId === " + $(value).val());

                const result = JSON.parse(message.body);

                $('.' + roomId + 'lastMessageTime').text('' + result.createdDate);
                if (result.roomId === roomId) {
                    console.log("여기 입니다. createdDate" + result.createdDate)
                    showGreeting(result);
                } else {
                    showToastr(result.roomId);
                }
            })
            console.log('value =' + $(value).val());
        });
    })


    $('#message').on('input', function () {
        if ($('#message').val() === ''){
            $('#send').attr("disabled",true);
        }
        else{
            $('#send').attr("disabled",false);
        }
    })
}

/*function connect() {

    data = {
        'type': 'ENTER',
        'roomId': roomId,
        // 'roomId': this.roomId,
        'sender': $("#nickname").val(),
        'message': 'Enter Room'
    };
    socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {

        data = {
            'type': 'ENTER',
            'roomId': roomId,
            'sender': $("#nickname").val(),
            'message': 'Enter Room'
        };
        stompClient.subscribe('/sub/chat/room/' + roomId, function (message) {
            console.log("Send Message === " + JSON.parse(message.body).message);
            showGreeting(JSON.parse(message.body).sender, JSON.parse(message.body).message);

        })

    })

}*/

function showGreeting(message) {
    var messageSpace = document.getElementById("sendMessage");
    console.log("showGreeting create Date Time ==== " + message);
    console.log("showGreeting create Date Time ==== " + message.body);

    if (nickname == message.sender) {
        messageSpace.innerHTML = messageSpace.innerHTML +
            "<div class=\"outgoing_msg mb-3\">\n" +
            "   <div class=\"sent_msg\">\n" +
            "       <p>" + message.message + "</p>\n" +
            "       <span class=\"time_date\">" + message.createdDate + "</span>\n" +
            "   </div>\n" +
            "</div>\n";
    } else {
        messageSpace.innerHTML = messageSpace.innerHTML +
            "<div class=\"incoming_msg mb-3\" th:if=\"${message.sender} != ${username}\">\n" +
            "   <div class=\"incoming_msg_img\">\n" +
            "       <img src=\"/css/images/userProfile.png\" alt=\"sunil\">\n" +
            // "       <img src=\"https://ptetutorials.com/images/user-profile.png\" alt=\"sunil\">\n" +
            "   </div>\n" +
            "   <div class=\"received_msg\">\n" +
            "       <div class=\"received_withd_msg\">\n" +
            "           <p>" + message.message + "</p>\n" +
            "               <span class=\"time_date\">" + message.createdDate + "</span>\n" +
            "       </div>\n" +
            "   </div>\n" +
            "</div>";

    }
    alarmMessage()

    var element = document.getElementById('message-history');
    element.scrollTop = element.scrollHeight;
}

function showToastr(alarmRoomId) {
    var alarmRoom = $('.' + alarmRoomId);
    $('.' + alarmRoomId + 'plus').text('+');
    $("#plus").text('+');
    var count = alarmRoom.text();

    alarmRoom.text(parseInt(count) + 1);

    if (count === "")
        alarmRoom.text(1);



    var countMessages = $("#count-message");
    var count = countMessages.text();


    if (count === "" || !Number.isInteger(count) || count === 0)
        countMessages.text(1);
    else {
        countMessages.text(parseInt(count) + 1);
    }
    if (count > 0)
        countMessages.text(parseInt(count) + 1);

    countMessages.css('display', '');
    toastr.success('메시지가 도착했습니다.');
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function send() {
    data = {
        'type': 'TALK',
        'roomId': roomId,
        'sender': $("#nickname").val(),
        'message': $("#message").val(),
        'senderStatus' : 'Y',
        'receiverStatus' : 'N'
    };
    stompClient.send("/pub/chat/message", {/*'roomId' : roomId, 'username' :$("#nickname").val() */}, JSON.stringify(data));
    console.log(JSON.stringify(data))


    $("#message").val('');
}



function alarmMessage() {

    $.ajax({
        type: "POST",
        url: '/chat/room/enter/' + roomId,
        async: false,
        dataType: 'json',
        contentType: "application/json",
        success: function (data) {
            console.log("JSON.parse Data===" + JSON.parse(data))
        },
        error: function (request, status, error, jqXHR, textStatus, errorThrown) {
            alert("수정 중 문제 발생!!")
            console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
            console.log("ERROR : " + textStatus + " : " + errorThrown);
        }
    });
    // })

}

function parse(str) {
    var year = str.substr(0, 4);
    var month = str.substr(5, 2);
    var day = str.substr(8, 2);
    var hour = str.substr(11, 2);
    var minute = str.substr(14, 2);
    var time = str.substr(11,5)

    // return month + "-" + day + " " + hour + ":" + minute;
    return month + "-" + day + " " + time;
}

// );
