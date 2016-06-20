/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import com.advantech.service.OvertimeRequestService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "GetOvertimeRequest", urlPatterns = {"/GetOvertimeRequest"})
public class GetOvertimeRequest extends HttpServlet {

    private int BASIC_PERMISSION;//0
    private int LINE_LEADER_PERMISSION;//1
    private int SYTEM_MANAGER_PERMISSION;//2
    private int SYSOP_LIMIT_PERMISSION;//3

    OvertimeRequestService overtimeRequestService;

    @Override
    public void init() throws ServletException {
        BASIC_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("BASIC_PERMISSION"));
        LINE_LEADER_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("LINE_LEADER_PERMISSION"));
        SYTEM_MANAGER_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYTEM_MANAGER_PERMISSION"));
        SYSOP_LIMIT_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYSOP_LIMIT_PERMISSION"));
        overtimeRequestService = BasicService.getOvertimeRequestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession(false);

        //只獲取"當日"的加班請求，其他請查詢加班history

        out.print(new JSONObject().put("data", getOvertimeRequest(
                (int) session.getAttribute("userNo"), 
                (int) session.getAttribute("permission"), 
                (String) session.getAttribute("sitefloor"), 
                (int) session.getAttribute("department")
        )));
    }

    private List getOvertimeRequest(int userNo, int permission, String sitefloor, int department) {
        List l;
        if (permission == BASIC_PERMISSION) {
            l = overtimeRequestService.getOvertimeRequest(userNo);
        } else if (permission == LINE_LEADER_PERMISSION) {
            l = overtimeRequestService.getOvertimeRequest(sitefloor, department);
        } else if (permission == SYTEM_MANAGER_PERMISSION) {
            l = overtimeRequestService.getOvertimeRequestBySitefloor(sitefloor);
        } else if (permission >= SYSOP_LIMIT_PERMISSION) {
            l = overtimeRequestService.getOvertimeRequest();
        } else {
            l = new ArrayList();
        }
        return l;
    }
}
