/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.Identit;
import com.advantech.service.BasicService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static final Logger log = LoggerFactory.getLogger(TestClass.class);

    public static void main(String arg0[]) {
        List l = BasicService.getLeaveRequestService().getLeaveRequestInDay("2016-06-21", 1, "6");
        System.out.println(new Gson().toJson(l));
    }
}
