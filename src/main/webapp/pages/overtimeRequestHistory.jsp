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
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/fixedHeader.dataTables.min.css">
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

        <script>
            $(document).ready(function () {

// <th>id</th>
//                        <th>工號</th>
//                        <th>名稱</th>
//                        <th>時數(小時)</th>
//                        <th>人員種類</th>
//                        <th>儲存時間</th>
//                        <th>自己申請</th>
//                        <th>加班確認者</th>
//                        <th>加班確認時間</th>
                var table = $("#data").DataTable({
                    "bProcessing": true,
                    "fixedHeader": true,
                    "sAjaxSource": "../GetOvertimeRequest",
                    "columns": [
                        {data: "id", visible: false},
                        {data: "jobnumber"},
                        {data: "name"},
                        {data: "overtimeHours"},
                        {data: "department"},
                        {data: "saveTime"},
                        {data: "reqByUser"},
                        {data: "checkUserName"},
                        {data: "checkTime"}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "bLengthChange": true,
                    "sServerMethod": "POST",
//                    "pagingType": "scrolling",
//                    "order": [[8, "desc"]],
//                    iDisplayLength: 30,
//                    "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],

//                    "bServerSide": true,
                    "columnDefs": [
                        {
                            "targets": [6],
                            "data": "checkStatus",
                            "render": function (data, type, full) {
                                return (data == 1 ? "是" : "否");
                            }
                        },
                        {
                            "targets": [7,8],
                            "data": "checkStatus",
                            "render": function (data, type, full) {
                                return (data == null ? "無" : data);
                            }
                        }
                    ]
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
        <jsp:include page="../temp/header.jsp" />
        <div id="wigetCtrl">
            <table id="data" class="table table-striped">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>工號</th>
                        <th>名稱</th>
                        <th>時數(小時)</th>
                        <th>人員種類</th>
                        <th>儲存時間</th>
                        <th>自己申請</th>
                        <th>加班確認者</th>
                        <th>加班確認時間</th>
                    </tr>
                </thead>
            </table>
        </div>
        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
