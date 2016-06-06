/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.LeaveRequest;
import com.advantech.helper.DateParser;
import com.advantech.model.LeaveRequestDAO;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class LeaveRequestService {

    private final LeaveRequestDAO leaveRequestDAO;

    private final int WORK_HOURS_PERDAY = 9;
    private final int LIMIT_PEOPLE_PERDAY = 2;

    public LeaveRequestService() {
        leaveRequestDAO = new LeaveRequestDAO();
    }

    public List<LeaveRequest> getLeaveRequest() {
        return leaveRequestDAO.getLeaveRequest();
    }

    public List<LeaveRequest> getLeaveRequestDetailInPage(int pageSize, int currentPage) {
        return leaveRequestDAO.getLeaveRequestDetailInPage(pageSize, currentPage);
    }

    public List<Map> getLeaveType() {
        return leaveRequestDAO.getLeaveType();
    }

    public List<Map> getLeaveReason() {
        return leaveRequestDAO.getLeaveReason();
    }

    public List<Map> getAllDepartment() {
        return leaveRequestDAO.getAllDepartment();
    }

    public List<LeaveRequest> getTomorrowsLeaveRequest(int sitefloor) {
        return leaveRequestDAO.getTomorrowsLeaveRequest(sitefloor);
    }

    public List<LeaveRequest> getLeaveRequestBySitefloor(int sitefloor) {
        return leaveRequestDAO.getLeaveRequestBySitefloor(sitefloor);
    }

    public List<LeaveRequest> getPersonalRequest(int userNo) {
        return leaveRequestDAO.getPersonalRequest(userNo);
    }

    public List<LeaveRequest> getTodaysLeaveRequset(int sitefloor) {
        return leaveRequestDAO.getTodaysLeaveRequset(sitefloor);
    }

    public List<Map> getPersonalTotalLeaveRequest(String startDate, String endDate, int userNo) {
        return leaveRequestDAO.getPersonalTotalLeaveRequest(startDate, endDate, userNo);
    }

    public List<Map> getAllTotalLeaveRequest(String startDate, String endDate) {
        return leaveRequestDAO.getAllTotalLeaveRequest(startDate, endDate);
    }

    public List<Map> getTotalLeaveRequestBySitefloor(String startDate, String endDate, int sitefloor) {
        return leaveRequestDAO.getTotalLeaveRequestBySitefloor(startDate, endDate, sitefloor);
    }

    public List<Map> getTotalLeaveRequestForExeclOutput(String startDate, String endDate, int sitefloor) {
        return leaveRequestDAO.getTotalLeaveRequestForExeclOutput(startDate, endDate, sitefloor);
    }

    public List<Map> getLeaveRequestPeopleAmount(String dateBegin, String dateEnd, int department, int sitefloor) {
        return leaveRequestDAO.getLeaveRequestPeopleAmount(dateBegin, dateEnd, department, sitefloor);
    }

    public List<Map> getLeaveRequestInDay(String date, int department, int sitefloor) {
        return leaveRequestDAO.getLeaveRequestInDay(date, department, sitefloor);
    }

    public boolean isPersonDataInDayExist(LeaveRequest l) {
        return leaveRequestDAO.isPersonDataInDayExist(l);
    }

    public List peopleLimitCheck(String beginTime, String endTime, int userDepartment, int userSitefloor) throws JSONException {
        List list = new ArrayList();
        if (DateParser.dateDiff(beginTime, endTime) >= WORK_HOURS_PERDAY) {
            List l = getLeaveRequestPeopleAmount(beginTime, endTime, userDepartment, userSitefloor);
            if (!l.isEmpty()) {
                JSONArray ja = new JSONArray(new Gson().toJson(l));
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject obj = ja.getJSONObject(i);
                    String date = obj.getString("DATE");
                    int requestPeople = (int) obj.get("amount");
                    if (requestPeople >= LIMIT_PEOPLE_PERDAY) {
                        list = getLeaveRequestInDay(date, userDepartment, userSitefloor);
                        break;
                    }
                }
            }
        }
        return list;
    }

    public String newLeaveRequest(LeaveRequest l) {
        boolean isRequestDateDuplicate = isPersonDataInDayExist(l);
        if (isRequestDateDuplicate) {
            return "申請失敗，申請的時間區域中已經請過其他假，請從歷史查詢中確認請假時間。";
        } else {
            List list = new ArrayList();
            list.add(l);
            return insertLeaveRequest(list) ? "true" : "false";
        }
    }

    public boolean insertLeaveRequest(List beanList) {
        return leaveRequestDAO.insertLeaveRequest(beanList);
    }

    public boolean deleteLeaveRequest(int id) {
        return leaveRequestDAO.deleteLeaveRequest(id);
    }

    public boolean updateLeaveRequest(List beanList) {
        return leaveRequestDAO.updateLeaveRequest(beanList);
    }

}
