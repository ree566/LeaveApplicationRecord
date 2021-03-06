/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.helper.ProcRunner;
import com.blogspot.monstersupreme.dataaccess.ConnectionFactory;
import com.blogspot.monstersupreme.dataaccess.UserTransactionFactory;
import com.blogspot.monstersupreme.dataaccess.XUserTransaction;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BasicDAO implements Serializable {

    /*
     change the database driver because of
     http://www.javaworld.com.tw/jute/post/view?bid=6&id=131471&tpg=1&ppg=1&sty=1&age=0
     http://www.javaworld.com.tw/jute/post/view?bid=21&id=366&sty=1&tpg=1&age=-1
        
     old: "jdbc:sqlserver://NB991001\\KEVIN;databaseName=Internal_Check";
     the different is jtds no need to provide the instance name
    
     http://simon-tech.blogspot.tw/2012/02/tomcat-datasource.html
     建議將 JDBC driver 放在 $CATALINA_BASE/lib 路徑下，以免造成 JRE Memory Leak 的問題
    
     QueryRunner負責CRUD, ProcedureRunner只負責Read
     */
    private static final Logger log = LoggerFactory.getLogger(BasicDAO.class);

    private static QueryRunner qRunner = null;
    private static ProcRunner pRunner = null;

    private static ConnectionFactory connFactory = null;
    private static UserTransactionFactory txFactory = null;

    private static final int RETRY_WAIT_TIME = 3000;

    public static void init() {
        try {
            qRunner = new QueryRunner();
            pRunner = new ProcRunner();
//        connFactory = new ConnectionFactory("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://M3-SERVER/LeaveApplicationRecord", "waychien", "m3server");
            connFactory = new ConnectionFactory();
            connFactory.setDataSource(getDataSource());
            txFactory = new UserTransactionFactory();
            txFactory.setConnectionFactory(connFactory);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    protected static void main(String arg0[]) {
    }

    private static DataSource getDataSource() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("jdbc/leaveApp");
        return dataSource;
    }

    protected static Connection getConn() {
        return openConn();
    }

//    synchronized　
    private synchronized static Connection openConn() {
        return connFactory.getConnection();
    }

    protected static List select(Connection conn, String sql, Object... params) {
        return query(conn, new MapListHandler(), sql, params);
    }

    protected static List select(Connection conn, Class cls, String sql, Object... params) {
        return query(conn, new BeanListHandler(cls), sql, params);
    }

    private static List query(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List<?> data = null;
        XUserTransaction tx = txFactory.getUserTransaction();
        try {
            tx.begin();
            data = (List) qRunner.query(conn, sql, rsh, params);
            tx.commit();
        } catch (SQLException e) {
            log.error(e.toString());
            tx.rollback();
        }
        return data == null ? new ArrayList() : data;
    }

    protected static List selectProc(Connection conn, Class cls, String sql, Object... params) {
        return queryProc(conn, new BeanListHandler(cls), sql, params);
    }

    protected static List selectProc(Connection conn, String sql, Object... params) {
        return queryProc(conn, new MapListHandler(), sql, params);
    }

    private static List queryProc(Connection conn, ResultSetHandler rsh, String sql, Object... params) {
        List<?> data = null;
        XUserTransaction tx = txFactory.getUserTransaction();
        try {
            tx.begin();
            data = (List) pRunner.queryProc(conn, sql, rsh, params);
            tx.commit();
        } catch (SQLException e) {
            log.error(e.toString());
            tx.rollback();
        }
        return data == null ? new ArrayList() : data;
    }

    protected static boolean alterTable(Connection conn, String sql, Object... params) {
        boolean flag = false;
        XUserTransaction tx = txFactory.getUserTransaction();
        try {

            tx.begin();
            qRunner.update(conn, sql, params);
            tx.commit();
            flag = true;
        } catch (SQLException e) {
            // do not retry if we get any other error
            log.error("Error has occured - Error Code: "
                    + e.getErrorCode() + " SQL STATE :"
                    + e.getSQLState() + " Message : " + e.getMessage());
            tx.rollback();
        }
        return flag;
    }

    protected static boolean alterTableWithBean(Connection conn, String sql, List<Object> beanList, String... propertyNames) {
        boolean flag = false;
        XUserTransaction tx = txFactory.getUserTransaction();
        try {
            tx.begin();
            PreparedStatement ps = conn.prepareStatement(sql);
            for (Object o : beanList) {
                qRunner.fillStatementWithBean(ps, o, propertyNames);
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
            flag = true;
            tx.commit();
        } catch (SQLException e) {
            log.error(e.toString());
            tx.rollback();
        }
        return flag;
    }

    public static void cleanUpSource() {
//        qRunner = null;
//        connFactory = null;
//        txFactory = null;
    }

}
