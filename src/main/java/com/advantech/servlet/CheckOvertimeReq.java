/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.service.BasicService;
import com.advantech.service.OvertimeRequestService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "CheckOvertimeReq", urlPatterns = {"/CheckOvertimeReq"})
public class CheckOvertimeReq extends HttpServlet {

//    private static final Logger log = LoggerFactory.getLogger(CheckOvertimeReq.class);
    OvertimeRequestService overtimeRequestService;

    @Override
    public void init()
            throws ServletException {
        overtimeRequestService = BasicService.getOvertimeRequestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession(false);

        int userNo = (int) session.getAttribute("userNo");

        out.print(overtimeRequestService.isExistOvertimeRequest(userNo));
    }
}
