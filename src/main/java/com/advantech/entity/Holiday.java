/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

/**
 *
 * @author Wei.Cheng
 */
public class Holiday {
    private int id;
    private String name;
    private String dateFrom;
    private String dateTo;
    private int inMonth;
    private int businessDay;
    
    public Holiday(){
        
    }

    public Holiday(int id, String name, String dateFrom, String dateTo) {
        this.id = id;
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getInMonth() {
        return inMonth;
    }

    public void setInMonth(int inMonth) {
        this.inMonth = inMonth;
    }

    public int getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(int businessDay) {
        this.businessDay = businessDay;
    }
    
}
