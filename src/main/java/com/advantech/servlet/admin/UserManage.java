/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet.admin;

import com.advantech.helper.StringParser;
import com.advantech.entity.Identit;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import com.google.gson.Gson;
import java.io.*;
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
@WebServlet(name = "UserManage", urlPatterns = {"/UserManage"})
public class UserManage extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserManage.class);

    private IdentitService identitService = null;
    private ParamChecker pChecker;

    @Override
    public void init() throws ServletException {
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

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        String action = req.getParameter("action");

        JSONObject resultMsg = new JSONObject();

        String errorMessage = "";
        String saveToDbErrorMsg = "發生問題，請聯絡管理人員。";
        boolean result = false;

        if (action != null) {
            String id = req.getParameter("id");
            String jobnumber = req.getParameter("jobnumber");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            String lineType = req.getParameter("lineType");
            String permission = req.getParameter("permission");
            String sitefloor = req.getParameter("sitefloor");
            String email = req.getParameter("email");

//            if (pChecker.checkInputVals(id, jobnumber, password, name, department, permission, sitefloor)) {
            Identit i = new Identit(
                    StringParser.strToInt(id),
                    jobnumber,
                    password,
                    name,
                    StringParser.strToInt(lineType),
                    StringParser.strToInt(department),
                    StringParser.strToInt(permission),
                    sitefloor,
                    pChecker.checkInputVal(email) ? email : null
            );

            log.info("Get new user info " + new Gson().toJson(i));

            switch (action) {
                case "insert":
//                    result = true;
                    if (checkUserIsExist(jobnumber)) {
                        result = false;
                        errorMessage = "已經存在相同的使用者。";
                    } else {
                        result = identitService.newIdentit(i);
                        errorMessage = result ? "" : saveToDbErrorMsg;
                    }
                    break;
                case "update":
                    result = identitService.updateIdentit(i);
                    errorMessage = result ? "" : saveToDbErrorMsg;
                    break;
                case "delete":
                    result = identitService.deleteIdentit(StringParser.strToInt(id));
                    errorMessage = result ? "" : saveToDbErrorMsg;
                    break;
            }
//            }
        }
        out.print(resultMsg.put("result", result).put("errorMessage", errorMessage));

    }

    private boolean checkUserIsExist(String jobnumber) {
        return identitService.isUserExist(jobnumber);
    }
}
