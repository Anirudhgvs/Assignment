package com.Assignment.service;

import com.Assignment.entity.Product;
import com.Assignment.utils.Utils;
import com.Assignment.repo.ProductRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    Logger logger = LoggerFactory.getLogger(ProductService.class);

    public void save(MultipartFile file) throws Exception {
            List<Product> products = Utils.convertCSVToListOfProduct(file.getInputStream());
            this.productRepo.saveAll(products);
    }

    public ResponseEntity<?> getInStockProductsBySupplier(Integer pageNumber, Integer pageSize, String supplierName, String productName) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> productsPage = productRepo.productsInStockBySupplier(supplierName, pageable);
        List<Product> products = productsPage.getContent();
        if(!products.isEmpty() && productName != null){
            products = products.stream().filter(p -> StringUtils.compareIgnoreCase(p.getName(), productName) == 0).collect(Collectors.toList());
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    public ResponseEntity<List<Product>> getNotExpiredProductsBySupplier(Integer pageNumber, Integer pageSize, List<String> suppliers) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> productsPage = productRepo.productsNotExpiredBySupplier(new Date(), suppliers, pageable);
        List<Product> products = productsPage.getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
