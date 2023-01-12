package com.titashop.admin.user.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.titashop.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf");

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.RED);

        Paragraph paragraph = new Paragraph("* List of Users * ", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setSpacingAfter(5);
        table.setWidths(new float[] {1.0f,1.0f,3.5f,3.0f,3.0f,3.0f,1.5f,2.0f});
        writeTableHeader(table);
        writeTableData(table, listUsers);

        document.add(table);

        document.close();

    }

    private void writeTableData(PdfPTable table, List<User> listUsers) {
        int qntd = 0;
        for(User user : listUsers){
            qntd++;
            table.addCell(String.valueOf(qntd));
            table.addCell(String.valueOf(user.getId()));
            table.addCell(String.valueOf(user.getEmail()));
            table.addCell(String.valueOf(user.getFirstName()));
            table.addCell(String.valueOf(user.getLastName()));
            table.addCell(String.valueOf(user.getRoles()));
            table.addCell(String.valueOf(user.isEnabled()));
            table.addCell(String.valueOf(user.getCreatedDate()));

        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.COURIER);
        font.setSize(10);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("#", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Client Since", font));
        table.addCell(cell);
    }
}
