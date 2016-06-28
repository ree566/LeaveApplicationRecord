<%-- 
    Document   : overtimeRequest
    Created on : 2016/3/4, 下午 03:25:49
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="css/serverMessage.css">
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 90%;
            }
            #form-wiget{
                /*visibility:hidden;*/
            }
            #bento{
                width:300px; 
                height: 50px; 
                /*background-color: red;*/
            }
            #serverMsg{
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/serverMessage.js"></script>
        <script src="js/moment.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script>
            var minHourForBandon = 2;

            function getAllBandon() {
                var now = moment();
                var hour = now.hour();
                var beginHour = 13;
                var endHour = 17;
                if ((hour < beginHour || endHour < hour) && $("#userPermission").val() <= 3) {
                    $("input,select").attr("disabled", true);
                    $("#serverMsg").html("已經超過申請時間，開放的時間為" + beginHour + "點到" + endHour + "點。");
                }
                $.ajax({
                    type: "Post",
                    url: "GetBandon",
                    dataType: 'json',
                    success: function (response) {
                        var arr = response;
                        for (var i = 0; i < arr.length; i++) {
                            $("#bentoStoreName").append("<option value=" + arr[i].departmentId + ">" + arr[i].name + "</option>");
                        }

                        $("#bentoStoreName").change(function () {
                            var selectIndex = $(this)[0].selectedIndex - 1;
                            if (selectIndex < 0) {
                                $("#bentoList").html("<option value=''>請選擇便當</option>");
                                return false;
                            }
                            var bandonList = arr[selectIndex].bandonList;
                            $("#bentoList").html("");
                            for (var i = 0; i < bandonList.length; i++) {
                                $("#bentoList").append("<option value=" + bandonList[i].id + ">" + bandonList[i].name + "</option>");
                            }
                        });

                    }
                });
            }

            function checkUserRequested() {
                $.ajax({
                    type: "Post",
                    url: "CheckOvertimeRequested",
                    dataType: 'json',
                    data: {
                        overtimeHours: $("#overtime").val(),
                        bandonId: $("#bentoList").val()
                    },
                    success: function (response) {
                        if (response == false) {
                            disabledUserInput();
                        } else {
                            getAllBandon();
                        }
                    }
                });
            }

            function disabledUserInput() {
                $("select,input").attr("disabled", "disabled");
            }

            function saveOvertimeRequest() {
                var hour = $("#overtime").val();
                var bandonId = $("#bentoList").val();

                console.log(bandonId);

                if (hour < minHourForBandon && bandonId != "") {
                    $("#serverMsg").html("您的加班時數低於可訂便當的時數( " + minHourForBandon + " 小時)，請重新輸入。");
                    return false;
                }

                $.ajax({
                    type: "Post",
                    url: "OvertimeReq",
                    dataType: 'json',
                    data: {
                        overtimeHours: hour,
                        bandonId: bandonId
                    },
                    success: function (response) {
                        if (response == true) {
                            console.log(response);
                            $("#serverMsg").html("");
                            disabledUserInput();
                        }
                        showServerMsg(response);
                    }
                });
            }

            $(function () {
//                $("#bento").hide();
                $("select,input").addClass("form-control");
                checkUserRequested();

                $("#send").click(function () {
                    saveOvertimeRequest();
                });

                $("#overtime").change(function () {
                    if ($(this).val() >= minHourForBandon) {
//                        $("#bento").show();
                    } else {
//                        $("#bento").hide();
                    }
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <input type="hidden" id="userPermission" value="${sessionScope.permission}">
        <div id="wigetCtrl">
            <h3>請選擇今日加班時數，大於2小時者請選擇便當。</h3>
            <div id="form-wiget" class="form-inline">
                <!--加班時數>2小時候出現-->
                <input type="number" id="overtime" min="0.5" max="4" step="0.5" value="0.5" />
                <div id="bento" class="form-control">
                    <select id="bentoStoreName">
                        <option value="">請選擇店家</option>
                    </select>
                    <select id="bentoList">
                    </select>
                </div>
                <input type="button" id="send" value="send">
                <div id="serverMsg"></div>
            </div>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
