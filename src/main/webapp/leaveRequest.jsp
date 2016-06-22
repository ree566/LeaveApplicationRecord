<%-- 
    Document   : leaveRequest
    Created on : 2016/2/23, 上午 09:59:56
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
                width: 95%;
                margin: 0px auto;
            }
            #serverMsg{
                color:red;
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/datepicker-zh-TW.js"></script>
        <script src="js/moment.js"></script>
        <script src="js/bootstrap-datetimepicker.min.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <script src="js/jquery.blockUI.Default.js"></script>
        <script src="js/zh-tw.js"></script>
        <script src="js/dropDownList.js"></script>
        <script>
            $(document).ready(function () {
                var now = moment();
//                console.log(now.hour());
                if (now.hour() >= 22) {
                    $("input,select").attr("disabled", true);
                    $("#serverMsg").html("已經超過申請時間，請明天再試。");
                }

                var momentFormatString = 'YYYY-MM-DD HH:mm';

                initDropdownList();
                $(":text,input[type='number'],select").addClass("form-control");
                $(":button").addClass("btn btn-default");
                //both array from dropDownList.js

                var options = {
                    defaultDate: moment({hour: 8, minute: 30}),
                    minDate: moment({hour: 8, minute: 30}),
                    useCurrent: true,
                    locale: "zh-tw",
                    stepping: 30,
                    sideBySide: true,
                    format: momentFormatString,
                    extraFormats: [momentFormatString],
                    disabledHours: [0, 1, 2, 3, 4, 5, 6, 7, 18, 19, 20, 21, 22, 23, 24]
                };
                var beginTimeObj = $('#fini').datetimepicker(options);
                var endTimeObj = $('#ffin').datetimepicker(options);

                beginTimeObj.on("dp.change", function (e) {
                    endTimeObj.data("DateTimePicker").minDate(e.date);
                });

                $("#leaveType").change(function () {
                    var selected = $(this).val();
                    switch (selected) {
                        case "1":
                        case "4":
                        case "7":
                            showReasonOptions();
                            break;
                        default:
                            $("#remark").hide();
                            break;
                    }
                });

                $("#send").click(function () {
                    if (!confirm("確定提出申請?")) {
                        return false;
                    }
                    saveData();
                });

                $("#reset").click(function () {
                    resetFormObjs();
                });

                function saveData() {
                    var beginTimeString = $("#fini").val();
                    var endTimeString = $("#ffin").val();
                    var isDateLegal = checkRequestTime(beginTimeString, endTimeString);

                    if (isDateLegal) {

                        var obj = {
                            id: $("#userNo").val(),
                            beginTime: beginTimeString,
                            endTime: endTimeString,
                            leaveType: $("#leaveType").val(),
                            leaveReason: $("#leaveReason").val()
                        };

                        if (obj.id == null) {
                            return;
                        }

                        $("#serverMsg").html("");

                        if (checkVal(obj.beginTime, obj.endTime) == false || obj.leaveType == "-1") {
                            $("#serverMsg").html("Input value cannot be empty.");
                            return false;
                        }
                        if ($("#leaveReason").is(":visible") && obj.leaveReason == "-1") {
                            $("#serverMsg").html("Please select your leave reason.");
                            return false;
                        }

                        checkRequestDate(obj);

                    } else {
                        $("#serverMsg").html("Please recheck your leaveRequest time.");
                    }
                }

                function checkRequestTime(beginDate, endDate) {
                    return moment(beginDate, momentFormatString).isBefore(moment(endDate, momentFormatString));
                }

                function checkRequestDate(obj) {

                    var leaveType = $("#leaveType option:selected").text();
                    var userDepartment = $("#userDepartment").val();

                    if ((leaveType.indexOf("特休") != -1 || leaveType.indexOf("事假") != -1) && userDepartment != 4) {
                        checkPeopleAmountAndSaveLeaveRequest(obj);
                    } else {
                        saveLeaveRequest(obj);
                    }
                }

                function checkPeopleAmountAndSaveLeaveRequest(obj) {
                    $.ajax({
                        type: "Post",
                        url: "LimitLeaveRequestCheck",
                        dataType: 'json',
                        data: obj,
                        success: function (response) {
                            $("#serverMsg").html("");
                            var responseArr = response;
                            var j = responseArr.length;
                            if (j != 0) {
                                $("#serverMsg").append("<p>申請失敗，申請時段人數已達上限，如有問題請聯絡管理人員。</p>");
                                for (var i = 0; i < j; i++) {
                                    var jsonObj = responseArr[i];
                                    $("#serverMsg").append("<p>" +
                                            jsonObj.name + " " +
                                            jsonObj.leaveType + " " +
                                            jsonObj.remark + " " +
                                            jsonObj.leaveFrom + " - " +
                                            jsonObj.leaveTo +
                                            "</p>");
                                }
                            } else {
                                saveLeaveRequest(obj);
                            }
                        }
                    });
                }

                function saveLeaveRequest(obj) {
                    $.ajax({
                        type: "Post",
                        url: "LeaveReq",
                        dataType: 'html',
                        data: obj,
                        success: function (response) {
                            $("#serverMsg").html(response);
                            if (response == "true") {
                                $("#fini").html("");
                                $("#serverMsg").html("申請成功");
                                resetFormObjs();
                            } else {
                                $("#serverMsg").html(response);
                            }
                        }
                    });
                }

                function showReasonOptions() {
                    $("#remark").show();
                }

                function checkVal() {
                    for (var i = 0; i < arguments.length; i++) {
                        var arg = arguments[i];
                        if (arg == null || arg == "" || arg == 0) {
                            return false;
                        }
                    }
                    return true;
                }

                function resetFormObjs() {
                    $(":text,input[type='number']").val("");
                    $("select").val(-1);
                }
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <input type="hidden" value="${sessionScope.department}" id="userDepartment">
        <div id="wigetCtrl">
            <h3>請在下方選擇各項資訊後，送出完成請假申請。</h3>
            <hr />
            <div>
                <table id="leaveRequest" class="table">
                    <tr>
                        <td class="title">申請時間</td>
                        <td>
                            <div class="form-group form-inline">
                                日期 
                                <div class='input-group date' id='beginTime'>
                                    <input type="text" id="fini" placeholder="請選擇起始時間"> 
                                </div> 
                                <div class='input-group date' id='endTime'>
                                    <input type="text" id="ffin" placeholder="請選擇結束時間"> 
                                </div>
                                <select id="leaveType">
                                    <option value="-1">請選擇假種</option>
                                </select>
                                <input type="button" id="send" value="確定(Ok)">
                                <input type="button" id="reset" value="重設(Reset)">
                                <div id="serverMsg"></div>
                            </div>
                        </td>
                    </tr>
                    <tr id="remark" hidden>
                        <td>備註</td>
                        <td>
                            <div class="form-inline">
                                <select id="leaveReason">
                                    <option value="-1">請選擇事由</option>
                                </select>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <hr />
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
