/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "AllUser", urlPatterns = {"/AllUser"})
public class AllUser extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AllUser.class);

    private int SYSOP_LIMIT_PERMISSION;
    private IdentitService identitService = null;

    @Override
    public void init() throws ServletException {
        SYSOP_LIMIT_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYSOP_LIMIT_PERMISSION"));
        identitService = BasicService.getIdentitService();
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
        HttpSession session = req.getSession(false);
        int permission = (int) session.getAttribute("permission");
        List l;
        if (permission >= SYSOP_LIMIT_PERMISSION) {

            l = identitService.getIdentit(permission);

        } else if (permission < SYSOP_LIMIT_PERMISSION) {

            int sitefloor = (int) session.getAttribute("sitefloor");
            l = identitService.getIdentit(permission, sitefloor);

        } else {
            
            l = new ArrayList();
            
        }
        out.print(new JSONObject().put("data", l));
    }

    public static void main(String arg0[]) {
        double standard = 0.8;
        Double num = 0.800000000000000;
        System.out.println(num > standard ? "true" : "false");
    }
}
