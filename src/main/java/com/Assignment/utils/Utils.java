package com.Assignment.utils;

import com.Assignment.controller.ProductController;
import com.Assignment.entity.Product;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static boolean checkCSVFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("text/csv");
    }

    public static List<Product> convertCSVToListOfProduct(InputStream is) throws Exception {
        List<Product> list = new ArrayList<>();
        CSVReader csvReader = new CSVReader(new InputStreamReader(is));
        String[] values = null;
        csvReader.readNext();
        while ((values = csvReader.readNext()) != null) {
            Integer id = null;
            String code = values[0];
            String name = values[1];
            String batch = values[2];
            Integer stock = Integer.parseInt(values[3]);
            Integer deal = Integer.parseInt(values[4]);
            Integer free = Integer.parseInt(values[5]);
            Double mrp = Double.parseDouble(values[6]);
            Double rate = Double.parseDouble(values[7]);
            String dateString = values[8];
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                logger.error("Failed to Parse Date for product code: {}", code, e);
            }
            String company = values[9];
            String supplier = values[10];
            Product product = new Product(id, code, name, batch, stock, deal, free, mrp, rate, date, company, supplier);
            list.add(product);
        }
        return list;
    }


}
