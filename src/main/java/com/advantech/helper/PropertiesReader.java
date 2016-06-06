/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class PropertiesReader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private static PropertiesReader p;
    private String mailList;
    private String ccList;

    private PropertiesReader() throws Exception {
            dataInit();
    }

    public static PropertiesReader getInstance() {
        if (p == null) {
            
            try {
                p = new PropertiesReader();
            } catch (Exception ex) {
                log.error("Can not load the property file.");
            }
        }
        return p;
    }

    private void dataInit() throws Exception {
        String configFile = "/options.properties";
        Properties properties = new Properties();
        InputStream is = this.getClass().getResourceAsStream(configFile);
        properties.load(is);
        
        loadParams(properties);
        
        is.close();
        properties.clear();
    }
    
    private void loadParams(Properties properties) {
        mailList = properties.getProperty("mailList");
        ccList = properties.getProperty("ccList");
    }

    public String getMailList() {
        return mailList;
    }

    public String getCcList() {
        return ccList;
    }

    public static void main(String arg0[]){
        PropertiesReader p = PropertiesReader.getInstance();
        System.out.println(p.ccList);
        System.out.println(p.mailList);
    }
}
