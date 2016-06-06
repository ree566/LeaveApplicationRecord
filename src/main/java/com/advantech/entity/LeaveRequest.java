/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.entity;

import java.util.Objects;

/**
 *
 * @author Wei.Cheng
 */
public class LeaveRequest {

    private int id;
    private int userNo;
    private String leaveType;
    private double leaveHours;
    private String leaveFrom;
    private String leaveTo;
    private String saveTime;//db auto getTime
    private String remark;
    private int typeNo;
    private int reasonNo;

    private String name; //userName
    private String jobnumber;
    private int reqByUser;
    private String department;

    public LeaveRequest() {

    }

    public LeaveRequest(int userNo, String leaveFrom, String leaveTo, int typeNo, int reasonNo, int reqByUser) {
        this.userNo = userNo;
        this.leaveFrom = leaveFrom;
        this.leaveTo = leaveTo;
        this.typeNo = typeNo;
        this.reasonNo = reasonNo;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public double getLeaveHours() {
        return leaveHours;
    }

    public void setLeaveHours(double leaveHours) {
        this.leaveHours = leaveHours;
    }

    public String getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(String leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReasonNo() {
        return reasonNo;
    }

    public void setReasonNo(int reasonNo) {
        this.reasonNo = reasonNo;
    }

    public int getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(int typeNo) {
        this.typeNo = typeNo;
    }

    public int getReqByUser() {
        return reqByUser;
    }

    public void setReqByUser(int reqByUser) {
        this.reqByUser = reqByUser;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.userNo;
        hash = 41 * hash + Objects.hashCode(this.leaveType);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.leaveHours) ^ (Double.doubleToLongBits(this.leaveHours) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.leaveFrom);
        hash = 41 * hash + Objects.hashCode(this.leaveTo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LeaveRequest other = (LeaveRequest) obj;
        if (this.userNo != other.userNo) {
            return false;
        }
        if (!Objects.equals(this.leaveType, other.leaveType)) {
            return false;
        }
        if (Double.doubleToLongBits(this.leaveHours) != Double.doubleToLongBits(other.leaveHours)) {
            return false;
        }
        if (!Objects.equals(this.leaveFrom, other.leaveFrom)) {
            return false;
        }
        if (!Objects.equals(this.leaveTo, other.leaveTo)) {
            return false;
        }
        return true;
    }

    public static void main(String[] arg0) {
    }
}
