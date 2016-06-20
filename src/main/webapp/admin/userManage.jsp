<%-- 
    Document   : adinAlter
    Created on : 2016/2/23, 下午 05:02:12
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="leaveRequest" class="com.advantech.model.LeaveRequestDAO" scope="application" />
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
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="../js/jquery.dataTables.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="../js/jquery.blockUI.js"></script>
        <script src="../js/jquery.blockUI.Default.js"></script>
        <script src="../js/jquery.dataTable.domSort.js"></script>
        <script src="../js/dropDownList.js"></script>
        <script src="../js/serverMessage.js"></script>
        <script>
            $(function () {

                var department = $(".department:first").clone();
                var sitefloor = $(".sitefloor:first").clone();
                var permission = $(".permission:first").clone();

                var userTable = $("#userInfo").DataTable({
                    "processing": false,
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
                        {data: "id"},
                        {data: "jobnumber", width: "80px"},
                        {data: "password", width: "150px"},
                        {data: "name", width: "150px"},
                        {data: "department", width: "150px"},
                        {data: "permission", width: "80px", "orderDataType": "dom-text-numeric"},
                        {data: "sitefloor", width: "80px", "orderDataType": "dom-text-numeric"}
                    ],
                    "columnDefs": [
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
                            "data": "department",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(department, data);
                            }
                        },
                        {
                            "targets": [5],
                            "data": "permission",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(permission, data);
                            }
                        },
                        {
                            "targets": [6],
                            "data": "sitefloor",
                            "render": function (data, type, full) {
                                return dulpicateDropDownList(sitefloor, data);
                            }
                        },
                        {
                            "targets": [7],
                            "data": "email",
                            "render": function (data, type, full) {
                                return "<input type='text' class='email' value='" + (data == null ? '' : data) + "' />";
                            }
                        },
                        {
                            "targets": [8],
                            "render": function (data, type, full) {
                                return "<div class='form-inline'><input type='button' class='save' value='儲存' disabled /><input type='button' class='delete' value='刪除' /></div>";
                            }
                        }
                    ],
                    "oLanguage": {
                        "sZeroRecords": "無符合資料",
                        "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
                    },
                    bAutoWidth: true,
                    iDisplayLength: -1,
                    order: [[1, 'asc']],
                    "fnDrawCallback": function () {
                        $(":text,input,select").addClass("form-control");
                        $(":button").addClass("btn btn-default");

                        var adminsSitefloor = $("#userSitefloor").val();
                        $(".sitefloor").attr($("#userPermission").val() > 2 ? {min: 1, max: 7} : {max: adminsSitefloor, min: adminsSitefloor});
                        $(".password").focus(function () {
                            this.type = "text";
                        }).blur(function () {
                            this.type = "password";
                        });
                        $(":password,:text,input[type='number'],select").change(function () {
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
                            var department = parents.find(".department").val();
                            var permission = parents.find(".permission").val();
                            var sitefloor = parents.find(".sitefloor").val();
                            var email = parents.find(".email").val();

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
                                        department: department,
                                        permission: permission,
                                        sitefloor: sitefloor,
                                        email: email
                                    },
                                    success: function (d) {
                                        $(parents).removeClass("danger");
                                        $(this).attr("disabled", true);
                                        showServerMsg(d);
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
                                    showServerMsg(d);
                                },
                                complete: function (d) {
                                    userTable.ajax.reload();//reload因為非同步只有在這裡會有作用
                                }
                            });
                        });
                    }
                });

                $("#addUser").click(function () {
                    if (!confirm("確定新增人員?")) {
                        return false;
                    }
                    var user = [];
                    $("#newUser").children(":not(:button)").each(function () {
                        var isvaild = checkVal($(this).val());
                        if (!isvaild) {
                            user.length = 0;
                            return false;
                        }
                        user.push($(this).val());
                    });
                    if (user.length == 0) {
                        alert("輸入資料有誤或者空白，請再次確認。");
                        return false;
                    }
                    $.ajax({
                        url: "../UserManage",
                        method: 'POST',
                        dataType: 'json',
                        data: {
                            action: "insert",
                            name: user[0],
                            jobnumber: user[1],
                            password: user[2],
                            department: user[3],
                            permission: user[4],
                            sitefloor: user[5],
                            email: user[6]
                        },
                        success: function (d) {
                            showServerMsg(d);
                        },
                        complete: function (d) {
                            userTable.ajax.reload();//reload因為非同步只有在這裡會有作用
                        }
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
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <div id="wigetCtrl">
            <input type="hidden" id="userSitefloor" value="${sessionScope.sitefloor}">
            <input type="hidden" id="userPermission" value="${sessionScope.permission}">
            <table id="userInfo" class="table table-condensed">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>工號</th>
                        <th>密碼</th>
                        <th>名稱</th>
                        <th>人員種類</th>
                        <th>權限</th>
                        <th>樓層</th>
                        <th>email</th>
                        <th>動作</th>
                    </tr>
                </thead>
            </table>
            <div>
                <h3>人員新增</h3>
                <div id="newUser" class="form-inline">
                    <input type="text" placeholder="請輸入姓名" />
                    <input type="text" placeholder="請輸入工號" />
                    <input type="password" placeholder="請輸入密碼" />
                    <select class="department">
                        <c:forEach var="department" items="${leaveRequest.allDepartment}">
                            <option value="${department.id}">${department.name}</option>
                        </c:forEach>
                    </select>
                    <select class="permission">
                        <option value="-1">請選擇權限</option>
                        <c:forEach var="i" begin="0" end="${sessionScope.permission}" step="1">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                    <select class="sitefloor">
                        <option value="">請輸入樓層</option>
                        <option value="B1">B1</option>
                        <c:forEach var="i" begin="1" end="7" step="1">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                    <input type="text" class="email" placeholder="請輸入email" />
                    <input type="button" id="addUser" value="確定" />
                </div>
            </div>
        </div>
        <div id="floatWiget" hidden="hidden"></div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
