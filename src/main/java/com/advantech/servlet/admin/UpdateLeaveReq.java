/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.DateUtils;
import com.advantech.helper.StringParser;
import com.advantech.entity.LeaveRequest;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "UpdateLeaveReq", urlPatterns = {"/UpdateLeaveReq"})
public class UpdateLeaveReq extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateLeaveReq.class);
    
    private int USER_MODIFY_SIGN, ADMIN_MODIFY_SIGN, SYTEM_MANAGER_PERMISSION;
    private LeaveRequestService leaveRequestService;

//    private ParamChecker pChecker;
    @Override
    public void init() throws ServletException {
        USER_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("USER_MODIFY_SIGN"));
        ADMIN_MODIFY_SIGN = StringParser.strToInt(getServletContext().getInitParameter("ADMIN_MODIFY_SIGH"));
        SYTEM_MANAGER_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYTEM_MANAGER_PERMISSION"));
        leaveRequestService = BasicService.getLeaveRequestService();
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
        
        String id = req.getParameter("id");
        String leaveType = req.getParameter("leaveType");
        String startDate = req.getParameter("leaveFrom");
        String endDate = req.getParameter("leaveTo");
        String leaveReason = req.getParameter("leaveReason");
        int permission = (int) req.getSession(false).getAttribute("permission");
        
        List l = new ArrayList();
        
        if (permission >= SYTEM_MANAGER_PERMISSION) {
            
            if (!DateUtils.checkDate(startDate, endDate)) {
                String str = startDate;
                startDate = endDate;
                endDate = str;
            }
            
            LeaveRequest lr = new LeaveRequest();
            lr.setId(StringParser.strToInt(id));
            lr.setTypeNo(StringParser.strToInt(leaveType));
            lr.setLeaveFrom(startDate);
            lr.setLeaveTo(endDate);
            lr.setReasonNo(StringParser.strToInt(leaveReason));
            lr.setReqByUser(ADMIN_MODIFY_SIGN);
            l.add(lr);
            
            out.print(leaveRequestService.updateLeaveRequest(l));
        } else {
            out.print(false);
        }
    }
}
