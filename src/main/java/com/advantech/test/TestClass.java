/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.Identit;
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
        List<Identit> l = new ArrayList();
        for (int i = 0; i <= 10; i++) {
            Identit identit = new Identit();
            identit.setId(i);
            l.add(identit);
        }
        System.out.println(new Gson().toJson(l));
        
        Iterator it = l.iterator();
        while(it.hasNext()){
            Identit i = (Identit) it.next();
            if(i.getId() == 5 || i.getId() == 7){
                it.remove();
            }
        }
        System.out.println(new Gson().toJson(l));
    }
}
