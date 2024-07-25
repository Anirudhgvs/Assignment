package com.Assignment.controller;

import com.Assignment.entity.Product;
import com.Assignment.utils.Utils;
import com.Assignment.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/products/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (Utils.checkCSVFormat(file)) {
                productService.save(file);
                return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload csv file");
            }
        } catch (Exception e) {
            logger.error("Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }



    @GetMapping("/products/{supplierName}")
    public ResponseEntity<?> getInStockProductsBySupplier(
            @PathVariable("supplierName") String supplierName,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "productName", required = false) String productName) {
        try {
            return productService.getInStockProductsBySupplier(pageNumber, pageSize, supplierName, productName);
        } catch (Exception e) {
            logger.error("Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getNotExpiredProductsBySuppliers(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "suppliers") List<String> suppliers) {
        try {
            return productService.getNotExpiredProductsBySupplier(pageNumber, pageSize, suppliers);
        } catch (Exception e) {
            logger.error("Internal Server Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }



}
