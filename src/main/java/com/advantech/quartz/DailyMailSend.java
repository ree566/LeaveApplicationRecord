/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartz;

import com.advantech.entity.Identit;
import com.advantech.entity.LeaveRequest;
import com.advantech.helper.DateUtils;
import com.advantech.helper.MailSend;
import com.advantech.service.BasicService;
import com.advantech.service.IdentitService;
import com.advantech.service.LeaveRequestService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class DailyMailSend implements Job {

    private static final Logger log = LoggerFactory.getLogger(DailyMailSend.class);
    private final String subjectTitle = "[請假系統訊息] ";
    private final String subject = " 請假人員列表";
    private final String[] tableHead = {"工號", "名稱", "假種", "事由", "時數", "開始時間", "結束時間", "申請時間"};
    private static final int MAIN_MAILTARGET_PERMISSION = 3;
    private final DateUtils dateUtils = new DateUtils();

    private String today;
    private String nextBusinessDay;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        boolean isTodaySpecialDay = dateUtils.checkTodayIsSpecailDay();
        if (!isTodaySpecialDay) {
            sendMailEverySiteFloor();
        }
    }

    public static void main(String[] arg0) {
//        System.out.print(new DailyMailSend().getDate());
    }

    public static void sendMailEverySiteFloor() {

        DailyMailSend d = new DailyMailSend();

        try {
            List l = d.getUserMailTarget();
            if (l.isEmpty()) {
//                log.error("Something wrong with database connection, can't get the user mail list.");
                System.out.println("Something wrong with database connection, can't get the user mail list.");
                return;
            }

            Iterator it = l.iterator();

            List<Identit> mainTargetMailList = new ArrayList();
            JSONArray arr = new JSONArray();

            while (it.hasNext()) {
                Identit i = (Identit) it.next();
                if ("".equals(i.getEmail())) {
                    it.remove();

                } else if (i.getPermission() == MAIN_MAILTARGET_PERMISSION) {
                    mainTargetMailList.add(i);
                    it.remove();
                }
            }

            for (Identit mainTarget : mainTargetMailList) {
                JSONArray innerArray = new JSONArray();
                it = l.iterator();
                while (it.hasNext()) {
                    Identit i = (Identit) it.next();
                    if (i.getSitefloor().equals(mainTarget.getSitefloor())) {
                        String mail = i.getEmail();
                        if (mail != null || !"".equals(mail)) {
                            innerArray.put(mail);
                            it.remove();
                        }
                    }
                }
                arr.put(innerArray);
            }

            for (int i = 0, j = mainTargetMailList.size(); i < j; i++) {
                Identit identit = mainTargetMailList.get(i);
                JSONArray ccMailList = arr.getJSONArray(i);
                if (ccMailList.length() == 0) {
                    continue;
                }
                log.info("Begin send mail...");
//                System.out.println("Begin send mail...");
                d.sendMail(identit.getSitefloor(), identit.getEmail(), ccMailList);
                log.info("Find next floor...");
//                System.out.println("Find next floor...");
            }
            log.info("Send mail job complete.");
//            System.out.println("Send mail job complete.");
        } catch (JSONException e) {
            log.error(e.toString());
        }
    }

    private List getUserMailTarget() {
        IdentitService identitService = BasicService.getIdentitService();
        List l = identitService.getMailList();
        if (l.isEmpty()) {
            int timeCount = 1;
            int retryTime = 5;
            int retrySecond = 10;
            try {
                do {
                    log.error("The maillist isEmpty, retry get maillist again after 5 second...");
//                    System.out.println("The maillist isEmpty, retry get maillist again after " + retrySecond + " second...");
                    Thread.sleep(retrySecond * 1000);
                    log.error("Getting maillist...");
//                    System.out.println("Getting maillist...");
                    l = identitService.getMailList();
                    log.error("Retry times:" + timeCount);
//                    System.out.println("Retry times:" + timeCount);
                    timeCount++;
                } while (l.isEmpty() && timeCount <= retryTime);
            } catch (InterruptedException e) {
                log.error(e.toString());
            }
        }
        return l;
    }

    private void sendMail(String sitefloor, String mainTarget, JSONArray ccList) {

        today = dateUtils.getTodaysString();
        nextBusinessDay = dateUtils.nextBusinessDay();

        String mailTitle = generateTitle(sitefloor);
        String mailBody = generateMailBody(sitefloor);

//        System.out.println("Begin sendMail for sitefloor: " + sitefloor + " F");
//        System.out.println("The main mail target user is: " + mainTarget);
//        System.out.println("The mail cc list users are: " + ccList);
        log.info("Begin sendMail for sitefloor: " + sitefloor + " F");
        log.info("The main mail target user is: " + mainTarget);
        log.info("The mail cc list users are: " + ccList);

        MailSend.getInstance().sendMailWithoutSender(this.getClass(), mainTarget, ccList, mailTitle, mailBody);
    }

    private String generateTitle(String sitefloor) {
        StringBuilder sb = new StringBuilder();
        sb.append(subjectTitle)
                .append(today)
                .append(" ")
                .append(sitefloor)
                .append("F")
                .append(subject);
        return sb.toString();
    }

    public String generateMailBody(String sitefloor) {

        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Dear All:</h2>")
                .append("<h5>以下是下次上班日請假人員，以及今日申請請假人員列表。</h5>")
                .append("<h5 style='color:red'>※申請時間紅色，代表管理員代理申請。</h5>");

        String table1 = getTodaysLeaveRequest(sitefloor);
        table1 = "<h3>今日(" + today + ")申請人員</h3>" + ("".equals(table1) ? "無" : table1);

//        "<h3>明日請假人員</h3>"
        String table2 = getNextBussinessDayLeaveRequest(nextBusinessDay, sitefloor);
        table2 = "<h3>下次上班日(" + nextBusinessDay + ")請假人員</h3>" + ("".equals(table2) ? "無" : table2);

        if ("".equals(table1) && "".equals(table2)) {
            sb.append("<h3>今日無申請請假人員，以及明日無請假人員。</h3>");
        } else {
            sb.append(table2)
                    .append("<hr/>")
                    .append(table1);
        }
        return sb.toString();
    }

    private String getTodaysLeaveRequest(String sitefloor) {
        LeaveRequestService leaveRequestService = BasicService.getLeaveRequestService();
        return generateTable(leaveRequestService.getTodaysLeaveRequset(sitefloor));
    }

    public String getNextBussinessDayLeaveRequest(String nextBusinessDay, String sitefloor) {
        LeaveRequestService leaveRequestService = BasicService.getLeaveRequestService();
        return generateTable(leaveRequestService.getLeaveRequestByDay(nextBusinessDay, sitefloor));
    }

    private String generateTable(List<LeaveRequest> l) {
        if (l.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\" border:3px #FFAC55 solid; padding:5px; \" rules=\"all\" cellpadding=\"5\">")
                .append("<tr>");
        for (String str : tableHead) {
            sb.append("<th>")
                    .append(str)
                    .append("</th>");
        }
        sb.append("</tr>");
        for (LeaveRequest lr : l) {
            sb.append("<tr><td>")
                    .append(lr.getJobnumber())
                    .append("</td><td>")
                    .append(lr.getName())
                    .append("</td><td>")
                    .append(lr.getLeaveType())
                    .append("</td><td>")
                    .append(lr.getRemark())
                    .append("</td><td>")
                    .append(lr.getLeaveHours())
                    .append("</td><td>")
                    .append(lr.getLeaveFrom())
                    .append("</td><td>")
                    .append(lr.getLeaveTo())
                    .append("</td><td")
                    .append(lr.getReqByUser() == 1 ? "" : " style='color:red'")
                    .append(">")
                    .append(lr.getSaveTime())
                    .append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

}
