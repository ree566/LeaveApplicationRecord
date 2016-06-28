/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.StringParser;
import com.advantech.model.LeaveRequestDAO;
import com.advantech.service.BasicService;
import com.google.gson.Gson;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestServlet2", urlPatterns = {"/TestServlet2"})
public class TestServlet2 extends HttpServlet {

    private int SYSOP_PERMISSION = 3;

    @Override
    public void init() throws ServletException {
        SYSOP_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYSOP_PERMISSION"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
//        int size = StringParser.strToInt(req.getParameter("size"));
//        int page = StringParser.strToInt(req.getParameter("page"));
        PrintWriter out = res.getWriter();
//        List l = BasicService.getLeaveRequestService().getLeaveRequestDetailInPage(size, page);
        out.print(req.getRemoteAddr());
//        DailyMailSend.sendMailEverySiteFloor();
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E");
        return sdf.format(new Date());
    }

    private String generateMailBody() {
        return new StringBuilder()
                .append("<p>時間 <strong>")
                .append(getDate())
                .append("</strong> 統計到的線平衡率</p>")
                .append("<p>與資料庫儲存的最佳平衡比對，下降差距到達了")
                .append("百分之 ")
                .append(5)
                .append(" </p><p>")
                .append(1)
                .append("% ----> <font style='color:red'>")
                .append(2)
                .append("</font>%</p>")
                .append("<p>工單號碼: ")
                .append("test")
                .append("</p><p>生產機種: ")
                .append("test")
                .append("</p><p>生產人數: ")
                .append("test")
                .append("</p><p>詳細歷史資料請上 <a href='")
                .append("http://172.20.131.208/Line_Balancing/Login.aspx")
                .append("'>http://172.20.131.208/Line_Balancing/Login.aspx</a> 中的歷史紀錄做查詢</p>")
                .toString();
    }

    private String generateTable(JSONArray arr) {
        if (arr.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        JSONObject innerObj = arr.getJSONObject(0);
        Iterator it = innerObj.keySet().iterator();

        sb.append("<table border=1 style='padding:2px 2px'>");
        sb.append("<tr>");
        while (it.hasNext()) {
            String key = (String) it.next();
            sb.append("<th>");
            sb.append(key);
            sb.append("</th>");
        }
        sb.append("</tr>");

        for (int i = 0, j = arr.length(); i < j; i++) {
            JSONObject jsonObj = arr.getJSONObject(i);
            Iterator it2 = jsonObj.keySet().iterator();
            sb.append("<tr>");
            while (it2.hasNext()) {
                String key = (String) it2.next();
                sb.append("<td>");
                sb.append(jsonObj.get(key));
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
//        return "";
    }

}
