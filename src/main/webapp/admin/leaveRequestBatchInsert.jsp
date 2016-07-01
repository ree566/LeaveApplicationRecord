<%-- 
    Document   : adminInsert
    Created on : 2016/2/25, 上午 10:25:18
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
        <style>
            #wigetCtrl{
                width: 98%;
                margin: 0px auto;
            }
            .alarm{
                color: red;
            }
            #floatWiget{
                position: fixed;
                right: 20px;
                bottom: 20px;    
                padding: 10px 15px;    
                font-size: 20px;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="../js/datepicker-zh-TW.js"></script>
        <script src="../js/moment.js"></script>
        <script src="../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../js/zh-tw.js"></script>
        <script src="../js/jquery.blockUI.js"></script>
        <script src="../js/jquery.blockUI.Default.js"></script>
        <script src="../js/jquery.dataTable.domSort.js"></script>
        <script src="../js/dropDownList.js"></script>
        <script>

            $(document).ready(function () {
                initDropdownList();

                var timepickerObj;

                var table = $("#data").dataTable({
                    "processing": true,
                    "serverSide": false,
                    "bLengthChange": false,
                    "bPaginate": false,
                    "bInfo": false,
                    "ajax": {
                        "url": "../AllUser",
                        "type": "Post",
                        "dataType": "json"
                    },
                    "columns": [
                        {data: "id", width: "50px"},
                        {data: "jobnumber", width: "50px"},
                        {data: "name", width: "50px"},
                        {width: "140px"},
                        {width: "140px"},
                        {width: "140px"},
                        {width: "140px"}
                    ],
                    "oLanguage": {
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    iDisplayLength: -1,
                    "columnDefs": [
                        {
                            "targets": [0],
                            "data": "id",
                            "render": function (data, type, full) {
                                return "<input type='hidden' class='userNo' value=" + data + ">" + data;
                            }
                        }, {
                            "targets": [3],
                            "render": function (data, type, full) {
                                return "<div class='row'><div class='col-sm-6'><input type='text' class='datepicker startDate' placeholder='請輸入開始日期'></div></div>";
                            }
                        },
                        {
                            "targets": [4],
                            "render": function (data, type, full) {
                                return "<div class='row'><div class='col-sm-6'><input type='text' class='datepicker endDate' placeholder='請輸入結束日期'></div></div>";
                            }
                        },
                        {
                            "targets": [5],
                            "data": "typeNo",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList($(".leaveType:first"), data);
                            }
                        },
                        {
                            "targets": [6],
                            "data": "reasonNo",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList($(".leaveReason:first"), data);
                            }
                        }
                    ],
                    order: [[1, 'asc']],
                    "fnDrawCallback": function () {
                        $("select,input[type='number'],:text").addClass("form-control");
                        timepickerObj = $('.datepicker').datetimepicker({
                            defaultDate: moment({hour: 8, minute: 30}),
                            locale: "zh-tw",
                            stepping: 30,
                            format: 'YYYY-MM-DD HH:mm',
                            extraFormats: ['YYYY-MM-DD HH:mm'],
                            sideBySide: true
                        });
                        timepickerObj.on("dp.change", function () {
                            var col = $(this).parents("tr");
                            appendNameWhenChange(col);
                        });
                    }
                });

                $("#requestList").on("submit", function () {
                    block();
                    if ($(".danger").length != 0) {
                        $.unblockUI();
                        alert("請假日期重疊，請重新再次確認(列的背景為紅色的欄位)。");
                        return false;
                    }
                });

                $("body").on("change", ".leaveType", function () {
                    var sel = $(this).val();
                    if (sel == -1) {
                        $(this).parents("tr").removeClass("danger");
                    } else {
                        var col = $(this).parents("tr");
                        var userNo = col.find(".userNo").val();
                        var beginTime = col.find(".startDate").val();
                        var endTime = col.find(".endDate").val();
                        checkRequestDate(col, userNo, beginTime, endTime);
                    }
                });

                function appendNameWhenChange(col) {
                    col.addClass("alarm");
                    col.find(".userNo").attr("name", "userNo");
                    col.find(".startDate").attr("name", "startDate");
                    col.find(".endDate").attr("name", "endDate");
                    col.find(".leaveType").attr("name", "leaveType");
                    col.find(".leaveReason").attr("name", "leaveReason");
                }

                function checkRequestDate(object, userNo, beginTime, endTime) {
                    if (userNo == null || beginTime == "" || endTime == "") {
                        object.removeClass("danger");
                        return;
                    }
                    $.ajax({
                        type: "Post",
                        url: "../LeaveRequestDulpCheck",
                        dataType: 'json',
                        data: {
                            userNo: userNo,
                            beginTime: beginTime,
                            endTime: endTime
                        },
                        success: function (response) {
                            console.log(response);
                            if (response == true) {
                                object.addClass("danger");
                            } else {
                                object.removeClass("danger");
                            }
                        }
                    });
                }
            });
        </script>
    </head>
    <body>
        <jsp:include page="../temp/header.jsp" />

        <div id="selectObj" hidden>
            <select class="form-control leaveType">
                <option value="-1">請選擇假種</option>
            </select>
            <select class="form-control leaveReason">
                <option value="-1">請選擇事由</option>
            </select>
        </div>
        <div id="wigetCtrl">
            <form id="requestList" action="../InsertLeaveReq" method="post">
                <div>
                    <table id="data" class="table table-striped table-hover table-condensed form-inline">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>工號</th>
                                <th>名稱</th>
                                <th>請假開始時間</th>
                                <th>請假結束時間</th>
                                <th>假種</th>
                                <th>事由</th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th>id</th>
                                <th>工號</th>
                                <th>名稱</th>
                                <th>請假開始時間</th>
                                <th>請假結束時間</th>
                                <th>假種</th>
                                <th>事由</th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
                <hr />
                <div id="floatWiget">
                    <div id="serverMsg"></div>
                    <input type="submit" id="saveAll" class="btn btn-primary" value="儲存" onclick="return(confirm('確定儲存?'))">
                </div>
            </form>
        </div>
        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
