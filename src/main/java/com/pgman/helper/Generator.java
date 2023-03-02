package com.pgman.helper;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.pgman.entities.Guest;

public class Generator {

    public static String[] HEADERS = {
            "ID", "NAME", "DOB", "STATUS", "REG.DATE", "OCCUPATION"
    };

    private static Logger logger = LoggerFactory.getLogger(Generator.class);

    public static String SHEET_NAME = "Guest_data";

    public static ByteArrayInputStream dataToExcel(List<Guest> guests) throws IOException {

        // create work book
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            // create sheet
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            // create row
            Row row = sheet.createRow(0);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            // value of rows
            int rowIndex = 1;
            for (Guest g : guests) {

                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                String status = g.isEnabled() ? "Active" : "Deactive";

                dataRow.createCell(0).setCellValue(g.getId());
                dataRow.createCell(1).setCellValue(g.getName());
                dataRow.createCell(2).setCellValue(g.getDob().toString());
                dataRow.createCell(3).setCellValue(status);
                dataRow.createCell(4).setCellValue(g.getRegDate().toString());
                dataRow.createCell(5).setCellValue(g.getOccupation());
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Failed to load data in Excel");
        } finally {

            workbook.close();
            out.close();
        }

        return null;

    }

}
