/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

/**
 *
 * @author Wei.Cheng
 */
public class BasicService{
    private static final HolidayService holidayService;
    private static final IdentitService identitService;
    private static final LeaveRequestService leaveRequestService;
    private static final OvertimeRequestService overtimeRequestService;

    static{
        holidayService = new HolidayService();
        identitService = new IdentitService();
        leaveRequestService = new LeaveRequestService();
        overtimeRequestService = new OvertimeRequestService();
    }

    public static HolidayService getHolidayService() {
        return holidayService;
    }

    public static IdentitService getIdentitService() {
        return identitService;
    }

    public static LeaveRequestService getLeaveRequestService() {
        return leaveRequestService;
    }

    public static OvertimeRequestService getOvertimeRequestService() {
        return overtimeRequestService;
    }

}
