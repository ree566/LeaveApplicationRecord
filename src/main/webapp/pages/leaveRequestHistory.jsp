<%-- 
    Document   : history
    Created on : 2016/2/23, 下午 05:02:12
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="isAdmin" value="${sessionScope.permission >= initParam.SYTEM_MANAGER_PERMISSION ? true: false}" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/fixedHeader.dataTables.min.css">
        <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
        <style>
            #wigetCtrl{
                width: 95%;
                margin: 0px auto;
            }
            .alarm{
                color: red;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="../js/dataTables.fixedHeader.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/datepicker-zh-TW.js"></script>
        <script src="../js/moment.js"></script>
        <script src="../js/bootstrap-datetimepicker.min.js"></script>
        <script src="../js/zh-tw.js"></script>
        <script>
            var table;
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
                        } else if ((datofini <= iFini && iFini <= datoffin) || (iFini <= datofini && datofini <= iFfin)) {
                            return true;
                        }
                        return false;
                    }
            );

            // Function for converting a yyyy/mm/dd date value into a numeric string for comparison (example 2010/08/12 becomes 20100812
            function parseDateValue(rawDate) {
                var fullDateArray = rawDate.split(" ");
                var dateArray = fullDateArray[0].split("-");
                var parsedDate = dateArray[0] + dateArray[1] + dateArray[2];
                return parsedDate;
            }

            function getHistory() {
                table = $("#data").DataTable({
                    "processing": true,
                    "serverSide": false,
                    "fixedHeader": true,
                    "ajax": {
                        "url": "../GetLeaveRequestHistory",
                        "type": "Post",
                        "dataType": "json",
                        data: {
                            startDate: $('#fini').val(),
                            endDate: $('#ffin').val()
                        }
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
                    bDestroy: true,
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
            }

            $(document).ready(function () {

                var fini = $('#fini').datetimepicker({
                    defaultDate: moment().startOf('isoWeek'),
                    locale: "zh-tw",
                    stepping: 30,
                    format: 'YYYY-MM-DD',
                    extraFormats: ['YYYY-MM-DD']
                });
                var ffin = $('#ffin').datetimepicker({
                    defaultDate: ${isAdmin} ? moment().endOf('isoWeek') : moment('12-31', 'MM-DD'),
                    locale: "zh-tw",
                    stepping: 30,
                    format: 'YYYY-MM-DD',
                    extraFormats: ['YYYY-MM-DD']
                });

                getHistory();

                $("#checkAll").on("change", function () {
                    checkBoxGearing($(this));
                });

                $("#research").hide();

                $("body").on("dp.change", "#fini, #ffin", function () {
                    $("#research").show();
                });

                $('#data tbody').on('click', 'tr', function () {
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        table.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                });

                $("#research").click(function () {
                    getHistory();
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="../temp/header.jsp" />
        <c:if test="${isAdmin}">
            <div>
                <h5 class="alarm">※僅會顯示一周內請假紀錄，如搜尋結果為空時請擴大搜尋時間範圍</h5>
            </div>
        </c:if>
        <div id="wigetCtrl">
            <div class="container">
                <div class="row">
                    <div class="form-group form-inline">
                        <label>Search time key: between</label>
                        <div class='input-group date' id='beginTime'>
                            <input type="text" id="fini" class="form-control" placeholder="請選擇起始時間"> 
                        </div> 
                        <label>to</label>
                        <div class='input-group date' id='beginTime'>
                            <input type="text" id="ffin" class="form-control" placeholder="請選擇結束時間">
                        </div>
                        <button class="form-control" id="research"><span class="glyphicon glyphicon-refresh"></span>重新查詢</button>
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

        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
