package com.titashop.admin.user.export;

import com.titashop.admin.user.AbstractExporter;
import com.titashop.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExport extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv",".csv");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name","Created Date","Enabled", "Roles"};
        String[] fieldMapping = {"id", "email", "firstname", "lastname","createdDate","enabled", "roles"};

        csvBeanWriter.writeHeader(csvHeader);

        for (User user : listUsers){
            csvBeanWriter.write(user, fieldMapping);
        }

        csvBeanWriter.close();

    }
}
