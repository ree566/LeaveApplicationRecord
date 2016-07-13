<%-- 
    Document   : adinAlter
    Created on : 2016/2/23, 下午 05:02:12
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="identit" class="com.advantech.model.IdentitDAO" scope="application" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="../css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="../css/fixedHeader.dataTables.min.css">
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
            table a{
                cursor: pointer;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../js/jquery.validation.min.js"></script> 
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="../js/dataTables.fixedHeader.min.js"></script>
        <script src="../js/jquery.dataTables.columnFilter.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/jquery.blockUI.js"></script>
        <script src="../js/jquery.blockUI.Default.js"></script>
        <script src="../js/jquery.dataTable.domSort.js"></script>
        <script src="../js/dropDownList.js"></script>
        <!--<script src="../js/serverMessage.js"></script>-->
        <script>
            $(function () {

                var department = $(".department:first").clone().removeAttr("name");
                var sitefloor = $(".sitefloor:first").clone().removeAttr("name");
                var permission = $(".permission:first").clone().removeAttr("name");
                var lineType = $(".lineType:first").clone().removeAttr("name");

                var userTable = $("#userInfo").DataTable({
                    "ajax": {
                        "url": "../AllUser",
                        "type": "Post",
                        "dataType": "json"
                    },
                    "columns": [
                        {data: "id"},
                        {data: "jobnumber", width: "80px"},
                        {width: "100px"},
                        {width: "100px", type: "text"},
                        {type: "select"},
                        {width: "100px"},
                        {width: "100px"},
                        {width: "100px", type: "text"},
                        {}
                    ],
                    "columnDefs": [
                        {"searchable": false, "targets": [2]},
                        {
                            "targets": [0],
                            "orderable": false,
                            "data": "id",
                            "render": function (data, type, full) {
                                return "<input type='hidden' class='userid' value=" + data + ">" + data;
                            }
                        },
                        {
                            "targets": [2],
                            "orderable": false,
                            "data": "password",
                            "render": function (data, type, full) {
                                return "<input type='password' class='password' value='" + data + "' />";
                            }
                        },
                        {
                            "targets": [3],
                            "orderable": false,
                            "data": "name",
                            "render": function (data, type, full) {
                                return "<input type='text' class='name' value='" + data + "' />";
                            }
                        },
                        {
                            "targets": [4],
                            "orderable": false,
                            "data": "lineType",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(lineType, data);
                            }
                        },
                        {
                            "targets": [5],
                            "data": "department",
                            "orderable": false,
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(department, data);
                            }
                        },
                        {
                            "targets": [6],
                            "data": "permission",
                            "orderable": false,
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(permission, data);
                            }
                        },
                        {
                            "targets": [7],
                            "data": "sitefloor",
                            "orderable": false,
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(sitefloor, data);
                            }
                        },
                        {
                            "targets": [8],
                            "data": "email",
                            "orderable": false,
                            "render": function (data, type, full) {
                                return "<input type='text' class='email' value='" + (data == null ? '' : data) + "' />";
                            }
                        },
                        {
                            "targets": [9],
                            "render": function (data, type, full) {
                                return "<div class='form-inline'>" +
                                        "<input type='button' class='save' value='儲存' disabled />" +
                                        "<input type='button' class='delete' value='刪除' />" +
                                        "</div>";
                            }
                        }
                    ],
                    "oLanguage": {
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    "processing": true,
                    "serverSide": false,
                    "fixedHeader": true,
                    "bLengthChange": false,
                    "stateSave": true,
//                    "bPaginate": false,
//                    "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                    "iDisplayLength": 20,
                    "bInfo": true,
                    bAutoWidth: true,
                    order: [[1, 'asc']],
                    "fnDrawCallback": function () {
                        $(":text,input,select").addClass("form-control");
                        $(":button").addClass("btn btn-default");
                        $(".password").focus(function () {
                            this.type = "text";
                        }).blur(function () {
                            this.type = "password";
                        });
                        $("#userInfo").find(":password,:text,input[type='number'],select").change(function () {
                            var parents = $(this).parents("tr");
                            parents.addClass("danger");
                            parents.find(".save").attr("disabled", false);
                        });

                        $(".save").click(function () {
                            if (!confirm("確定儲存?")) {
                                return false;
                            }
                            var parents = $(this).parents("tr");
                            var rowObj = userTable.row(parents).data();

                            var psw = parents.find(".password").val();
                            var name = parents.find(".name").val();
                            var lineType = parents.find(".lineType").val();
                            var department = parents.find(".department").val();
                            var permission = parents.find(".permission").val();
                            var sitefloor = parents.find(".sitefloor").val();
                            var email = parents.find(".email").val();

                            var jobnumber = parents.children().eq(1).html();

                            if ($("#userJobnumber").val() == jobnumber && permission < $("#userPermission").val()) {
                                if (!confirm("修改自己的權限到較低權限，確定?")) {
                                    return false;
                                }
                            }

                            if (checkVal(psw, name, department, permission, sitefloor)) {
                                $.ajax({
                                    url: "../UserManage",
                                    method: 'POST',
                                    dataType: 'json',
                                    data: {
                                        action: "update",
                                        id: rowObj.id,
                                        password: psw,
                                        name: name,
                                        lineType: lineType,
                                        department: department,
                                        permission: permission,
                                        sitefloor: sitefloor,
                                        email: email
                                    },
                                    success: function (d) {
                                        $(parents).removeClass("danger");
                                        $(this).attr("disabled", true);
                                        var msg = d.errorMessage;
                                        showServerMsg(msg);
                                    }
                                });
                            }
                        });
                        $(".delete").click(function () {
                            if (!confirm("確定刪除人員?\n該人員的下列資料表紀錄將會一併刪除\n-- 請假申請\n-- 加班申請")) {
                                return false;
                            }
                            var parents = $(this).parents("tr");
                            var rowObj = userTable.row(parents).data();
                            $.ajax({
                                url: "../UserManage",
                                method: 'POST',
                                dataType: 'json',
                                data: {
                                    action: "delete",
                                    id: rowObj.id
                                },
                                success: function (d) {
                                    $(parents).removeClass("danger");
                                    $(this).attr("disabled", true);
                                    var msg = d.errorMessage;
                                    showServerMsg(msg);
                                },
                                complete: function (d) {
                                    userTable.ajax.reload();//reload因為非同步只有在這裡會有作用
                                }
                            });
                        });
                    }
                });

                $.validator.addMethod("valueNotEquals", function (elementValue, element, param) {
                    return elementValue != param;
                }, "Value cannot be {0}");

                $("#addUser").click(function () {
                    if (!confirm("確定新增人員?")) {
                        return false;
                    }

                    $("#newUser").validate({
                        rules: {
                            "name": {
                                required: true
                            },
                            "jobnumber": {
                                required: true
                            },
                            "email": {
                                required: true,
                                email: true
                            },
                            "password": {
                                required: true,
                                minlength: 5
                            },
                            "lineType": {
                                required: true
                            },
                            "permission": {
                                required: true
                            },
                            "sitefloor": {
                                required: true
                            }
                        },
                        messages: {
                            "lineType": {
                                valueNotEquals: "Please select an item!"
                            },
                            "permission": {
                                valueNotEquals: "Please select an item!"
                            },
                            "sitefloor": {
                                valueNotEquals: "Please select an item!"
                            }
                        },
                        debug: true,
                        submitHandler: function (form) {
                            $.post('../UserManage', $('#newUser').serialize(), function (d) {
                                var msg = d.errorMessage;
                                var result = d.result;
                                console.log(msg);
                                console.log(result);
                                showServerMsg(msg);
                                if (result) {
                                    userTable.ajax.reload();
                                    $("#myModal .close").click();
                                    $("#reset").trigger("click");

                                }
                            });
                        }
                    });
                });

                $("#addTestVal").click(function () {
                    $("#newUser").find(":text,:password").val("test6");
                    $("#newUser").find(".email").val("test6@advantech.com.tw");
                    $("#newUser select").each(function () {
                        $(this).find("option").eq(3).prop('selected', true);
                    });
                });

                function checkVal() {
                    for (var i = 0; i < arguments.length; i++) {
                        var arg = arguments[i];
                        if (arg == null || arg == "" || arg == -1) {
                            return false;
                        }
                    }
                    return true;
                }

                function showServerMsg(msg) {
                    $("#floatWiget").show();
                    if (msg == true || msg == "") {
                        $("#floatWiget").html("<div id='message' style='background-color:green'>儲存成功</div>");
                    } else if (msg == false || msg != "") {
                        $("#floatWiget").html("<div id='message' style='background-color:red'>" + (msg == false ? "發生問題，請聯絡管理員。" : msg) + "</div>");
                    }
                    setTimeout(function () {
                        $('#message').fadeOut(function () {
                            $(this).remove();
                            $("#floatWiget").hide();
                        });
                    }, 5000);
                }
            });
        </script>
    </head>
    <body>
        <jsp:include page="../temp/header.jsp" />
        <div id="wigetCtrl">
            <input type="hidden" id="userJobnumber" value="${sessionScope.jobnumber}">
            <input type="hidden" id="userSitefloor" value="${sessionScope.sitefloor}">
            <input type="hidden" id="userPermission" value="${sessionScope.permission}">
            <h5 class="alarm">
                權限0(基本使用者)，權限1(幹部)，權限2(管理者)<c:if test="${sessionScope.permission >=3}">，權限3(最高管理人員)</c:if>
                    ，其中權限1與2的使用者，每日會收到系統所寄出的請假通知信件(附加在CC中)
                </h5>
                <h5 class="alarm">
                    ※所有的修改將在使用者"下次登入"時生效。
                </h5>
                <table id="userInfo" class="table table-condensed">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>工號</th>
                            <th>密碼</th>
                            <th>名稱</th>
                            <th>線別</th>
                            <th>人員種類</th>
                            <th>權限</th>
                            <th>樓層</th>
                            <th>email</th>
                            <th>動作</th>
                        </tr>
                    </thead>
                </table>
                <div>
                    <!-- Trigger the modal with a button -->
                    <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">人員新增</button><--欲新增人員請按

                    <!-- Modal -->
                    <div id="myModal" class="modal fade" role="dialog">
                        <div class="modal-dialog">

                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">人員新增</h4>
                                </div>
                                <div class="modal-body">

                                    <div id="" class="">
                                        <form id="newUser">
                                            <input type="hidden" name="action" value="insert">
                                            <table class="table">
                                                <tr>
                                                    <td>姓名:</td>
                                                    <td><input type="text" name="name" placeholder="請輸入姓名" /></td>
                                                </tr>
                                                <tr>
                                                    <td>工號:</td>
                                                    <td><input type="text" name="jobnumber" placeholder="請輸入工號" /></td>
                                                </tr>
                                                <tr>
                                                    <td>密碼:</td>
                                                    <td><input type="password" name="password" placeholder="請輸入密碼" /></td>
                                                </tr>
                                                <tr>
                                                    <td>線別:</td>
                                                    <td>
                                                        <select name="lineType" class="lineType">
                                                            <option value="">請輸入線別</option>
                                                        <c:forEach var="lineType" items="${identit.allUserLineType}">
                                                            <option value="${lineType.id}">${lineType.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>種類:</td>
                                                <td>
                                                    <select name="department" class="department">
                                                        <c:forEach var="department" items="${identit.allDepartment}">
                                                            <option value="${department.id}">${department.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>權限:</td>
                                                <td>
                                                    <select name="permission" class="permission">
                                                        <option value="">請選擇權限</option>
                                                        <c:forEach var="i" begin="0" end="${sessionScope.permission}" step="1">
                                                            <option value="${i}">${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>樓層:</td>
                                                <td>
                                                    <select name="sitefloor" class="sitefloor">
                                                        <option value="">請輸入樓層</option>
                                                        <option value="B1">B1</option>
                                                        <c:forEach var="i" begin="1" end="7" step="1">
                                                            <option value="${i}">${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>email:</td>
                                                <td><input type="text" name="email" class="email" placeholder="請輸入email" /></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" class="form-inline">
                                                    <input type="submit" id="addUser" value="確定" />
                                                    <input type="reset" id="reset" value="重設" />
                                                    <c:if test="${sessionScope.permission > initParam.TEST_FIELD_ACCESS_PERMISSION}">
                                                        <input type="button" id="addTestVal" value="測試" />
                                                    </c:if>
                                                </td>
                                            </tr>  
                                        </table>
                                    </form>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="../temp/footer.jsp" />
    </body>
</html>
