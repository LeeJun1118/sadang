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
                    alert('삭제 되었습니다.');
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
});