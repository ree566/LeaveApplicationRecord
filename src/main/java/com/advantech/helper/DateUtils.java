/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.entity.Holiday;
import com.advantech.service.BasicService;
import com.advantech.service.HolidayService;
import com.advantech.service.LeaveRequestService;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(StringParser.class);

    private static final String DATE_WITH_DAYONLY = "yyyy-MM-dd";
    private static final String DATE_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";
    private static final String FULL_DATE_INFO = "yyyy-MM-dd HH:mm:ss.SSS";

    public static void main(String[] arg0) {
 

        DateUtils dms = new DateUtils();
        System.out.println(dms.getToday().toString());

    }

    public static boolean checkDate(String begin, String end) {
        DateTime beginTime = stringToDateTimeWithoutSecond(begin);
        DateTime endTime = stringToDateTimeWithoutSecond(end);
        return beginTime.isBefore(endTime);
    }

    public static DateTime stringToDateTimeWithoutSecond(String d) {
        return toDateTime(DATE_WITHOUT_SECOND, d);
    }

    public static String dateTimeToStringWithoutSecond(DateTime d) {
        return toDateString(DATE_WITHOUT_SECOND, d);
    }

    public static String toFullDateString(DateTime d) {
        return toDateString(FULL_DATE_INFO, d);
    }

    public static DateTime toFullDateTime(String d) {
        return toDateTime(FULL_DATE_INFO, d);
    }

    public static String toDateStringOnlyDay(DateTime d) {
        return toDateString(DATE_WITH_DAYONLY, d);
    }

    private static String toDateString(String reg, DateTime d) {
        return DateTimeFormat.forPattern(reg).print(d);
    }

    private static DateTime toDateTime(String reg, String d) {
        return DateTimeFormat.forPattern(reg).parseDateTime(d);
    }

    private DateTime getToday() {
        return new DateTime().withTimeAtStartOfDay();
//        return new DateTime(2016, 4, 29, 0, 0, 0, 0);
    }

    public String getTodaysString() {
        return toDateStringOnlyDay(getToday());
    }

    public String nextBusinessDay() {
        return toDateStringOnlyDay(findNextBusinessDay());
    }

    public DateTime findNextBusinessDay() {
        return findNextBusinessDay(getToday());
    }

    private DateTime findNextBusinessDay(DateTime d) {
        log.info("Finding next businessDay...");
//        System.out.println("Finding next businessDay...");

        do {
            d = d.plusDays(1);
            log.info("The next date is " + d.toString() + ", begin checking...");
//            System.out.println("The next date is " + d.toString() + ", begin checking...");
        } while (checkDaySpecial(d));
        log.info("Next businessDay is " + toFullDateString(d));
//        System.out.println("Next businessDay is " + toFullDateTime(d));
        return d;
    }

    //Check is today in special day table(1-5)
    public boolean checkTodayIsSpecailDay() {
        DateTime today = getToday();
        boolean checkStatus = checkDaySpecial(today);
        log.info("Today is " + toFullDateString(today) + " ,is today a special day ? -- " + checkStatus);
//        System.out.println("Today is " + toFullDateTime(today) + " ,is today a special day ? -- " + checkStatus);
        return checkStatus;
    }

    private boolean checkDaySpecial(DateTime d) {
        HolidayService hs = BasicService.getHolidayService();
        int dayOfWeek = d.getDayOfWeek();
        switch (dayOfWeek) {
            case DateTimeConstants.SUNDAY:
                return true;
            case DateTimeConstants.SATURDAY:
                List<Holiday> l = hs.getSpecialSaturday(d.getMonthOfYear());
                if (l.isEmpty()) {
                    log.info("This Month not exist Special Saturday, so today is not need to check special Saturday.");
//                    System.out.println("This Month not exist Special Saturday, so today is not need to check special Saturday.");
                    return true;
                } else {
                    log.info("This month contain special Saturday, check is today special...");
//                    System.out.println("This month contain special Saturday, check is today special...");
                    Holiday h = l.get(0);
                    DateTime d1 = DateUtils.toFullDateTime(h.getDateFrom());
                    log.info(toFullDateString(d1));
//                    System.out.println(toFullDateTime(d1));
                    boolean isSpecialSaturday = d1.toLocalDate().isEqual(d.toLocalDate());
                    log.info("Is today special Saturday? -- " + isSpecialSaturday);
//                    System.out.println("Is today special Saturday? -- " + isSpecialSaturday);
                    return !isSpecialSaturday; //Return false to send mail on saturday.
                }
            default:
                boolean checkStatus = hs.isSpecialDay(toFullDateString(d.withTimeAtStartOfDay()));
                log.info("Today is normal BusinessDay, Check is today in SpecialDays -- " + checkStatus);
                System.out.println("Today is normal BusinessDay, Check is today in SpecialDays -- " + checkStatus);
                return checkStatus;
        }
    }

    public static int dateDiff(String begin, String end) {
        int diff = -1;
        try {
            DateTime beginTime = stringToDateTimeWithoutSecond(begin);
            DateTime endTime = stringToDateTimeWithoutSecond(end);
            diff = Hours.hoursBetween(beginTime, endTime).getHours();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return diff;
    }
}
