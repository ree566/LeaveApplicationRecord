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
public class Identit {

    private int id;
    private String jobnumber;
    private String password;
    private String name;
    private int department;
    private int permission;
    private String sitefloor;
    private String email;

    public Identit() {

    }

    public Identit(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public Identit(int id, String jobnumber, String password, String name, int department, int permission, String sitefloor, String email) {
        this.id = id;
        this.jobnumber = jobnumber;
        this.password = password;
        this.name = name;
        this.department = department;
        this.permission = permission;
        this.sitefloor = sitefloor;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getSitefloor() {
        return sitefloor;
    }

    public void setSitefloor(String sitefloor) {
        this.sitefloor = sitefloor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
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
        final Identit other = (Identit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public static void main(String[] arg0) {
    }
}
