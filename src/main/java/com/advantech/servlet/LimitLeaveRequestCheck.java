/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import com.advantech.service.LeaveRequestService;
import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "LimitLeaveRequestCheck", urlPatterns = {"/LimitLeaveRequestCheck"})
public class LimitLeaveRequestCheck extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LimitLeaveRequestCheck.class);

    private int BASIC_PERMISSION;
    private final int INDIRECT_DEPARTMENT_NO = 4;

    private LeaveRequestService leaveRequestService;

    @Override
    public void init() throws ServletException {
        BASIC_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("BASIC_PERMISSION"));
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
        String beginTime = req.getParameter("beginTime");
        String endTime = req.getParameter("endTime");

        HttpSession session = req.getSession(false);

        String sitefloor = (String) session.getAttribute("sitefloor");
        int department = (int) session.getAttribute("department");
        int permission = (int) session.getAttribute("permission");

        try {
            
            boolean needToCheck = (permission == BASIC_PERMISSION && department != INDIRECT_DEPARTMENT_NO);
            List checkResult = (needToCheck ? leaveRequestService.peopleLimitCheck(beginTime, endTime, department, sitefloor) : new ArrayList());
            out.print(checkResult.isEmpty() ? new JSONArray() : new JSONArray(checkResult));

        } catch (JSONException ex) {
            log.error(ex.toString());
            out.print(new JSONArray());
        }
    }

    public static void main(String arg0[]) {
        System.out.println(new JSONArray());
        List l = new ArrayList();
        l.add(1);
        System.out.println(new JSONArray(l));
        System.out.println(new Gson().toJson(l));
    }

}
