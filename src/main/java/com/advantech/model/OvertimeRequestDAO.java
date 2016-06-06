/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.OvertimeRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class OvertimeRequestDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn();
    }

    public List<OvertimeRequest> getOvertimeRequest() {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail");
    }

    private List<OvertimeRequest> getOvertimeRequestInPage(String sql, int pageSize, int currentPage) {
        return select(
                getConn(),
                "select top " + pageSize + " *"
                + "from ("
                + "select row_number() over(order by id) as rownumber,* from("
                + sql
                + ")t"
                + ")t1 "
                + "where t1.rownumber > ?", currentPage
        );
    }

    public List<OvertimeRequest> getOvertimeBySitefloor(int sitefloor) {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail WHERE sitefloor = ?", sitefloor);
    }

    public List<OvertimeRequest> getPersonalOvertimeRequest(int userNo) {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail WHERE userNo = ?", userNo);
    }

    public List<Map> getBandonDepartment() {
        return query("SELECT * FROM bandonDepartment");
    }

    public List<Map> getBandon(int departmentId) {
        return query("SELECT * FROM bandonView WHERE department = ?", departmentId);
    }

    public List<OvertimeRequest> getAllOvertimeRequestHistory(int pageSize, int currentPage) {
        return getOvertimeRequestInPage("SELECT * FROM overtimeRequestHistoryView", pageSize, currentPage);
    }

    public List<Map> getOvertimeHistoryBySitefloor(int sitefloor) {
        return query("SELECT * FROM overtimeRequestHistoryView WHERE sitefloor = ?", sitefloor);
    }

    public List<Map> getPersonalOvertimeRequestHistory(int userNo) {
        return query("SELECT * FROM overtimeRequestHistoryView WHERE userNo = ?", userNo);
    }

    private List<OvertimeRequest> queryOvertimeRequestTable(String sql, Object... params) {
        return select(getConn(), sql, params);
    }

    private List<Map> query(String sql, Object... params) {
        return select(getConn(), sql, params);
    }

    public boolean newOvertimeRequest(List beanList) {
        return alterOvertimeRequestForBean(
                "INSERT INTO overtimeRequest(userNo,overtimeHours,bandonId,reqByUser,checkStatus) values(?,?,?,?,?)",
                beanList,
                "userNo", "overtimeHours", "bandonId", "reqByUser", "checkStatus"
        );
    }

    public boolean newOvertimeRequestWithoutBandon(List beanList) {
        return alterOvertimeRequestForBean(
                "INSERT INTO overtimeRequest(userNo,overtimeHours,reqByUser,checkStatus) values(?,?,?,?)",
                beanList,
                "userNo", "overtimeHours", "reqByUser", "checkStatus"
        );
    }

    public boolean deleteOvertimeRequest(int id) {
        return alterOvertimeRequest("DELETE FROM overtimeRequest WHERE id = ?", id);
    }

    public boolean updateOvertimeRequest(List beanList) {
        return alterOvertimeRequestForBean(
                "UPDATE overtimeRequest SET overtimeHours = ?, bandonId = ? WHERE id = ?",
                beanList,
                "overtimeHours", "bandonId", "id"
        );
    }

    public boolean changeOvertimeRequestStatus(int userNo, int checkSign) {
        return alterOvertimeRequest("UPDATE overtimeRequest SET checkStatus = ? WHERE userNo = ?", checkSign, userNo);
    }

    private boolean alterOvertimeRequest(String sql, Object... params) {
        return alterTable(getConn(), sql, params);
    }

    private boolean alterOvertimeRequestForBean(String sql, List beanList, String... propertyNames) {
        return alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }

    
}
