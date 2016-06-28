<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>${initParam.pageTitle}</title>
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <style>
            fieldset {
                width: 400px;
                margin-top:150px;
            }

            /*            fieldset legend {
                            border: 1px solid;
                            padding: 5px 5px;
                        }*/

            img {
                position: relative;
                width: 200px;
                float: right;
                bottom: 0px;
            }

            .borderless tbody tr td, .borderless tbody tr th, .borderless thead tr th {
                border: none;
            }
            .error{
                color: red;
            }
            .divctrl{
                /*border: 1px solid;*/
            }
            fieldset.scheduler-border{
                border: 1px groove #ddd !important;
                padding: 0 1.4em 1.4em 1.4em !important;
                /*margin: 0 0 1.5em 0 !important;*/
                -webkit-box-shadow:  0px 0px 0px 0px #000;
                box-shadow:  0px 0px 0px 0px #000;
            }
            legend.scheduler-border {
                width:inherit; /* Or auto */
                padding:0 10px; /* To give a bit of padding on the left and right */
                border-bottom:none;
                border: 1px groove #ddd;
            }
        </style>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="//cdn.jsdelivr.net/jquery.validation/1.15.0/jquery.validate.min.js"></script> 
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery.blockUI/2.70/jquery.blockUI.min.js"></script>
        <script>
            var isCommitted = false;//表单是否已经提交标识，默认为false
            function dosubmit() {
                if (!isCommitted) {
                    isCommitted = true;//提交表单后，将表单是否已经提交标识设置为true
                    return true;//返回true让表单正常提交
                } else {
                    return false;//返回false那么表单将不提交
                }
            }
            jQuery.validator.addMethod("regex", //addMethod第1个参数:方法名称
                    function (value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
                        var exp = new RegExp(params);     //实例化正则对象，参数为传入的正则表达式
                        return exp.test(value);                    //测试是否匹配
                    }, "格式錯誤");    //addMethod第3个参数:默认错误信息
            var rule = {required: true, regex: "^[0-9a-zA-Z-]+$"};
            var msg = {required: "必须填寫", regex: "格式錯誤"};
            $(function () {
                $("#login").validate({
                    rules: {
                        jobnumber: rule, //密码1必填
                        password: rule
                    },
                    messages: {
                        jobnumber: msg,
                        password: msg
                    },
                    errorPlacement: function (error, element) {                             //错误信息位置设置方法  
                        error.appendTo(element.parent().next());                            //这里的element是录入数据的对象  
                    },
                    submitHandler: function (form) {
                        block();
                        form.submit();
                    }
                });
            });
            function block() {
                $.blockUI({
                    css: {
                        border: 'none',
                        padding: '15px',
                        backgroundColor: '#000',
                        '-webkit-border-radius': '10px',
                        '-moz-border-radius': '10px',
                        opacity: .5,
                        color: '#fff'
                    },
                    fadeIn: 0
                    , overlayCSS: {
                        backgroundColor: '#FFFFFF',
                        opacity: .3
                    }
                });
            }
            $(document).ready(function () {
                $(":text,:password").addClass("form-control");
                $(":submit,:reset").addClass("btn btn-default");
            });
        </script>
    </head>

    <body>
        <c:if test="${sessionScope.Jobnumber != null}">
            <c:redirect url="Home"/>
        </c:if>
        <center>
            <div class="divctrl">
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border">請假申請紀錄系統:</legend>
                    <div>
                        <form action="Login" method="post" onsubmit="dosubmit()" id="login">
                            <table class="table borderless">
                                <tr>
                                    <td>Name:</td>
                                    <td><input type="text" name="jobnumber" maxlength="50" autocomplete="off"></td>
                                    <td></td>
                                </tr>
                                <td>Password:</td>
                                <td><input type="password" name="password" maxlength="50"></td>
                                <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td><input type="submit" id="btnSubmit" value="提交" /><input type="reset" value="取消"/></td>
                                    <td></td> 
                                </tr>
                            </table>
                            <!--<img src="images/logo.jpg" />-->
                            <div id="errormsg" style="color:red;float:left">
                                <c:out value="${errormsg}" default=""></c:out>
                            </div>
                        </form>
                    </div>
                </fieldset>
            </div>
        </center>
    </body>
</html>
