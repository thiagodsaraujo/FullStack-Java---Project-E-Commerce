package com.titashop.product;

import com.titashop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 10;

    @Autowired private ProductRepository repository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId){

        String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return repository.listByCategory(categoryId, categoryIDMatch, pageable);

    }

}
