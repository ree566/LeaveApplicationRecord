<%-- 
    Document   : pswChange
    Created on : 2016/3/15, 下午 02:13:47
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 90%;
            }
            #serverMsg{
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/datepicker-zh-TW.js"></script>
        <script src="js/moment.js"></script>
        <script src="js/bootstrap-datetimepicker.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/zh-tw.js"></script>
        <script>
            $(function () {
                $("#change").click(function () {
                    var oldpsw = $("#oldpassword").val();
                    var psw1 = $("#password").val();
                    var psw2 = $("#password2").val();
                    if(oldpsw == psw1){
                        $("#serverMsg").html("舊密碼與新密碼不得相同");
                        return false;
                    }
                    if (psw1 != psw2) {
                        $("#serverMsg").html("請確認您的密碼欄位1與欄位2是否相同");
                        return false;
                    }
                    if (!psw1.match(/^(?=.*\d)(?=.*[a-z])[0-9a-zA-Z]{6,}$/)) {
                        $("#serverMsg").html("請確認您的密碼格式，至少要有一個\"小寫英文\"和\"數字\"，最低長度為6，且不能是特殊符號。");
                        return false;
                    }
                    $.ajax({
                        type: "Post",
                        url: "ChangePassword",
                        dataType: 'html',
                        data: {
                            oldpassword: oldpsw,
                            password: psw1
                        },
                        success: function (response) {
                            var serverMsg = "";
                            if(response == "true"){
                                $("input[type='password']").val("");
                                serverMsg = "success";
                            }else{
                                serverMsg = "請確認您的舊密碼是否正確";
                            }
                            $("#serverMsg").html(serverMsg);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
            <div id="wigetCtrl">
                <h3>Please change your password.</h3>
                <input type="password" id="oldpassword" placeholder="請輸入舊密碼">
                <input type="password" id="password" placeholder="請輸入新密碼">
                <input type="password" id="password2" placeholder="請再次輸入密碼">
                <input type="button" id="change" value="確定">
                <div id="serverMsg"></div>
            </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
