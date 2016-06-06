/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

/**
 *
 * @author Wei.Cheng
 */
public class Pagenation {

    //总记录条数
    private int rowCount = 0;
    //总页数
    private int pageCount = 0;
    //当前页数
    private int currentpage = 1;
    //页面显示数目
    private int pageSize = 10;
    //action名称（实现）
    private String actionName;
    //查询语句
    private String sql;
    private String hql;

    //构造方法
    public Pagenation() {
        super();
    }
    //有参构造函数

    public Pagenation(int rowCount, int pageCount, int currentpage, int pageSize) {
        super();
        this.currentpage = currentpage;
        this.rowCount = rowCount;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        //很好的分页算法.也可以换成可以换成
        //this.pageCount=rowCount%this.pageSize==0?rowCount/this.pageSize:rowCount/this.pageSize+1;
        this.pageCount = (rowCount + this.pageSize - 1) / this.pageSize;
    }
    //开始记录条数

    public Integer getStartRow() {
        return (getCurrentpage() - 1) * getPageSize();
    }
    //结束记录条数

    public Integer getEndRow() {
        return getCurrentpage() * getPageSize();
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentpage() {
        if (currentpage > pageCount) {
            currentpage = pageCount;
        }
        if (currentpage < 1) {
            currentpage = 1;
        }
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }
}
