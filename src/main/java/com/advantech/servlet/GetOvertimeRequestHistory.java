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
@WebServlet(name = "GetOvertimeRequestHistory", urlPatterns = {"/GetOvertimeRequestHistory"})
public class GetOvertimeRequestHistory extends HttpServlet {

    private int BASIC_PERMISSION;
    private int SYSOP_LIMIT_PERMISSION;
    OvertimeRequestService overtimeRequestService;

    @Override
    public void init() throws ServletException {
        BASIC_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("BASIC_PERMISSION"));
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

        int iDisplayStart = StringParser.strToInt(req.getParameter("iDisplayStart"));
        int iDisplayLength = StringParser.strToInt(req.getParameter("iDisplayLength"));

        List l;
        int permission = (int) session.getAttribute("permission");
        if (permission == BASIC_PERMISSION) {
            
            int userNo = (int) session.getAttribute("userNo");
            l = overtimeRequestService.getPersonalOvertimeRequestHistory(userNo);
            
        } else if (permission > BASIC_PERMISSION && permission < SYSOP_LIMIT_PERMISSION) {
            
            int sitefloor = (int) session.getAttribute("sitefloor");
            l = overtimeRequestService.getOvertimeHistoryBySitefloor(sitefloor);
            
        } else if (permission >= SYSOP_LIMIT_PERMISSION) {
            
            l = overtimeRequestService.getAllOvertimeRequestHistory(iDisplayLength, iDisplayStart);
            System.out.print("iDisplayStart: " + iDisplayStart + " iDisplayLength: " + iDisplayLength);
            
        } else {
            
            l = new ArrayList();
            
        }
        out.print(new JSONObject().put("data", l));

    }
}
