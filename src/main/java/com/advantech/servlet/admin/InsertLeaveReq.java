/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.DateUtils;
import com.advantech.helper.StringParser;
import com.advantech.entity.LeaveRequest;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "InsertLeaveReq", urlPatterns = {"/InsertLeaveReq"})
public class InsertLeaveReq extends HttpServlet {

//    private static final Logger log = LoggerFactory.getLogger(InsertLeaveReq.class);
    private int ADMIN_MODIFY_SIGN;
    private LeaveRequestService leaveRequestService;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        ADMIN_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("ADMIN_MODIFY_SIGH"));
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

        String[] userNo = req.getParameterValues("userNo");
        String[] leaveType = req.getParameterValues("leaveType");
        String[] startDate = req.getParameterValues("startDate");
        String[] endDate = req.getParameterValues("endDate");
        String[] leaveReason = req.getParameterValues("leaveReason");

        List l = new ArrayList();

        for (int i = 0, j = userNo.length; i < j; i++) {

            boolean isParamVaild = pChecker.checkInputVals(userNo[i], leaveType[i], startDate[i], endDate[i]);
            if (!isParamVaild) {
                continue;
            }

            String start = startDate[i];
            String end = endDate[i];
            if (!DateUtils.checkDate(start, end)) {
                String str = start;
                start = end;
                end = str;
            }

            l.add(new LeaveRequest(
                    StringParser.strToInt(userNo[i]),
                    start,
                    end,
                    StringParser.strToInt(leaveType[i]),
                    StringParser.strToInt(leaveReason[i]),
                    ADMIN_MODIFY_SIGN
            ));
        }
        if (l.isEmpty()) {
            out.print("無資料新增");
        } else {
            out.print(leaveRequestService.insertLeaveRequest(l) ? "新增成功" : "新增失敗");
        }
        res.setHeader("Refresh", "3;url=pages/LeaveRequestHistory");
    }
}
