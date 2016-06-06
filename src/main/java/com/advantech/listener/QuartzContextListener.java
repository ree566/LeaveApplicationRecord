/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.model.BasicDAO;
import com.advantech.helper.ThreadLocalCleanUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Wei.Cheng
 */
public class QuartzContextListener implements ServletContextListener {

    public QuartzContextListener() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BasicDAO.cleanUpSource();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadLocalCleanUtil.clearThreadLocals();


    }
}
