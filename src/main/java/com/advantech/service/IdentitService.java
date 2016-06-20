/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Identit;
import com.advantech.model.IdentitDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitService {

    private final IdentitDAO identitDAO;

    protected IdentitService() {
        identitDAO = new IdentitDAO();
    }

    public List<Identit> getIdentit(int userPermission) {
        return identitDAO.getIdentit(userPermission);
    }

    public List<Identit> getIdentit(int userPermission, int sitefloor) {
        return identitDAO.getIdentit(userPermission, sitefloor);
    }

    public List<Identit> getMailList() {
        return identitDAO.getMailList();
    }

    public List<Identit> userLogin(String jobnumber, String password) {
        return identitDAO.userLogin(jobnumber, password);
    }

    public boolean newIdentit(List beanList) {
        return identitDAO.newIdentit(beanList);
    }

    public boolean updateIdentit(List beanList) {
        return identitDAO.updateIdentit(beanList);
    }

    public boolean updatePassword(int userNo, String password) {
        return identitDAO.updateUsersPassword(userNo, password);
    }

    public boolean deleteIdentit(int userNo) {
        return identitDAO.deleteIdentit(userNo);
    }
}
