/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.entity.Identit;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
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
@WebServlet(name = "UserManage", urlPatterns = {"/UserManage"})
public class UserManage extends HttpServlet {

    private IdentitService identitService = null;
//    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
        identitService = BasicService.getIdentitService();
//        pChecker = new ParamChecker();
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
        boolean flag = false;
        if (action != null) {
            String id = req.getParameter("id");
            String jobnumber = req.getParameter("jobnumber");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            String permission = req.getParameter("permission");
            String sitefloor = req.getParameter("sitefloor");
            String email = req.getParameter("email");

//            if (pChecker.checkInputVals(id, jobnumber, password, name, department, permission, sitefloor)) {
            List l = new ArrayList();
            
            l.add(new Identit(
                    StringParser.strToInt(id),
                    jobnumber,
                    password,
                    name,
                    StringParser.strToInt(department),
                    StringParser.strToInt(permission),
                    StringParser.strToInt(sitefloor),
                    email
            ));
            
            switch (action) {
                case "insert":
                    flag = identitService.newIdentit(l);
                    break;
                case "update":
                    flag = identitService.updateIdentit(l);
                    break;
                case "delete":
                    flag = identitService.deleteIdentit(StringParser.strToInt(id));
                    break;
            }
//            }
        }
        out.print(flag);

    }
}
