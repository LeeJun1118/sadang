var socket = null;
var stompClient = null;
var data = null;

var webSocket;
var nickname;
var roomId = document.getElementById("roomId").value;

var get_input = $("#roomList input[type=text]");
connect();

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

            data = {
                'type': 'ENTER',
                'roomId': $(value).val(),
                'sender': $("#nickname").val(),
                'message': 'Connect sockJs'
            };

            stompClient.subscribe('/sub/chat/room/' + $(value).val(), function (message) {
                console.log("roomIdList[i].roomId === " + $(value).val());

                if (JSON.parse(message.body).roomId === roomId) {
                    showGreeting(JSON.parse(message.body).sender, JSON.parse(message.body).message);
                } else {
                    showToastr();
                }
            })
            console.log('value =' + $(value).val());
        });

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

function showGreeting(sender, message) {
    var messageSpace = document.getElementById("sendMessage");
    if (nickname == sender) {
        messageSpace.innerHTML = messageSpace.innerHTML +
            "<div class=\"outgoing_msg mb-3\">\n" +
            "   <div class=\"sent_msg\">\n" +
            "       <p>" + message + "</p>\n" +
            "       <span class=\"time_date\"> 11:01 AM    |    June 9</span>\n" +
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
            "           <p>" + message + "</p>\n" +
            "               <span class=\"time_date\"> 11:01 AM    |    June 9</span>\n" +
            "       </div>\n" +
            "   </div>\n" +
            "</div>";

    }

    var element = document.getElementById('message-history');
    element.scrollTop = element.scrollHeight;
}

function showToastr() {
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
        'message': $("#message").val()
    };
    stompClient.send("/pub/chat/message", {}, JSON.stringify(data));

    $("#message").val('');
}
