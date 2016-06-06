/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.entity.OvertimeRequest;
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
@WebServlet(name = "OvertimeReq", urlPatterns = {"/OvertimeReq"})
public class OvertimeReq extends HttpServlet {

//    private static final Logger log = LoggerFactory.getLogger(OvertimeReq.class);
    private OvertimeRequestService overtimeRequestService;
    private int USER_MODIFY_SIGN;

    @Override
    public void init()
            throws ServletException {
        USER_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("USER_MODIFY_SIGN"));
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

        String overtimeHours = req.getParameter("overtimeHours");
        String bandonId = req.getParameter("bandonId");

        int userNo = (int) session.getAttribute("userNo");

        out.print(insertOvertimeRequest(userNo, StringParser.strToDouble(overtimeHours), bandonId));

    } 

    private boolean insertOvertimeRequest(int userNo, double hours, String bandonId) {

        OvertimeRequest ov = new OvertimeRequest(
                userNo,
                hours,
                USER_MODIFY_SIGN
        );

        if (!"".equals(bandonId)) {
            ov.setBandonId(StringParser.strToInt(bandonId));
        }
        return overtimeRequestService.newOvertimeRequest(ov);
    }

    public static void main(String arg0[]) {

    }
}
