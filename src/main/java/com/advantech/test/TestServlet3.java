/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.service.BasicService;
import java.io.*;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "TestServlet3", urlPatterns = {"/TestServlet3"})
public class TestServlet3 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TestServlet3.class);
    private final int EXCEL_START_ROW = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
//        DailyMailSend.sendMailEverySiteFloor();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

//        res.setContentType("text/html");
//        PrintWriter out = res.getWriter();
        try {

            FileInputStream file = new FileInputStream(new File("C:\\Users\\Wei.Cheng\\Desktop\\example.xlsx"));

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;

            List<Map> l = BasicService.getLeaveRequestService().getTotalLeaveRequestForExeclOutput("2016-01-01", "2016-03-01", "6");

            int startRow = EXCEL_START_ROW;
            for (Map m : l) {
                Row row = sheet.getRow(startRow);
                //Update the value of cell

                int index = 0;

                for (Object key : m.keySet()) {
                    Object value = m.get(key);
                    cell = row.getCell(index);
                    cell.setCellValue(value.toString());
                    index++;
                }

                startRow++;
            }
            file.close();

            FileOutputStream outFile = new FileOutputStream(new File("C:\\Users\\Wei.Cheng\\Desktop\\update.xlsx"));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error(e.toString());
        }

    }
}
