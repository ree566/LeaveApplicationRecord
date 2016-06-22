<%-- 
    Document   : history
    Created on : 2016/2/23, 下午 05:02:12
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
        <style>
            #wigetCtrl{
                width: 95%;
                margin: 0px auto;
            }
            .alarm{
                color: red;
            }
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
        <script src="js/dataTables.fixedHeader.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="js/datepicker-zh-TW.js"></script>
        <script src="js/moment.js"></script>
        <script src="js/bootstrap-datetimepicker.min.js"></script>
        <script src="js/zh-tw.js"></script>
        <script>
            $.fn.dataTableExt.afnFiltering.push(
                    function (oSettings, aData, iDataIndex) {
                        //日期data目前統一
                        //2016-02-23 15:17:23.58
                        //https://www.datatables.net/plug-ins/filtering/row-based/range_dates   datatable date range plugin code
                        var iFini = parseDateValue($("#fini").val());
                        var iFfin = parseDateValue($("#ffin").val());

                        var iStartDateCol = 4;
                        var iEndDateCol = 5;
                        var datofini = parseDateValue(aData[iStartDateCol]);
                        var datoffin = parseDateValue(aData[iEndDateCol]);

                        if (iFini === "" || iFfin === "")
                        {
                            return true;
                        } else if (iFini <= datofini && iFfin === "")
                        {
                            return true;
                        } else if (iFfin >= datoffin && iFini === "")
                        {
                            return true;
                        }
//                        else if (iFini <= datofini && iFfin >= datoffin)
//                        {
//                            return true;
//                        }
                        else if ((datofini <= iFini && iFini <= datoffin) || (iFini <= datofini && datofini <= iFfin)) {
                            return true;
                        }
                        return false;
//                        :dt1 = datofini, :td2 = datoffin, dt1 = iFini,dt2 = iFfin
                    }
            );

            // Function for converting a yyyy/mm/dd date value into a numeric string for comparison (example 2010/08/12 becomes 20100812
            function parseDateValue(rawDate) {
                var fullDateArray = rawDate.split(" ");
                var dateArray = fullDateArray[0].split("-");
                var parsedDate = dateArray[0] + dateArray[1] + dateArray[2];
                return parsedDate;
            }

            $(document).ready(function () {

                var table = $("#data").DataTable({
                    "processing": false,
                    "serverSide": false,
                    "fixedHeader": true,
                    "ajax": {
                        "url": "GetLeaveRequestHistory",
                        "type": "Post",
                        "dataType": "json"
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "jobnumber"},
                        {data: "name"},
                        {data: "leaveHours"},
                        {data: "leaveFrom"},
                        {data: "leaveTo"},
                        {data: "leaveType"},
                        {data: "remark"},
                        {data: "saveTime"},
                        {data: "department"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "bLengthChange": true,
                    "order": [[8, "desc"]],
                    iDisplayLength: 30,
                    "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],
                    "columnDefs": [
                        {
                            "targets": [10],
                            "data": "reqByUser",
                            "render": function (data, type, full) {
                                return data == 1 ? "是" : "否";
                            }
                        }
                    ]
                });

                var fini = $('#fini').datetimepicker({
                    defaultDate: moment('01-01', 'MM-DD'),
                    locale: "zh-tw",
                    stepping: 30,
                    format: 'YYYY-MM-DD',
                    extraFormats: ['YYYY-MM-DD']
                });
                var ffin = $('#ffin').datetimepicker({
                    defaultDate: moment('12-31', 'MM-DD'),
                    locale: "zh-tw",
                    stepping: 30,
                    format: 'YYYY-MM-DD',
                    extraFormats: ['YYYY-MM-DD']
                });

                $("#checkAll").on("change", function () {
                    checkBoxGearing($(this));
                });

                $("body").on("dp.change", "#fini, #ffin", function () {
                    table.draw();
                });

                $('#data tbody').on('click', 'tr', function () {
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
        <jsp:include page="head.jsp" />
        <div id="wigetCtrl">
            <div class="container">
                <div class="row">
                    <div class="form-group form-inline">
                        Search time key: between
                        <div class='input-group date' id='beginTime'>
                            <input type="text" id="fini" class="form-control" placeholder="請選擇起始時間"> 
                        </div> to 
                        <div class='input-group date' id='beginTime'>
                            <input type="text" id="ffin" class="form-control" placeholder="請選擇結束時間">
                        </div>
                    </div>
                </div>
            </div>
            <table id="data" class="table table-striped">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>工號</th>
                        <th>名稱</th>
                        <th>時數(小時)</th>
                        <th>請假開始時間</th>
                        <th>請假結束時間</th>
                        <th>假種</th>
                        <th>事由</th>
                        <th>申請時間</th>
                        <th>人員種類</th>
                        <th>自己申請</th>
                    </tr>
                </thead>
            </table>
            <div id="serverMsg"></div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
