/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.entity.LeaveRequest;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "LeaveRequestDulpCheck", urlPatterns = {"/LeaveRequestDulpCheck"})
public class LeaveRequestDulpCheck extends HttpServlet {

//    private static final Logger log = LoggerFactory.getLogger(InsertLeaveReq.class);
    @Override
    public void init() throws ServletException {
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

        String userNo = req.getParameter("userNo");
        String beginTime = req.getParameter("beginTime");
        String endTime = req.getParameter("endTime");
        LeaveRequestService leaveRequestService = BasicService.getLeaveRequestService();

        boolean checkStatus = leaveRequestService.isPersonDataInDayExist(
                new LeaveRequest(
                        StringParser.strToInt(userNo),
                        beginTime,
                        endTime
                )
        );

        out.print(checkStatus);

    }
}
