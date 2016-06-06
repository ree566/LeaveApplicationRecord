/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "LeaveRequestOption", urlPatterns = {"/LeaveRequestOption"})
public class LeaveRequestOption extends HttpServlet {

    private List leaveTypes;
    private List leaveReasons;

    @Override
    public void init() throws ServletException {
        LeaveRequestService leaveRequestService = BasicService.getLeaveRequestService();
        leaveTypes = leaveRequestService.getLeaveType();
        leaveReasons = leaveRequestService.getLeaveReason();
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

        out.print(new JSONObject().put("leaveType", leaveTypes).put("leaveReason", leaveReasons));

    }
}
