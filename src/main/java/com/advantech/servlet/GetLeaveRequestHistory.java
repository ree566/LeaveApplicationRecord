/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
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
@WebServlet(name = "GetLeaveRequestHistory", urlPatterns = {"/GetLeaveRequestHistory"})
public class GetLeaveRequestHistory extends HttpServlet {

    private int BASIC_PERMISSION;
    private int SYSOP_LIMIT_PERMISSION;
    private LeaveRequestService leaveRequestService;

    @Override
    public void init() throws ServletException {
        BASIC_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("BASIC_PERMISSION"));
        SYSOP_LIMIT_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYSOP_LIMIT_PERMISSION"));
        leaveRequestService = BasicService.getLeaveRequestService();
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

        out.print(new JSONObject().put("data", getLeaveRequest(
                (int) session.getAttribute("userNo"),
                (int) session.getAttribute("permission"),
                (String) session.getAttribute("sitefloor"),
                req.getParameter("startDate"),
                req.getParameter("endDate") + " 23:00"
        )));

    }

    private List getLeaveRequest(int userNo, int permission, String sitefloor, String startDate, String endDate) {
        List l;
        if (permission == BASIC_PERMISSION) {
            l = leaveRequestService.getPersonalRequest(startDate, endDate, userNo);
        } else if (permission > BASIC_PERMISSION && permission < SYSOP_LIMIT_PERMISSION) {
            l = leaveRequestService.getLeaveRequestBySitefloor(startDate, endDate, sitefloor);
        } else if (permission >= SYSOP_LIMIT_PERMISSION) {
            l = leaveRequestService.getLeaveRequest(startDate, endDate);
        } else {
            l = new ArrayList();
        }
        return l;
    }
}
