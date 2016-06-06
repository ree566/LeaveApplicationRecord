function showServerMsg(msg) {
    $("#floatWiget").show();
    if (msg) {
        $("#floatWiget").html("<div id='message' style='background-color:green'>儲存成功</div>");
    } else {
        $("#floatWiget").html("<div id='message' style='background-color:red'>發生問題，請聯絡管理員。</div>");
    }
    setTimeout(function () {
        $('#message').fadeOut(function () {
            $(this).remove();
            $("#floatWiget").hide();
        });
    }, 5000);
}