<%-- 
    Document   : adinAlter
    Created on : 2016/2/23, 下午 05:02:12
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="../css/serverMessage.css">
        <style>
            #wigetCtrl{
                width: 98%;
                margin: 0px auto;
            }
            .alarm{
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/datepicker-zh-TW.js"></script>
        <script src="../js/moment.js"></script>
        <script src="../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../js/zh-tw.js"></script>
        <script src="../js/jquery.blockUI.js"></script>
        <script src="../js/jquery.blockUI.Default.js"></script>
        <script src="../js/jquery.dataTable.domSort.js"></script>
        <script src="dropDownList.js"></script>
        <script src="../js/serverMessage.js"></script>
        <script>

            $(document).ready(function () {

                //Initialize the dropDownList and clone into the jquery datatable.
                initDropdownList();

                //-----------------

                var table = $("#data").DataTable({
                    "processing": true,
                    "serverSide": false,
                    "ajax": {
                        "url": "../GetLeaveRequestHistory",
                        "type": "Post",
                        dataType: "json"
                    },
                    "columns": [
                        {data: "jobnumber", width: "70px"},
                        {data: "name", width: "60px"},
                        {data: "leaveHours", width: "50px"},
                        {data: "leaveFrom", "orderDataType": "dom-text", width: "110px"},
                        {data: "leaveTo", "orderDataType": "dom-text", width: "110px"},
                        {data: "typeNo", "orderDataType": "dom-select", width: "110px"},
                        {data: "reasonNo", "orderDataType": "dom-select", width: "110px"},
                        {data: "saveTime", width: "100px"},
                        {data: "id", width: "100px"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "bLengthChange": true,
                    "order": [[7, "desc"]],
                    iDisplayLength: 30,
                    "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],
                    "columnDefs": [
                        {
                            "targets": [3],
                            "data": "leaveFrom",
                            "render": function (data, type, full) {
                                return "<div class='row'><div class='col-sm-10'><input type='text' class='datepicker leaveFrom' value='" + data + "'></div></div>";
                            }
                        },
                        {
                            "targets": [4],
                            "data": "leaveTo",
                            "render": function (data, type, full) {
                                return "<div class='row'><div class='col-sm-10'><input type='text' class='datepicker leaveTo' value='" + data + "'></div></div>";
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
                        },
                        {
                            "targets": [8],
                            "orderable": false,
                            "data": "id",
                            "render": function (data, type, full) {
                                return "<div class='form-inline'>" +
                                        "<input type='hidden' class='id' value='" + data + "'>" +
                                        "<input type='button' class='save' value='儲存' disabled />" +
                                        "<input type='button' class='delete' value='刪除' /></div>";
//                                
                            }
                        }
                    ],
                    "fnDrawCallback": function () {
                        $("input").addClass("form-control");
                        var timepickerObj = $('.datepicker').datetimepicker({
                            locale: "zh-tw",
                            stepping: 30,
                            format: 'YYYY-MM-DD HH:mm',
                            extraFormats: ['YYYY-MM-DD HH:mm'],
                            sideBySide: true
                        });

                        timepickerObj.on("dp.change", function () {
                            var col = $(this).parents("tr");
                            col.find(".save").attr("disabled", false);
                        });

                        $(".delete").click(function () {
                            if (!confirm("確定刪除?")) {
                                return false;
                            }
                            deleteData($(this).parent().find(".id").val());
                        });

                        $(".save").click(function () {
                            if (!confirm("確定修改?")) {
                                return false;
                            }
                            var col = $(this).parents("tr");
                            var data = {
                                id: col.find(".id").val(),
                                leaveType: col.find(".leaveType").val(),
                                leaveFrom: col.find(".leaveFrom").val(),
                                leaveTo: col.find(".leaveTo").val(),
                                leaveReason: col.find(".leaveReason").val()
                            };
                            saveData(data);
                        });
                    }
                });

                $("body").on("change", "select, .leaveType, .leaveReason", function () {
                    var col = $(this).parents("tr");
                    col.addClass("alarm");
                    col.find(".save").attr("disabled", false);
                });

                function saveData(obj) {
                    $.ajax({
                        url: "../UpdateLeaveReq",
                        method: 'POST',
                        dataType: 'json',
                        data: obj,
                        success: function (d) {
                            showServerMsg(d);
                        },
                        complete: function (d) {
                            table.ajax.reload();//reload因為非同步只有在這裡會有作用
                        }
                    });
                }

                function deleteData(id) {
                    $.ajax({
                        url: "../DeleteLeaveReq",
                        method: 'POST',
                        dataType: 'json',
                        data: {
                            id: id
                        },
                        success: function (d) {
                            showServerMsg(d);
                        },
                        complete: function (d) {
                            table.ajax.reload();//reload因為非同步只有在這裡會有作用
                        }
                    });
                }
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />

        <div id="selectObj" hidden>
            <select class="form-control leaveType">
                <option value="-1">請選擇假種</option>
            </select>
            <select class="form-control leaveReason">
                <option value="-1">請選擇事由</option>
            </select>
        </div>

        <div id="wigetCtrl">
            <h5 class="alarm">※假種和事由下拉式沒有到定位時，請試著重新整理。</h5>
            <table id="data" class="table table-condensed">
                <thead>
                    <tr>
                        <th>工號</th>
                        <th>名稱</th>
                        <th>時數(H)</th>
                        <th>開始時間</th>
                        <th>結束時間</th>
                        <th>假種</th>
                        <th>事由</th>
                        <th>申請時間</th>
                        <th>動作</th>
                    </tr>
                </thead>
                <tfoot>
                    <tr>
                        <th>工號</th>
                        <th>名稱</th>
                        <th>時數(H)</th>
                        <th>開始時間</th>
                        <th>結束時間</th>
                        <th>假種</th>
                        <th>事由</th>
                        <th>申請時間</th>
                        <th>動作</th>
                    </tr>
                </tfoot>
            </table>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
