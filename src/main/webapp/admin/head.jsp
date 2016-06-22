<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<style>
    .active{
        background-color: red;
    }
    a{
        color:#333333;
        text-decoration: none;
        font-size:18px;
        /*display:block;*/
    }
    /*div{padding:10px 3px 10px 42px;}*/
    a:link,a:visited{
        color:#999999;
        font-size: 14px;
        text-decoration:none;
    }
    .borderless tbody tr td, .borderless tbody tr th, .borderless thead tr th {
        /*border: none;*/
    }
    .wigetctrl{
        padding:10px 3px 10px 42px;
    }
    .navbar-custom .navbar-nav > .admin > a:hover {
        color: red;
    }
</style>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script>

</script>
<div style="text-align:center; color: red">
    <noscript>For full functionality of this page it is necessary to enable JavaScript. Here are the <a href="http://www.enable-javascript.com" target="_blank"> instructions how to enable JavaScript in your web browser</a></noscript>
</div>
<input type="hidden" id="userNo" value="<c:out value="${sessionScope.userNo}" default="" />" />
<nav class="navbar navbar-default navbar-custom">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="../"><b>請假申請紀錄系統</b></a>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <li><a href="../LeaveRequest">請假申請</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">加班申請<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="../OvertimeRequest">加班申請</a></li>
                        <li><a href="../OvertimeRequestCheck">加班確認</a></li>
                        <li><a href="../OvertimeRequestHistory">加班歷史查詢</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        歷史查詢
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="../LeaveRequestHistory">請假紀錄(明細)</a></li>
                        <li><a href="../TotalLeaveRequest">請假紀錄(統計)</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        管理人員功能
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="admin"><a href="LeaveRequestBatchInsert">休假批次新增</a></li>
                        <li class="admin"><a href="LeaveRequestManage">休假批次修改</a></li>
                        <li class="admin"><a href="HolidayManage">特殊假期管理</a></li>
                        <li class="admin"><a href="UserManage">${sessionScope.sitefloor}F人員管理</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li id="login">   
                    <c:choose>
                        <c:when test="${sessionScope.user == null}">
                            <a href="../Login">
                                <span class="glyphicon glyphicon-log-in"></span> 人員登入
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a>${sessionScope.user}您好。</a>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li id="logout"><a href="../Logout">登出</a></li>
            </ul>
        </div>
    </div>
</nav>
<!-- 為了省略include所造成多餘的<html><body>標籤而簡化，encoding會有問題還是要加上開頭 -->
