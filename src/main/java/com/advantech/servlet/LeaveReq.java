/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.entity.LeaveRequest;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "LeaveReq", urlPatterns = {"/LeaveReq"})
public class LeaveReq extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LeaveReq.class);

    private int USER_MODIFY_SIGN;
    private LeaveRequestService leaveRequestService;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        USER_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("USER_MODIFY_SIGN"));
        leaveRequestService = BasicService.getLeaveRequestService();
        pChecker = new ParamChecker();
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

        String beginTime = req.getParameter("beginTime");
        String endTime = req.getParameter("endTime");
        String leaveType = req.getParameter("leaveType");
        String leaveReason = req.getParameter("leaveReason");
        int userNo = (int) session.getAttribute("userNo");

        String message;
        boolean isParamVaild = pChecker.checkInputVals(beginTime, endTime, leaveType);

        if (isParamVaild) {
            LeaveRequest lr = new LeaveRequest(
                    userNo,
                    beginTime,
                    endTime,
                    StringParser.strToInt(leaveType),
                    StringParser.strToInt(leaveReason),
                    USER_MODIFY_SIGN
            );
            message = leaveRequestService.newLeaveRequest(lr);
        } else {
            message = "請檢查您的輸入是否有誤，如問題持續發生請聯絡管理員。";
        }

        out.print(message);

    }

    public static void main(String arg0[]) {

    }
}
