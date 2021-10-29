var socket = null;
var stompClient = null;
var data = null;

var webSocket;
var nickname;
var roomId = document.getElementById("roomId").textContent;
var token;

document.getElementById("name").addEventListener("click", function () {
    nickname = document.getElementById("nickname").value;
    token = document.getElementById("token").value;
    document.getElementById("nickname").style.display = "none";
    document.getElementById("name").style.display = "none";
    connect();
})
document.getElementById("send").addEventListener("click", function () {
    send();
})

function connect() {
    data = {
        'type': 'ENTER',
        'roomId': this.roomId,
        'sender': $("#nickname").val(),
        'message': '입장'
    };
    socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    console.log("this.token == " + this.token);

    stompClient.connect({"token": this.token}, function() {
        stompClient.subscribe('/sub/chat/room/' + roomId, function (message) {
            console.log("Send Message === " +JSON.parse(message.body).message);
            showGreeting(JSON.parse(message.body).message);
        })

        console.log("####!!!!!Enter Room token === " + token);
        // "Sender"님이 입장하셨습니다. 메시지 보냄
        stompClient.send("/pub/chat/message", {"token": token}, JSON.stringify(data));
    })

}

function showGreeting(message) {
    var messageSpace = document.getElementById("sendMessage");
    messageSpace.innerHTML = messageSpace.innerHTML +
        "<li class='list-group-item'>" +
        message +
        "</li>";
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
        'roomId': this.roomId,
        'sender': $("#nickname").val(),
        'message': $("#message").val()
    };
    stompClient.send("/pub/chat/message", {"token":this.token}, JSON.stringify(data));
    $("#message").val('');
}
