/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.LeaveRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class LeaveRequestDAO {

    private Connection getConn() {
        return BasicDAO.getConn();
    }

    public List<LeaveRequest> getLeaveRequest() {
        return queryLeaveRequestTable("SELECT * FROM leaveRequestDetail");
    }

    public List<LeaveRequest> getLeaveRequest(String startDate, String endDate) {
        return queryLeaveRequestTable("SELECT * FROM leaveRequestIn(?,?)", startDate, endDate);
    }

    private List<LeaveRequest> getLeaveRequestInPage(String sql, int pageSize, int currentPage) {
        return BasicDAO.select(
                getConn(),
                "select top " + pageSize + " *"
                + "from ("
                + "select row_number() over(order by id) as rownumber,* from("
                + sql
                + ")t"
                + ")t1 "
                + "WHERE t1.rownumber > ?", currentPage
        );
    }

    public List<LeaveRequest> getLeaveRequestDetailInPage(int pageSize, int currentPage) {
        return getLeaveRequestInPage("SELECT * FROM leaveRequestDetail", pageSize, currentPage);
    }

    public List<Map> getLeaveType() {
        return query("SELECT * FROM leaveType");
    }

    public List<Map> getLeaveReason() {
        return query("SELECT * FROM leaveReason");
    }

    public List<LeaveRequest> getTomorrowsLeaveRequest(String sitefloor) {
        return queryLeaveRequestTable("SELECT * FROM tomorrowsLeaveRequest WHERE sitefloor = ?", sitefloor);
    }

    public List<LeaveRequest> getLeaveRequestBySitefloor(String startDate, String endDate, String sitefloor) {
        return queryLeaveRequestTable("SELECT * FROM leaveRequestIn(?,?) WHERE sitefloor = ?", startDate, endDate, sitefloor);
    }

    public List<LeaveRequest> getPersonalRequest(String startDate, String endDate, int userNo) {
        return queryLeaveRequestTable("SELECT * FROM leaveRequestIn(?,?) WHERE userNo = ?", startDate, endDate, userNo);
    }

    public List<LeaveRequest> getTodaysLeaveRequset(String sitefloor) {
        return queryLeaveRequestTable("SELECT * FROM todaysLeaveRequest WHERE sitefloor = ?", sitefloor);
    }

    public List<Map> getPersonalTotalLeaveRequest(String startDate, String endDate, int userNo) {
        return query("SELECT * FROM getTotalLeaveRequestByMonth(?,?) WHERE userNo = ?", startDate, endDate, userNo);
    }

    public List<Map> getAllTotalLeaveRequest(String startDate, String endDate) {
        return query("SELECT * FROM getTotalLeaveRequestByMonth(?,?)", startDate, endDate);
    }

    public List<Map> getTotalLeaveRequestBySitefloor(String startDate, String endDate, String sitefloor) {
        return query("SELECT * FROM getTotalLeaveRequestByMonth(?,?) WHERE sitefloor = ?", startDate, endDate, sitefloor);
    }

    public List<Map> getTotalLeaveRequestForExeclOutput(String startDate, String endDate, String sitefloor) {
        return query("SELECT * FROM dbo.tableViewForExcel(?,?) WHERE sitefloor = ? ORDER BY jobnumber", startDate, endDate, sitefloor);
    }

    private List<Map> query(String sql, Object... params) {
        return BasicDAO.select(getConn(), sql, params);
    }

    public List<Map> getLeaveRequestPeopleAmount(String dateBegin, String dateEnd, int department, String sitefloor) {
        return BasicDAO.selectProc(getConn(), "{CALL leaveRequestCheck_1 ?,?,?,?}", dateBegin, dateEnd, department, sitefloor);
    }

    public List<LeaveRequest> getLeaveRequestByDay(String date, String sitefloor) {
        return BasicDAO.selectProc(getConn(), LeaveRequest.class, "{CALL getLeaveRequestByDate ?,?}", date, sitefloor);
    }

    public List<Map> getLeaveRequestInDay(String date, int department, String sitefloor) {
        return BasicDAO.selectProc(getConn(), "{CALL getLeaveRequestInDay_1 ?,?,?}", date, department, sitefloor);
    }

    public boolean isPersonDataInDayExist(LeaveRequest l) {
        return !queryLeaveRequestTable("SELECT * FROM leaveRequestIn(?,?) WHERE userNo = ?", l.getLeaveFrom(), l.getLeaveTo(), l.getUserNo()).isEmpty();
    }

    private List<LeaveRequest> queryLeaveRequestTable(String sql, Object... params) {
        return BasicDAO.select(getConn(), LeaveRequest.class, sql, params);
    }

    public boolean insertLeaveRequest(List beanList) {
        return alterLeaveRequestForBean(
                "INSERT INTO leaveRequest(userNo, typeNo, leaveFrom, leaveTo, reasonNo, reqByUser) values(?,?,?,?,?,?)",
                beanList,
                "userNo", "typeNo", "leaveFrom", "leaveTo", "reasonNo", "reqByUser"
        );
    }

    public boolean deleteLeaveRequest(int id) {
        return alterLeaveRequest("DELETE FROM leaveRequest WHERE id = ?", id);
    }

    public boolean updateLeaveRequest(List beanList) {
        return alterLeaveRequestForBean(
                "UPDATE leaveRequest SET typeNo = ?, leaveFrom = ?, leaveTo = ?, reasonNo = ?, reqByUser = ? WHERE id = ?",
                beanList,
                "typeNo", "leaveFrom", "leaveTo", "reasonNo", "reqByUser", "id"
        );
    }

    private boolean alterLeaveRequest(String sql, Object... params) {
        return BasicDAO.alterTable(getConn(), sql, params);
    }

    private boolean alterLeaveRequestForBean(String sql, List beanList, String... propertyNames) {
        return BasicDAO.alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }
}
