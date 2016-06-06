/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.OvertimeRequest;
import com.advantech.model.OvertimeRequestDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class OvertimeRequestService {

    private final OvertimeRequestDAO overtimeRequestDAO;

    private final int CHECK_SIGH = 1;
    private final int UNCHECK_SIGH = 0;

    private final double limitHourForBandon = 2;

    public OvertimeRequestService() {
        overtimeRequestDAO = new OvertimeRequestDAO();
    }

    public List<OvertimeRequest> getOvertimeRequest() {
        return overtimeRequestDAO.getOvertimeRequest();
    }

    public List<OvertimeRequest> getOvertimeBySitefloor(int sitefloor) {
        return overtimeRequestDAO.getOvertimeBySitefloor(sitefloor);
    }

    public boolean isExistOvertimeRequest(int userNo) {
        return getPersonalOvertimeRequest(userNo).isEmpty();
    }

    public List<OvertimeRequest> getPersonalOvertimeRequest(int userNo) {
        return overtimeRequestDAO.getPersonalOvertimeRequest(userNo);
    }

    public List getBandonDepartment() {
        return overtimeRequestDAO.getBandonDepartment();
    }

    public List getBandonByDepartment(int departmentId) {
        return overtimeRequestDAO.getBandon(departmentId);
    }

    public List getAllOvertimeRequestHistory(int pageSize, int currentPage) {
        return overtimeRequestDAO.getAllOvertimeRequestHistory(pageSize, currentPage);
    }

    public List getOvertimeHistoryBySitefloor(int sitefloor) {
        return overtimeRequestDAO.getOvertimeHistoryBySitefloor(sitefloor);
    }

    public List getPersonalOvertimeRequestHistory(int userNo) {
        return overtimeRequestDAO.getPersonalOvertimeRequestHistory(userNo);
    }

    public JSONArray getBandon() {
        JSONArray allBandonList = new JSONArray();
        List<Map> departments = getBandonDepartment();
        for (Map dep : departments) {
            String departmentName = (String) dep.get("name");
            int departmentId = (int) dep.get("id");
            List bandonList = getBandonByDepartment(departmentId);
            allBandonList.put(
                    new JSONObject().put("departmentId", departmentId).put("name", departmentName).put("bandonList", bandonList)
            );
        }
        return allBandonList;
    }

    public boolean newOvertimeRequest(OvertimeRequest ov) {
        List requestList = getPersonalOvertimeRequest(ov.getUserNo());

        if (!requestList.isEmpty() || hoursNotEnoughForBandon(ov)) {
            return false;
        }

        List l = new ArrayList();
        ov.setCheckStatus(UNCHECK_SIGH);
        l.add(ov);
        return ov.getBandonId() == 0 ? newOvertimeRequestWithoutBandon(l) : newOvertimeRequest(l);
    }

    private boolean hoursNotEnoughForBandon(OvertimeRequest ov) {
        return ov.getOvertimeHours() < limitHourForBandon && ov.getBandonId() != 0;
    }

    private boolean newOvertimeRequest(List beanList) {
        return overtimeRequestDAO.newOvertimeRequest(beanList);
    }

    private boolean newOvertimeRequestWithoutBandon(List beanList) {
        return overtimeRequestDAO.newOvertimeRequestWithoutBandon(beanList);
    }

    public boolean updateOvertimeRequest(List beanList) {
        return overtimeRequestDAO.updateOvertimeRequest(beanList);
    }

    public boolean userCheckOvertimeRequest(int userNo) {
        return overtimeRequestDAO.changeOvertimeRequestStatus(userNo, CHECK_SIGH);
    }

    public boolean restoreCheckStatus(int userNo) {
        return overtimeRequestDAO.changeOvertimeRequestStatus(userNo, UNCHECK_SIGH);
    }

    public boolean deleteOvertimeRequest(int id) {
        return overtimeRequestDAO.deleteOvertimeRequest(id);
    }

}
