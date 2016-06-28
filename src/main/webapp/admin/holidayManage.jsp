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
                width: 90%;
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
        <script src="../js/dropDownList.js"></script>
        <script src="../js/serverMessage.js"></script>
        <script>
            $(document).ready(function () {
                var table = $("#data").DataTable({
                    "processing": true,
                    "serverSide": false,
                    "bLengthChange": false,
                    "bPaginate": false,
                    "bInfo": false,
                    "ajax": {
                        "url": "../GetHoliday",
                        "type": "Post",
                        dataType: "json"
                    },
                    "columns": [
                        {data: "id"},
                        {data: "name"},
                        {data: "dateFrom"},
                        {data: "dateTo"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    iDisplayLength: 30,
                    "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],
                    "columnDefs": [
                        {
                            "targets": [0],
                            "data": "name",
                            "render": function (data, type, full) {
                                return "<input type='hidden' class='id' value='" + data + "'>" + data;
                            }
                        },
                        {
                            "targets": [1],
                            "data": "name",
                            "render": function (data, type, full) {
                                return "<input type='text' class='name' placeholder='請輸入假期名稱' value='" + data + "'>";
                            }
                        },
                        {
                            "targets": [2],
                            "data": "dateFrom",
                            "render": function (data, type, full) {
                                return "<div class='row'>" +
                                        "<div class='col-sm-6'><input type='text' value='" + data + "' class='datepicker startDate' placeholder='請輸入日期'></div>" +
                                        "</div>";
                            }
                        },
                        {
                            "targets": [3],
                            "data": "dateTo",
                            "render": function (data, type, full) {
                                return "<div class='row'>" +
                                        "<div class='col-sm-6'><input type='text' value='" + data + "' class='datepicker endDate' placeholder='請輸入日期'></div>" +
                                        "</div>";
                            }
                        },
                        {
                            "targets": [4],
                            "render": function (data, type, full) {
                                return "<div class='form-inline'><input type='button' class='alter' value='儲存'><input type='button' class='delete' value='刪除'></div>";
                            }
                        }
                    ],
                    "fnDrawCallback": function () {
                        $("input").addClass("form-control");
                        $(":button").addClass("btn btn-default");
                        $('.datepicker').datetimepicker({
                            defaultDate: $(this).val(),
                            locale: "zh-tw",
                            stepping: 30,
                            format: 'YYYY-MM-DD',
                            extraFormats: ['YYYY-MM-DD']
                        });
                    }
                });
                $("#send").click(function () {
                    if (!confirm("確定新增?")) {
                        return false;
                    }

                    var form = $(this).parent();
                    var name = form.children().eq(0).val();
                    var dateFrom = form.children().eq(1).val();
                    var dateTo = form.children().eq(2).val();

                    if (!checkVal(name, dateFrom, dateTo)) {
                        showServerMsg(false);
                        return false;
                    }

                    var datas = {
                        name: name,
                        dateFrom: dateFrom,
                        dateTo: dateTo
                    };
                    saveHoliday("insert", datas);
                });

                $("body").on("click", ".alter", function () {
                    if (!confirm("確定修改?")) {
                        return false;
                    }

                    var form = $(this).parents("tr");
                    var id = form.find(".id").val();
                    var name = form.find(".name").val();
                    var dateFrom = form.find(".startDate").val();
                    var dateTo = form.find(".endDate").val();
                    if (!checkVal(name, dateFrom, dateTo)) {
                        showServerMsg(false);
                        return false;
                    }
                    var datas = {
                        id: id,
                        name: name,
                        dateFrom: dateFrom,
                        dateTo: dateTo
                    };

                    saveHoliday("update", datas);
                });

                $("body").on("click", ".delete", function () {
                    if (!confirm("確定刪除?")) {
                        return false;
                    }
                    var form = $(this).parents("tr");
                    var datas = {
                        id: form.find(".id").val()
                    };
                    saveHoliday("delete", datas);
                });


                function saveHoliday(action, datas) {
                    datas.action = action;
                    $.ajax({
                        type: "Post",
                        url: "../HolidayManage",
                        dataType: 'html',
                        data: datas,
                        success: function (d) {
                            if (d) {
                                $("#addSpecialDay :text").val("");
                            }
                            showServerMsg(d);
                        },
                        complete: function (d) {
                            table.ajax.reload();//reload因為非同步只有在這裡會有作用
                        }
                    });
                }

                function checkVal() {
                    for (var i = 0; i < arguments.length; i++) {
                        var arg = arguments[i];
                        if (arg == null || arg == "") {
                            return false;
                        }
                    }
                    return true;
                }
            });

        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <div id="wigetCtrl">
            <table id="data" class="table table-condensed">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>假期名稱</th>
                        <th>起始時間</th>
                        <th>結束時間</th>
                        <th>動作</th>
                    </tr>
                </thead>
            </table>
            <div id="addSpecialDay" class="form-inline">
                <div class='row'>
                    <div class='col-lg-12'>
                        <input type="text" placeholder="請輸入假期名稱">
                        <input type="text" class="datepicker" placeholder="請輸入起始時間">
                        <input type="text" class="datepicker" placeholder="請輸入結束時間">
                        <input type="button" id="send" value="確定">
                    </div>
                </div>
            </div>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
