/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Identit;
import com.advantech.model.IdentitDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitService {

    private final IdentitDAO identitDAO;
    private final int servingSign = 1;
    private final int leaveSign = 0;

    protected IdentitService() {
        identitDAO = new IdentitDAO();
    }

    public List<Identit> getIdentit(int userPermission) {
        return identitDAO.getIdentit(userPermission);
    }
    
    public boolean isUserExist(String jobnumber){
        return !identitDAO.getIdentit(jobnumber).isEmpty();
    }

    public List<Identit> getIdentit(int userPermission, String sitefloor) {
        return identitDAO.getIdentit(userPermission, sitefloor);
    }

    public List<Identit> getMailList() {
        return identitDAO.getMailList();
    }

    public List<Identit> userLogin(String jobnumber, String password) {
        return identitDAO.userLogin(jobnumber, password);
    }

    public boolean newIdentit(Identit i) {
        i.setServing(servingSign);
        return identitDAO.newIdentit(packageObjectToList(i));
    }

    public boolean updateIdentit(Identit i) {
        return identitDAO.updateIdentit(packageObjectToList(i));
    }

    private List packageObjectToList(Object... o) {
        List l = new ArrayList();
        l.addAll(Arrays.asList(o));
        return l;
    }

    public boolean updatePassword(int userNo, String password) {
        return identitDAO.updateUsersPassword(userNo, password);
    }

    public boolean deleteIdentit(int userNo) {
        return identitDAO.updateIdentitServingStatus(leaveSign, userNo);
    }
}
