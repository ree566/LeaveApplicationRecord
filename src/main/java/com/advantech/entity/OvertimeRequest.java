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
public class OvertimeRequest {

    private int id;
    private int userNo;
    private String userName;
    private double overtimeHours;
    private int bandonId;
    private String bandonName;
    private String bandonDepartmentName;
    private int reqByUser;
    private String saveTime;
    private int sitefloor;
    private int checkStatus;

    public OvertimeRequest() {
    }

    public OvertimeRequest(int userNo, double overtimeHours, int reqByUser) {
        this.userNo = userNo;
        this.overtimeHours = overtimeHours;
        this.reqByUser = reqByUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public int getBandonId() {
        return bandonId;
    }

    public void setBandonId(int bandonId) {
        this.bandonId = bandonId;
    }

    public int getReqByUser() {
        return reqByUser;
    }

    public void setReqByUser(int reqByUser) {
        this.reqByUser = reqByUser;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBandonName() {
        return bandonName;
    }

    public void setBandonName(String bandonName) {
        this.bandonName = bandonName;
    }

    public String getBandonDepartmentName() {
        return bandonDepartmentName;
    }

    public void setBandonDepartmentName(String bandonDepartmentName) {
        this.bandonDepartmentName = bandonDepartmentName;
    }

    public int getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(int sitefloor) {
        this.sitefloor = sitefloor;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public boolean isChecked(OvertimeRequest o) {
        return getCheckStatus() == 1;
    }

}
