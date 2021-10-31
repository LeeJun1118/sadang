var socket = null;
var stompClient = null;
var data = null;

var webSocket;
var nickname;
var roomId = document.getElementById("roomId").textContent;

$(document).ready(function () {
});
connect();

document.getElementById("send").addEventListener("click", function () {
    nickname = document.getElementById("nickname").value;
    send();
})



function connect() {
    /*<![CDATA[*/
    var roomIdList = [[${roomIdList}]];
    /*]]>*/


    for (var i in roomIdList) {
        console.log(roomIdList[i].roomId)
    }

    /*data = {
        'type': 'ENTER',
        'roomId': '',
        // 'roomId': this.roomId,
        'sender': $("#nickname").val(),
        'message': 'Enter Room'
    };*/
    socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {

        // for (var i in roomIdList) {
            data = {
                'type': 'ENTER',
                // 'roomId': roomIdList[i].roomId,
                'roomId': this.roomId,
                'sender': $("#nickname").val(),
                'message': 'Enter Room'
            };
            // stompClient.subscribe('/sub/chat/room/' + roomIdList[i].roomId, function (message) {
            stompClient.subscribe('/sub/chat/room/' + roomId, function (message) {
                console.log("Send Message === " + JSON.parse(message.body).message);
                showGreeting(JSON.parse(message.body).sender, JSON.parse(message.body).message);

            })
        // }

    })

}

function showGreeting(sender,message) {
    var messageSpace = document.getElementById("sendMessage");
    messageSpace.innerHTML = messageSpace.innerHTML +
        "<li class='list-group-item'>" +
        "[" + sender + "] - " + message +
        "</li>";
    toastr.success('메시지가 도착했습니다.');
    messageSpace.scrollTop = messageSpace.scrollHeight;
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function send() {
    data = {
        'type': 'TALK',
        // 'roomId': myRoomId,
        'roomId': this.roomId,
        'sender': $("#nickname").val(),
        'message': $("#message").val()
    };
    stompClient.send("/pub/chat/message", {}, JSON.stringify(data));

    $("#message").val('');
}
