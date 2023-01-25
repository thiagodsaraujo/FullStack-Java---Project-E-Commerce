package com.titashop.admin.category.controller;

import com.titashop.admin.AbstractExporter;
import com.titashop.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {


    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "text/csv", ".csv", "categories_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID", "CategoryName"};
        String[] fieldMapping = {"id", "name"};

        csvBeanWriter.writeHeader(csvHeader);

        for (Category category : listCategories){
            category.setName(category.getName().replace("--", " "));
            csvBeanWriter.write(category, fieldMapping);
        }
        csvBeanWriter.close();

    }
}
