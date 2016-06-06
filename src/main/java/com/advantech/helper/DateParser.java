/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class DateParser {

    private static final Logger log = LoggerFactory.getLogger(StringParser.class);
    private static final String dateFormatString = "yyyy-MM-dd HH:mm";

    public static void main(String[] arg0) {

    }

    public static boolean checkDate(String begin, String end) {
        try {
            DateTime beginTime = stringToDateTime(begin);
            DateTime endTime = stringToDateTime(end);
            if (beginTime.isBefore(endTime)) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    public static DateTime stringToDateTime(String dateString) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormatString);
        return fmt.parseDateTime(dateString);
    }

    public static String dateTimeToString(DateTime d) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormatString);
        return fmt.print(d);
    }
    
    public static int dateDiff(String begin, String end){
        int diff = -1;
        try {
            DateTime beginTime = stringToDateTime(begin);
            DateTime endTime = stringToDateTime(end);
            diff = Hours.hoursBetween(beginTime, endTime).getHours();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return diff;
    }
}
