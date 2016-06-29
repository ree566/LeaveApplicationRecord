<%-- 
    Document   : index
    Created on : 2016/2/22, 上午 09:46:58
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
    </head>
    <body>
        <div>
            <c:set var="username" value="${sessionScope.user}"/>
            <c:set var="jobnumber" value="${sessionScope.jobnumber}"/>
            ${jobnumber == null ? "N/A" : jobnumber}，
            ${username == null ? "Guest" : username}您好。
            <c:redirect url="pages/LeaveRequest" />
        </div>
    </body>
</html>
