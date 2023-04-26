package com.titashop.product;

import com.titashop.common.entity.Product;
import com.titashop.common.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int SEARCH_RESULTS_PER_PAGE = 10;

    @Autowired private ProductRepository repository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId){

        String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return repository.listByCategory(categoryId, categoryIDMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        var product = repository.findByAlias(alias);

        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }
        return product;

    }

    public Page<Product> search(String keyword, int pageNum){

        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
        return repository.search(keyword, pageable);

    }

}
