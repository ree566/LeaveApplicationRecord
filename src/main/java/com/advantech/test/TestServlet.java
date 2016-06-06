/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.IdentitDAO;
import com.advantech.entity.Identit;
import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;

/**
 *
 * @author Wei.Cheng.
 * Style setting : http://shihshu.blogspot.tw/2010/05/npoi.html , https://dotblogs.com.tw/rickeysu/2013/12/26/136456
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

    private final String SHEETNAME = "sheet1";
    private final String[] dateHeader = {"一月", "二月", "三月", "四月", "五月", "六月", "上半年", "七月", "八月", "九月", "十月", "十一月", "十二月", "下半年", "全年"};
    private final String[] header = {"系統遲到(次數)", "事假(小時)", "病假(小時)", "特別假種(天)", "特休假(小時)", "加班時數(小時)"};
    private final int maxRepeatTime = 15;//按照格式 輸出1-12月 + 上下半年 + 整年 = 15
    private final int userInfoCol = 3;
    private final int maxCols = header.length * maxRepeatTime + userInfoCol;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        //尚未完成，需要把樣式調整成與"6F人員出勤統計表.xlsx相同樣式"
//        res.setContentType("application/vnd.ms-excel");
//        res.setHeader("Content-Disposition", "attachment; filename=filename.xls");
        HSSFWorkbook workbook = generateExcel();
// ...
// Now populate workbook the usual way.
// ...
//        workbook.write(res.getOutputStream()); // Write workbook to response.

        out.print("中文測試");
        try {
            FileOutputStream fOut = new FileOutputStream("C:\\Users\\Wei.Cheng\\Desktop\\test.xls");
            workbook.write(fOut);
            workbook.close();
            fOut.close();
        } catch (Exception e) {
            out.print(e);
        }
    }

    private HSSFWorkbook queryResultToExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("lawix10");
        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell((short) 0).setCellValue("CellHeadName1");
        rowhead.createCell((short) 1).setCellValue("CellHeadName2");
        rowhead.createCell((short) 2).setCellValue("CellHeadName3");
        rowhead.createCell((short) 3).setCellValue("CellHeadName4");
        rowhead.createCell((short) 4).setCellValue("CellHeadName5");
        int i = 1;
        List<Identit> l = BasicService.getIdentitService().getIdentit(3);
        for (Identit identit : l) {
            HSSFRow row = sheet.createRow((short) i);
            row.createCell((short) 0).setCellValue(Integer.toString(identit.getId()));
            row.createCell((short) 1).setCellValue(identit.getJobnumber());
            row.createCell((short) 2).setCellValue(identit.getName());
            row.createCell((short) 3).setCellValue(identit.getDepartment());
            row.createCell((short) 4).setCellValue(identit.getPermission());
            i++;
        }
        return workbook;
    }

    private HSSFWorkbook createAndSetHeader() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String safeName = WorkbookUtil.createSafeSheetName(SHEETNAME);
        Sheet sheet = workbook.createSheet(safeName);//建立工作表
        Row row1 = sheet.createRow((short) 0);//建立工作列

        CellStyle styleRow1 = workbook.createCellStyle();

        return workbook;
    }

    private HSSFWorkbook generateExcel() throws FileNotFoundException, IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();//建立Excel物件
        String safeName = WorkbookUtil.createSafeSheetName(SHEETNAME);
        Sheet sheet = workbook.createSheet(safeName);//建立工作表
        Row row1 = sheet.createRow((short) 0);//建立工作列
        Row row2 = sheet.createRow((short) 1);

        CellStyle styleRow1 = workbook.createCellStyle();
//        styleRow1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        styleRow1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平置中
//        styleRow1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直置中

//設定框線
        styleRow1.setBorderBottom((short) 1);
        styleRow1.setBorderTop((short) 1);
        styleRow1.setBorderLeft((short) 1);
        styleRow1.setBorderRight((short) 1);

        styleRow1.setWrapText(true);//自動換行

        for (int i = 0, j = header.length, k = 0; i < maxCols; i++) {
            Cell cell = row1.createCell(i);//建立儲存格
            cell.setCellStyle(styleRow1);//套用格式

            if (i < userInfoCol) {
                cell.setCellValue("test" + i);//設定內容
            } else {
                if ((i - userInfoCol) % j == 0) {
                    cell.setCellValue(dateHeader[k]);
                    k++;
                }
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        for (int i = userInfoCol, j = header.length; i < maxCols; i += j) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i, i + (j - 1)));
        }

        for (int i = userInfoCol, j = header.length; i < maxCols; i++) {
            Cell cell = row2.createCell(i);//建立儲存格
            cell.setCellStyle(styleRow1);//套用格式
            cell.setCellValue(header[(i - userInfoCol) % j]);//設定內容
        }

        sheet.autoSizeColumn(0);//自動調整欄位寬度
        //儲存檔案
        return workbook;
    }
}
