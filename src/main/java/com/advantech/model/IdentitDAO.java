/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Identit;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitDAO {

    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);

    private Connection getConn() {
        return BasicDAO.getConn();
    }

    public List<Identit> getIdentit(String jobnumber) {
        return queryIdentitTable("SELECT * FROM identitView WHERE jobnumber = ?", jobnumber);
    }

    public List<Identit> getIdentit(int userPermission) {
        return queryIdentitTable("SELECT * FROM identitView WHERE permission <= ?", userPermission);
    }

    public List<Identit> getIdentit(int userPermission, String sitefloor) {
        return queryIdentitTable("SELECT * FROM identitView WHERE permission <= ? and sitefloor = ?", userPermission, sitefloor);
    }

    public List<Identit> getMailList() {
        return queryIdentitTable("SELECT * FROM identitMailTargetView");
    }

    public List<Map> getAllDepartment() {
        return query("SELECT * FROM department");
    }

    public List<Map> getAllUserLineType() {
        return query("SELECT * FROM userLineType");
    }

    public List<Map> getAllUserLineType(String sitefloor) {
        return query("SELECT * FROM userLineType where sitefloor = ?", sitefloor);
    }

    public List<Identit> userLogin(String jobnumber, String password) {
        return queryIdentitTable("SELECT * FROM identitView WHERE jobnumber = ? AND password = ?", jobnumber, password);
    }

    private List<Identit> queryIdentitTable(String sql, Object... params) {
        return BasicDAO.select(getConn(), Identit.class, sql, params);
    }

    public boolean newIdentit(List beanList) {
        return alterIdentitForBean(
                "INSERT INTO identit(jobnumber, password, name, lineType, department, permission, sitefloor, email, serving) VALUES(?,?,?,?,?,?,?,?,?)",
                beanList,
                "jobnumber", "password", "name", "lineType", "department", "permission", "sitefloor", "email", "serving");
    }

    public boolean updateIdentit(List beanList) {
        return alterIdentitForBean(
                "UPDATE identit SET password = ?, name = ?, lineType = ?, department = ?, permission = ?, sitefloor = ? , email=? WHERE id = ?",
                beanList,
                "password", "name", "lineType", "department", "permission", "sitefloor", "email", "id");
    }

    public boolean updateUsersPassword(int userNo, String password) {
        return alterIdentit("UPDATE identit SET password = ? WHERE id = ?", password, userNo);
    }

    public boolean updateIdentitServingStatus(int status, int userNo) {
        return alterIdentit("UPDATE identit SET serving = ? WHERE id = ?", status, userNo);
    }

    private List<Map> query(String sql, Object... params) {
        return BasicDAO.select(getConn(), sql, params);
    }

    private boolean alterIdentit(String sql, Object... params) {
        return BasicDAO.alterTable(getConn(), sql, params);
    }

    private boolean alterIdentitForBean(String sql, List beanList, String... propertyNames) {
        return BasicDAO.alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }
}
