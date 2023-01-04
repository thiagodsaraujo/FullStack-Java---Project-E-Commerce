package com.titashop.admin.user;

import com.titashop.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserExcelExporter extends AbstractExporter{

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public UserExcelExporter(){
        workbook = new XSSFWorkbook();

    }


    private void writeHeaderLine(){
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        var cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "E-mail", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
        createCell(row, 6, "Client Since", cellStyle);
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){

        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer){
        cell.setCellValue((Integer)value);
        } else if (value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);

    }

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/octet-stream",".xlsx");
        writeHeaderLine();
        writeDataLines(listUsers);


        var outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> listUsers) {
        int rowIndex = 1;

        var cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setFontHeight(12);
        cellStyle.setFont(font);

        for(User user : listUsers){
            var row = sheet.createRow(rowIndex++);
            int columIndex = 0;

            createCell(row, columIndex++, user.getId(), cellStyle);
            createCell(row, columIndex++, user.getEmail(), cellStyle);
            createCell(row, columIndex++, user.getFirstName(), cellStyle);
            createCell(row, columIndex++, user.getLastName(), cellStyle);
            createCell(row, columIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columIndex++, user.isEnabled(), cellStyle);
            createCell(row, columIndex, user.getCreatedDate().toString(), cellStyle);
        }
    }

}
