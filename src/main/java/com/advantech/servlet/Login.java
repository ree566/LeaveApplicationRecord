package com.advantech.servlet;

import com.advantech.helper.StringParser;
import com.advantech.entity.Identit;
import com.advantech.helper.ParamChecker;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/Login"},
        initParams = {
            @WebInitParam(name = "SUCCESS", value = "pages/LeaveRequest"),
            @WebInitParam(name = "FAIL", value = "login.jsp"),
            @WebInitParam(name = "ALARM", value = "pages/ChangePSW")}
)
public class Login extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String SUCCESS;
    private String FAIL;
    private String ALARM;

    private int SYSOP_LIMIT_PERMISSION;

    private IdentitService identitService = null;
    private ParamChecker pChecker = null;

    @Override
    public void init() throws ServletException {
        SUCCESS = getServletConfig().getInitParameter("SUCCESS");
        FAIL = getServletConfig().getInitParameter("FAIL");
        ALARM = getServletConfig().getInitParameter("ALARM");
        SYSOP_LIMIT_PERMISSION = StringParser.strToInt(getServletContext().getInitParameter("SYSOP_LIMIT_PERMISSION"));
        identitService = BasicService.getIdentitService();
        pChecker = new ParamChecker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("jobnumber") != null) {
            res.sendRedirect(SUCCESS);
        } else {
            req.getRequestDispatcher(FAIL).forward(req, res);
        }
    }

    @Override
    @SuppressWarnings("null")
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        String jobnumber = req.getParameter("jobnumber");
        String password = req.getParameter("password");

        Identit i = checkInputValAndLogin(jobnumber, password);
        if (i != null) {
            session.setAttribute("jobnumber", i.getJobnumber());
            session.setAttribute("userNo", i.getId());
            session.setAttribute("user", i.getName());
            session.setAttribute("permission", i.getPermission());
            session.setAttribute("sitefloor", i.getSitefloor());
            session.setAttribute("department", i.getDepartment());
            if (jobnumber.equals(password) && i.getPermission() < SYSOP_LIMIT_PERMISSION) {
                res.sendRedirect(ALARM);
            } else {
                res.sendRedirect(SUCCESS);
            }
        } else {
            req.setAttribute("errormsg", "錯誤的帳號或密碼");
            req.getRequestDispatcher(FAIL).forward(req, res);
        }
    }

    private Identit checkInputValAndLogin(String jobnumber, String password) {
        boolean isParamVaild = pChecker.checkInputVals(jobnumber, password);
        if (!isParamVaild) {
            return null;
        }
        List<Identit> l = identitService.userLogin(jobnumber, password);
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

}
