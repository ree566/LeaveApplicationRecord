/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Identit;
import com.blogspot.monstersupreme.dataaccess.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitDAO {

    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);
    private final ConnectionFactory connFactory;

    public IdentitDAO() {
        connFactory = BasicDAO.getConnFactory();
    }

    private Connection getConn() {
        return connFactory.getConnection();
    }

    public List<Identit> getIdentit(int userPermission) {
        return queryIdentitTable("SELECT * FROM identit where permission <= ?", userPermission);
    }

    public List<Identit> getIdentit(int userPermission, String sitefloor) {
        return queryIdentitTable("SELECT * FROM identit where permission <= ? and sitefloor = ?", userPermission, sitefloor);
    }

    public List<Identit> getMailList() {
        return queryIdentitTable("SELECT * FROM identitMailTargetView");
    }

    public List<Identit> userLogin(String jobnumber, String password) {
        return queryIdentitTable("SELECT * FROM identit WHERE jobnumber = ? AND password = ?", jobnumber, password);
    }

    private List<Identit> queryIdentitTable(String sql, Object... params) {
        return BasicDAO.select(getConn(), Identit.class, sql, params);
    }

    public boolean newIdentit(List beanList) {
        return alterIdentitForBean(
                "INSERT INTO identit(jobnumber, password, name, department, permission, sitefloor, email) VALUES(?,?,?,?,?,?,?)",
                beanList,
                "jobnumber", "password", "name", "department", "permission", "sitefloor", "email");
    }

    public boolean updateIdentit(List beanList) {
        return alterIdentitForBean(
                "UPDATE identit SET password = ?, name = ?, department = ?, permission = ?, sitefloor = ? , email=? WHERE id = ?",
                beanList,
                "password", "name", "department", "permission", "sitefloor", "email", "id");
    }

    public boolean updateUsersPassword(int userNo, String password) {
        return alterIdentit("UPDATE identit SET password = ? WHERE id = ?", userNo, password);
    }

    public boolean deleteIdentit(int userNo) {
        Connection conn = getConn();
        QueryRunner qRunner = new QueryRunner();

        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            qRunner.update(conn, "DELETE FROM leaveRequest WHERE userNo = ?", userNo);
            qRunner.update(conn, "DELETE FROM overtimeRequest WHERE userNo = ?", userNo);
            qRunner.update(conn, "DELETE FROM identit WHERE id = ?", userNo);
            DbUtils.commitAndClose(conn);
            flag = true;
        } catch (SQLException e) {
            // do not retry if we get any other error
            DbUtils.rollbackAndCloseQuietly(conn);
            log.error("Error has occured - Error Code: "
                    + e.getErrorCode() + " SQL STATE :"
                    + e.getSQLState() + " Message : " + e.getMessage());
        }
        return flag;
    }

    private boolean alterIdentit(String sql, Object... params) {
        return BasicDAO.alterTable(getConn(), sql, params);
    }

    private boolean alterIdentitForBean(String sql, List beanList, String... propertyNames) {
        return BasicDAO.alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }
}
