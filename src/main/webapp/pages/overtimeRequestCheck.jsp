<%-- 
    Document   : overtimeRequestCheck
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
        <link rel="stylesheet" href="../css/serverMessage.css">
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
        <script src="../js/serverMessage.js"></script>

        <script>
            $(document).ready(function () {
                $("input").addClass("form-control");

                var table = $("#data").DataTable({
                    "processing": true,
                    "serverSide": false,
                    "fixedHeader": true,
                    "ajax": {
                        "url": "../GetOvertimeRequest",
                        "type": "Post",
                        "dataType": "json"
                    },
                    "columns": [
                        {data: "id", visible: false},
                        {data: "jobnumber"},
                        {data: "name"},
                        {data: "overtimeHours"},
                        {data: "department"},
                        {data: "saveTime"},
                        {}
                    ],
                    "oLanguage": {
                        "sLengthMenu": "顯示 _MENU_ 筆記錄",
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    "bLengthChange": true,
//                    "order": [[8, "desc"]],
//                    iDisplayLength: 30,
//                    "aLengthMenu": [[5, 10, 30, 50, -1], [5, 10, 30, 50, 'All']],
                    "columnDefs": [
                        {
                            "targets": [6],
                            "data": "checkStatus",
                            "render": function (data, type, full) {
                                var id = full.id;
                                return "<input type='checkbox' class='memberCheck' value=" + id + " /><font class='checkText'>" +
                                        (data == 1 ? "已" : "未") + "確認" +
                                        "</font>";
                            },
                            "orderable": false
                        }
                    ],
                    "fnDrawCallback": function () {
                        $(".memberCheck").change(function () {
                            $(this).next().text((this.checked ? "已" : "未") + "確認");
                            $("#checkAll").prop("checked", ($(".memberCheck:checked").length == $(".memberCheck").length));
                        });

                        $("#checkAll").click(function () {
                            $(".memberCheck").prop("checked", this.checked).trigger("change");
//                            $(this).next().text(($(this).is(":checked") ? "已" : "未") + "確認");
                        });
                    }
                });

                $("#confirm").click(function () {
                    if (confirm("確定加班資料無誤?(儲存之後無法再做修改)")) {
                        var checkId = $(".memberCheck:checked").map(function () {
                            return $(this).val();
                        }).get();
//                        var arr = [1,2,3,4,5,6];
                        console.log(checkId);
                        $.ajax({
                            type: "Post",
                            url: "../CheckOvertimeRequest",
                            dataType: 'json',
                            data: {
                                id: checkId
                            },
                            success: function (response) {
                                showServerMsg(response);
                            }, 
                            complete: function (d) {
                                table.ajax.reload();//reload因為非同步只有在這裡會有作用
                            }
                        });
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
                        <th class="form-inline"><input type='checkbox' id="checkAll" /><label for="checkAll">狀態</label></th>
                    </tr>
                </thead>
                <tfoot>
                    <tr>
                        <th>id</th>
                        <th>工號</th>
                        <th>名稱</th>
                        <th>時數(小時)</th>
                        <th>人員種類</th>
                        <th>儲存時間</th>
                        <th><input type='button' id="confirm" value="儲存" /></th>
                    </tr>
                </tfoot>
            </table>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
