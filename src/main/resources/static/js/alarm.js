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
    /*$.each(get_input, function (index, value) {
        if ($(value).val() == roomId){
            $(value).innerHTML = $(value).innerHTML +
                "<div>"
        }
    }*/
    // var messageSpace = document.getElementById("show-toastr");
    toastr.success('메시지가 도착했습니다.');
    // messageSpace.scrollTop = messageSpace.scrollHeight;
}