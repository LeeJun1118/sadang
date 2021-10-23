$(document).ready(function($) {
    $("#goBoard tr").click(function() {
        window.document.location = $(this).find('td:eq(0)').attr("href");
    });



    $('.deleteBoard').on('click', function (event) {

        var boardId = $('.deleteBoardId').val();

        console.log("sendData=="+boardId);

        $.ajax({
            type: "POST",
            url: "/board/delete",
            data: JSON.stringify(boardId),
            async: false,
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                if (JSON.parse(data) == 1) {
                    location.replace("/")
                } else {
                    alert("작성자만 삭제할 수 있습니다.")
                }
            },
            error: function (request, status, error, jqXHR, textStatus, errorThrown) {
                alert("삭제 중 문제 발생!!")
                //console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                console.log("ERROR : " + textStatus + " : " + errorThrown);
            }
        });

        event.preventDefault();
    })

    $('.verifyWriter').on('click', function (event) {
        var myUrl = $('.verifyWriter').attr('href');
        console.log("myUrl=="+myUrl);
        $.ajax({
            type: "GET",
            url: myUrl,
            async: false,
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                console.log("JSON.parse Data==="+JSON.parse(data));
                var boardId = JSON.parse(data);
                if (boardId == -1) {
                    alert("작성자만 수정할 수 있습니다.")
                } else {
                    location.replace("/board/update/" + boardId)
                    alert('수정하시겠습니까?');
                }
            },
            error: function (request, status, error, jqXHR, textStatus, errorThrown) {
                alert("수정 중 문제 발생!!")
                console.log("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                // console.log("ERROR : " + textStatus + " : " + errorThrown);
            }
        });
        event.preventDefault();
    })

});

var img = document.getElementsByClassName("myImage");
for (var x = 0; x < img.length; x++) {
    // img.item(x).onclick=function() {window.open(this.src)};
    img.item(x).onclick=function() {window.document.location = this.src};
}

var goBoard = function(boardId) {
    window.document.location = "board/" + boardId;
}