/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.advantech.entity.Holiday;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class HolidayDAO {

    private Connection getConn() {
        return BasicDAO.getConn();
    }

    public List getSpecialDays() {
        return queryHolidayTable("SELECT * FROM specialDaysView");
    }

    public List getSpecialDays(int month) {
        return queryHolidayTable("SELECT * FROM specialDaysView where inMonth = ?", month);
    }

    public List<Holiday> getSpecialDays(String date) {
        return queryHolidayTable("SELECT * FROM specialDaysView WHERE ? BETWEEN dateFrom AND dateTo", date);
    }

    public List getSpecialSaturday(int month) {
        return queryHolidayTable("SELECT * FROM specialDaysView WHERE DATEPART(dw, dateFrom) = 7 AND dateFrom = dateTo AND inMonth = ?", month);
    }

    private List queryHolidayTable(String sql, Object... params) {
        return BasicDAO.select(getConn(), Holiday.class, sql, params);
    }

    public boolean insertHoliday(List beanList) {
        return alterHoliday(
                "INSERT INTO specialDays(name, dateFrom, dateTo) VALUES(?,?,?)",
                beanList,
                "name", "dateFrom", "dateTo"
        );
    }

    public boolean updateHoliday(List beanList) {
        return alterHoliday(
                "UPDATE specialDays SET name = ?, dateFrom = ?, dateTo = ? WHERE id = ?",
                beanList,
                "name", "dateFrom", "dateTo", "id"
        );
    }

    public boolean deleteHoliday(List beanList) {
        return alterHoliday(
                "DELETE specialDays WHERE id = ?",
                beanList,
                "id"
        );
    }

    private boolean alterHoliday(String sql, List beanList, String... propertyNames) {
        return BasicDAO.alterTableWithBean(getConn(), sql, beanList, propertyNames);
    }
}
