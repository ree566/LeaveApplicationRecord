/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "DeleteLeaveReq", urlPatterns = {"/DeleteLeaveReq"})
public class DeleteLeaveReq extends HttpServlet {
    
    private int SYTEM_MANAGER_PERMISSION;
    
    @Override
    public void init() throws ServletException {
        SYTEM_MANAGER_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYTEM_MANAGER_PERMISSION"));
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        PrintWriter out = res.getWriter();
        String id = req.getParameter("id");
        if ((int) req.getSession(false).getAttribute("permission") >= SYTEM_MANAGER_PERMISSION) {
            out.print(deleteLeaveRequest(StringParser.strToInt(id)));
        } else {
            out.print(false);
        }
    }
    
    private boolean deleteLeaveRequest(int id) {
        return BasicService.getLeaveRequestService().deleteLeaveRequest(id);
    }
}
