<%-- 
    Document   : testpage
    Created on : 2016/3/14, 上午 11:21:49
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.css">
        <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
        <style>
            #form-wiget{
                margin: 0 auto;
                width: 95%;
            }
            .alert1{
                color: red;
            }
            .ui-datepicker-calendar {
                display: none;
            }
            .ui-datepicker {
                background: #333;
                border: 1px solid #555;
                color: #EEE;
            }
            .spec{
                background-color: wheat;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="../js/dataTables.fixedHeader.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/datepicker-zh-TW.js"></script>
        <script src="../js/moment.js"></script>
        <script src="../js/zh-tw.js"></script>
        <script src="../js/bootstrap-datetimepicker.min.js"></script>
        <script>
            $(function () {
                $(":text,input[type='number']").addClass("form-control");
                $(":button").addClass("btn btn-default");
                $('.date-picker').datetimepicker({
                    defaultDate: moment(),
                    locale: "zh-tw",
                    format: 'YYYY-MM'
                });
                var table;
                $("#send").click(function () {
                    $("#showHide").prop("checked", false);
                    var startDate = $("#startDate").val();
                    var endDate = $("#endDate").val();
                    table = $("#data").DataTable({
                        "processing": true,
                        "serverSide": false,
                        "fixedHeader": true,
                        "ajax": {
                            "type": "Post",
                            "url": "../GetLeaveRequest",
                            "dataType": "json",
                            data: {
                                "startDate": startDate,
                                "endDate": endDate
                            }
                        },
                        "columns": [
                            {data: "userNo","visible": false},
                            {data: "jobnumber",width: "60px"},
                            {data: "name",width: "60px"},
                            {data: "personalLeave"},
                            {data: "sickLeave"},
                            {data: "maternityLeave", "visible": false},
                            {data: "funeralLeave", "visible": false},
                            {data: "patermityLeave", "visible": false},
                            {data: "officalLeave", "visible": false},
                            {data: "occpationalSicknessLeave", "visible": false},
                            {data: "optionalLeave", "visible": false},
                            {data: "marriageLeave", "visible": false},
                            {data: "menstruationLeave", "visible": false},
                            {data: "preMaternityLeave", "visible": false},
                            {data: 0},
                            {data: "annualLeave"}
                        ],
                        "oLanguage": {
                            "sLengthMenu": "顯示 _MENU_ 筆記錄",
                            "sZeroRecords": "無符合資料",
                            "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                        },
                        bAutoWidth: true,
                        "bLengthChange": true,
                        "order": [[1, "asc"]],
                        destroy: true,
                        iDisplayLength: 30,
                        "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],
                        "columnDefs": [
                            {
                                "render": function (data, type, row) {
                                    if (data != 0) {
                                        return "<strong class='alert1'>" + data + "</strong>";
                                    }
                                    return data;
                                },
                                "targets": [3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15]
                            },
                            {
                                "render": function (data, type, row) {
                                    var sum = row.maternityLeave + row.funeralLeave + row.patermityLeave + row.officalLeave +
                                            row.occpationalSicknessLeave + row.optionalLeave + row.marriageLeave +
                                            row.menstruationLeave + row.preMaternityLeave;
                                    return "<strong " + (sum != 0 ? "class='alert1'" : "") + ">" + sum + "</strong>";
                                },
                                "targets": [14]
                            }
                        ]
                    });
                });

                $('#showHide').on('click', function (e) {
//                    e.preventDefault();
                    var columns = [5, 6, 7, 8, 9, 10, 11, 12, 13];
                    var replaceColumns = [14];
                    if ($(this).is(":checked")) {
                        table.columns(columns).visible(true, true);
                        table.columns(replaceColumns).visible(false, false);
                    } else {
                        table.columns(columns).visible(false, false);
                        table.columns(replaceColumns).visible(true, true);
                    }
                    table.columns.adjust().draw(false);
                });

                $("body").on('click', '#data tbody tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        table.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });
            });


        </script>
    </head>
    <body>
        <jsp:include page="../temp/header.jsp" />
            <div id="form-wiget">

                <label for="showHide">顯示特別假:</label>
                <input type="checkbox" id="showHide">

                <div class="form-inline">
                    <label for="startDate">起始時間:</label>
                    <div class='input-group date' id='beginTime'>
                        <input name="startDate" id="startDate" class="date-picker" />
                    </div> 

                    <label for="startDate">結束時間:</label>
                    <div class='input-group date' id='endTime'>
                        <input name="startDate" id="endDate" class="date-picker" />
                    </div> 

                    <input type="button" id="send" value="send">
                </div>
                <table id="data" class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th hidden>id</th>
                            <th>工號</th>
                            <th>名稱</th>
                            <th>事假</th>
                            <th>病假</th>
                            <th>產假</th>
                            <th>喪假</th>
                            <th>陪產假</th>
                            <th>公假</th>
                            <th>公傷假</th>
                            <th>彈性休假</th>
                            <th>婚假</th>
                            <th>生理假</th>
                            <th>產前假</th>
                            <th>特別假加總</th>
                            <th>特休</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th hidden>id</th>
                            <th>工號</th>
                            <th>名稱</th>
                            <th>事假</th>
                            <th>病假</th>
                            <th>產假</th>
                            <th>喪假</th>
                            <th>陪產假</th>
                            <th>公假</th>
                            <th>公傷假</th>
                            <th>彈性休假</th>
                            <th>婚假</th>
                            <th>生理假</th>
                            <th>產前假</th>
                            <th>特別假加總</th>
                            <th>特休</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
