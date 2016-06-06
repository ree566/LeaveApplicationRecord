/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {

    private IdentitService identitService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init()
            throws ServletException {
        identitService = BasicService.getIdentitService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession(false);
        int userNo = (int) session.getAttribute("userNo");
        String jobnumber = (String) session.getAttribute("jobnumber");

        String oldpassword = req.getParameter("oldpassword");
        String password = req.getParameter("password");

        boolean isParamVaild = pChecker.checkInputVals(oldpassword, password);
        boolean isUserExist = identitService.userLogin(jobnumber, oldpassword).isEmpty();

        if (!isUserExist && isParamVaild) {
            out.print(identitService.updatePassword(userNo, password));
        } else {
            out.print(false);
        }
    }

    public static void main(String[] arg0) {

    }
}
