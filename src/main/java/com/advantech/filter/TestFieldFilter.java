/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.filter;

import com.advantech.helper.StringParser;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Wei.Cheng
 */
public class TestFieldFilter implements Filter {

    private int TEST_FIELD_ACCESS_PERMISSION;

    @Override
    public void init(FilterConfig filterConfig) {
        String contextParam = filterConfig.getServletContext().getInitParameter("TEST_FIELD_ACCESS_PERMISSION");
        TEST_FIELD_ACCESS_PERMISSION = StringParser.strToInt(contextParam);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/";

        int userPermission = (int) session.getAttribute("permission");

        if (userPermission >= TEST_FIELD_ACCESS_PERMISSION) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("Error");
        }

    }

    @Override
    public void destroy() {
    }

}
