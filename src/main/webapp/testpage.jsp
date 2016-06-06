<%-- 
    Document   : overtime
    Created on : 2016/3/4, 下午 03:25:49
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
                width: 90%;
            }
/*            .bento{
                visibility:hidden;
            }*/
        </style>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script>
            var bentoList = [
                {
                    name: "悟饕",
                    bento: [
                        "炸雞腿", "辣雞腿", "滷雞腿", "烤雞腿", "滷排骨",
                        "無骨雞排", "蝦捲", "山賊烤雞腿", "燒肉飯", "卡啦雞",
                        "瓜仔肉", "炸排骨", "烤肉飯", "秋刀魚", "鯖魚",
                        "唐揚", "焢肉飯", "招牌", "香滷雞翅(2支)"
                    ]
                }, {
                    name: "陳記美食",
                    bento: [
                        "四寶飯", "招牌飯", "玫瑰雞腿飯", "吊燒雞腿飯", "明爐雞腿飯",
                        "酥炸雞腿飯", "脆皮燒肉飯", "香酥雞排飯", "黑胡椒鱈魚", "黑胡椒雞排",
                        "黑胡椒豬排", "叉燒烤鴨飯", "明爐烤鴨飯", "菜飯+荷包蛋", "魚肚",
                        "叉燒香腸飯", "叉燒燒肉飯", "港式燒臘飯", "蒜泥白肉飯", "蜜汁叉燒飯",
                        "燒鴨燒肉飯", "油雞烤鴨飯", "玫瑰油雞飯", "叉燒油雞飯", "牛腱"
                    ]
                }
            ];
            $(function () {
                $("select ,input[type='number']").addClass("form-control");
                $(".bento").hide();
                
                for (var i = 0, j = bentoList.length; i < j; i++) {
                    var storeName = bentoList[i].name;
                    $("#bentoStoreName").append("<option value='" + i + "'>" + storeName + "</option>");
                }

                $("#bentoStoreName").change(function () {
                    var selected = $(this).val();
                    $("#bentoList").html("<option value='-1'>請選擇便當</option>");
                    if (selected != -1) {
                        var obj = bentoList[selected].bento;
                        for (var i = 0, j = obj.length; i < j; i++) {
                            var bentoName = obj[i];
                            $("#bentoList").append("<option value='" + bentoName + "'>" + bentoName + "</option>");
                        }
                    }
                });

                $("#overtime").change(function () {
                    if ($(this).val() >= 2) {
                        $(".bento").show();
                    } else {
                        $(".bento").val(-1).hide();
                    }
                });
            });
        </script>
    </head>
    <body>
        <jsp:include page="head.jsp" />
            <div id="wigetCtrl">
                <h1>網頁施工中...</h1>
                <div id="form-wiget" class="form-inline">
                    <label for="overtime">加班時數:</label>
                    <!--加班時數>2小時候出現-->
                    <input type="number" id="overtime" min="0.5" max="5" step="0.5" placeholder="小時(Hour)" />
                    <select id="bentoStoreName" class="bento">
                        <option value="-1">請選擇店家</option>
                    </select>
                    <select id="bentoList" class="bento">
                        <option value="-1">請選擇便當</option>
                    </select>
                </div>
            </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
