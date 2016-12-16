/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.entity.LeaveRequest;
import static com.advantech.helper.DateUtils.getCurrentHour;
import static com.advantech.helper.DateUtils.isNextBussinessDayBetweenTwoDates;
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

    private int BASIC_PERMISSION;
    private int USER_MODIFY_SIGN, REQUEST_DENY_TIME;
    private LeaveRequestService leaveRequestService;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        BASIC_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("BASIC_PERMISSION"));
        USER_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("USER_MODIFY_SIGN"));
        REQUEST_DENY_TIME = StringParser.strToInt(getServletContext().getInitParameter("REQUEST_DENY_TIME"));
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
        Integer permission = (int) session.getAttribute("permission");

        String message;
        boolean isParamVaild = pChecker.checkInputVals(beginTime, endTime, leaveType);

        if (isParamVaild) {
            if (getCurrentHour() >= REQUEST_DENY_TIME && permission == BASIC_PERMISSION) {
                if (isNextBussinessDayBetweenTwoDates(beginTime, endTime)) { 
                    out.print("如欲申請下次上班日的假期請於" + REQUEST_DENY_TIME + "時前完成申請，如有問題請聯絡系統管理員");
                    return;
                }
            }
            
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
