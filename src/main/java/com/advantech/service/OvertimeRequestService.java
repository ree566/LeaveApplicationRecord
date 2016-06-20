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

    protected OvertimeRequestService() {
        overtimeRequestDAO = new OvertimeRequestDAO();
    }

    public List<OvertimeRequest> getOvertimeRequest() {
        return overtimeRequestDAO.getOvertimeRequest();
    }

    public List<OvertimeRequest> getOvertimeRequest(int userNo) {
        return overtimeRequestDAO.getOvertimeRequest(userNo);
    }

    public List<OvertimeRequest> getOvertimeRequest(String sitefloor, int department) {
        return overtimeRequestDAO.getOvertimeRequest(sitefloor, department);
    }

    public List<OvertimeRequest> getOvertimeRequestBySitefloor(String sitefloor) {
        return overtimeRequestDAO.getOvertimeRequestBySitefloor(sitefloor);
    }

    public boolean isExistOvertimeRequest(int userNo) {
        return getOvertimeRequest(userNo).isEmpty();
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

    public List getOvertimeHistoryBySitefloor(String sitefloor) {
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
        List requestList = getOvertimeRequest(ov.getUserNo());

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

    public boolean userCheckOvertimeRequest(String[] id, int userNo) {
        List l = new ArrayList();
        for (String i : id) {
            if (i == null || "".equals(i)) {
                continue;
            }
            l.add(new OvertimeRequest(Integer.parseInt(i), CHECK_SIGH, userNo));
        }
        return overtimeRequestDAO.changeOvertimeRequestStatus(l);
    }

    public boolean restoreCheckStatus(int[] id, int userNo) {
        List l = new ArrayList();
        for (int i : id) {
            l.add(new OvertimeRequest(i, CHECK_SIGH, userNo));
        }
        return overtimeRequestDAO.changeOvertimeRequestStatus(l);
    }

    public boolean deleteOvertimeRequest(int id) {
        return overtimeRequestDAO.deleteOvertimeRequest(id);
    }

}
