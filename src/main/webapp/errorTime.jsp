<%-- 
    Document   : newjsp
    Created on : 2016/4/14, 下午 05:23:40
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${initParam.pageTitle}</title>
        <style>
            #wigetCtrl{
                margin: 0 auto;
                width: 95%;
                text-align: center;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
        <div id="wigetCtrl">
            <h3>很抱歉，目前時間已經超過可申請時間範圍，請明天再試。</h3>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
