/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.entity.Holiday;
import com.advantech.service.BasicService;
import com.advantech.service.HolidayService;
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
@WebServlet(name = "HolidayManage", urlPatterns = {"/HolidayManage"})
public class HolidayManage extends HttpServlet {

    private HolidayService holidayService = null;

    @Override
    public void init()
            throws ServletException {
        holidayService = BasicService.getHolidayService();
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
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        if (action != null && !"".equals(action)) {
            List l = new ArrayList();
            l.add(new Holiday(StringParser.strToInt(id), name, dateFrom, dateTo));
            switch (action) {
                case "insert":
                    out.print(holidayService.newHoliday(l));
                    break;
                case "update":
                    out.print(holidayService.updateHoliday(l));
                    break;
                case "delete":
                    out.print(holidayService.deleteHoliday(l));
                    break;
            }
        }
    }
}
