/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.OvertimeRequest;
import com.blogspot.monstersupreme.dataaccess.ConnectionFactory;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class OvertimeRequestDAO {

    private final ConnectionFactory connFactory;

    public OvertimeRequestDAO() {
        connFactory = BasicDAO.getConnFactory();
    }

    private Connection getConn() {
        return connFactory.getConnection();
    }

    public List<OvertimeRequest> getOvertimeRequest() {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail");
    }

    public List<OvertimeRequest> getOvertimeRequest(int userNo) {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail WHERE userNo = ?", userNo);
    }

    public List<OvertimeRequest> getOvertimeRequest(String sitefloor, int department) {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail WHERE sitefloor = ? AND departmentId = ?", sitefloor, department);
    }

    public List<OvertimeRequest> getOvertimeRequestBySitefloor(String sitefloor) {
        return queryOvertimeRequestTable("SELECT * FROM overtimeRequestDetail WHERE sitefloor = ?", sitefloor);
    }

    private List<OvertimeRequest> getOvertimeRequestInPage(String sql, int pageSize, int currentPage) {
        return BasicDAO.select(
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

    public List<Map> getBandonDepartment() {
        return query("SELECT * FROM bandonDepartment");
    }

    public List<Map> getBandon(int departmentId) {
        return query("SELECT * FROM bandonView WHERE department = ?", departmentId);
    }

    public List<OvertimeRequest> getAllOvertimeRequestHistory(int pageSize, int currentPage) {
        return getOvertimeRequestInPage("SELECT * FROM overtimeRequestHistoryView", pageSize, currentPage);
    }

    public List<Map> getOvertimeHistoryBySitefloor(String sitefloor) {
        return query("SELECT * FROM overtimeRequestHistoryView WHERE sitefloor = ?", sitefloor);
    }

    public List<Map> getPersonalOvertimeRequestHistory(int userNo) {
        return query("SELECT * FROM overtimeRequestHistoryView WHERE userNo = ?", userNo);
    }

    private List<OvertimeRequest> queryOvertimeRequestTable(String sql, Object... params) {
        return BasicDAO.select(getConn(), sql, params);
    }

    private List<Map> query(String sql, Object... params) {
        return BasicDAO.select(getConn(), sql, params);
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

    public boolean changeOvertimeRequestStatus(List beanList) {
        return alterOvertimeRequestForBean("UPDATE overtimeRequest SET checkStatus = ?, checkUserNo = ? WHERE id = ?", beanList, "checkStatus", "checkUser", "id");
    }

    private boolean alterOvertimeRequest(String sql, Object... params) {
        return BasicDAO.alterTable(getConn(), sql, params);
    }

    private boolean alterOvertimeRequestForBean(String sql, List beanList, String... propertyNames) {
        return BasicDAO.alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }

}
