function showServerMsg(msg) {
    $("#floatWiget").show();
    if (msg || msg == "") {
        $("#floatWiget").html("<div id='message' style='background-color:green'>儲存成功</div>");
    } else if (msg == false || msg != "") {
        $("#floatWiget").html("<div id='message' style='background-color:red'>" + msg == false ? "發生問題，請聯絡管理員。" : msg + "</div>");
    }
    setTimeout(function () {
        $('#message').fadeOut(function () {
            $(this).remove();
            $("#floatWiget").hide();
        });
    }, 5000);
}
