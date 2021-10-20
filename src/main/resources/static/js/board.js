$(document).ready(function($) {
    $("#goBoard tr").click(function() {
        window.document.location = $(this).find('td:eq(0)').attr("href");
    });
});