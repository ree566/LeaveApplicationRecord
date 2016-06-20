/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Holiday;
import com.advantech.model.HolidayDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class HolidayService {

    private final HolidayDAO holidayDAO;

    protected HolidayService() {
        holidayDAO = new HolidayDAO();
    }

    public List<Holiday> getSpecialDays() {
        return holidayDAO.getSpecialDays();
    }

    public List<Holiday> getSpecialDays(int month) {
        return holidayDAO.getSpecialDays(month);
    }

    public List<Holiday> getSpecialSaturday(int month) {
        return holidayDAO.getSpecialSaturday(month);
    }

    public boolean isSpecialDay(String date) {
        return holidayDAO.isSpecialDay(date);
    }

    public boolean newHoliday(List beanList) {
        return holidayDAO.insertHoliday(beanList);
    }

    public boolean updateHoliday(List beanList) {
        return holidayDAO.updateHoliday(beanList);
    }

    public boolean deleteHoliday(List beanList) {
        return holidayDAO.deleteHoliday(beanList);
    }
}
