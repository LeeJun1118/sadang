var socket = null;
var stompClient = null;
var data = null;


var get_input = $("#roomList input[type=text]");


connect();

function connect() {

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
                showGreeting(JSON.parse(message.body).sender, JSON.parse(message.body).message);
            })
            console.log('value =' + $(value).val());
        });

    })

}


function showGreeting(sender, message) {

    var countMessages = $("#count-message");
    var count = countMessages.text();


    $("#plus").text('+');
    if (count === "" || !Number.isInteger(count) || count === 0)
        countMessages.text(1);
    else {
        countMessages.text(parseInt(count) + 1);
    }
    if (count > 0)
        countMessages.text(parseInt(count) + 1);

    countMessages.css('display', '');

    toastr.success('메세지가 도착했습니다.');
    // messageSpace.scrollTop = messageSpace.scrollHeight;
}