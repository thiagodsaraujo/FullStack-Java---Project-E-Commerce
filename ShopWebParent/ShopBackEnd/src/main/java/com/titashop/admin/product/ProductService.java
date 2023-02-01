package com.titashop.admin.product;

import com.titashop.common.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository repo;

    public List<Product> listAll(){
        return (List<Product>) repo.findAll();

    }

    public Product save(Product product){

        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        }

        else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return repo.save(product);
    }

    public Product editBrand(Product productInForm){
        Product productInDB = repo.findById(productInForm.getId()).get();


//        if (productInForm.getImage() != null){
//            productInDB.setImage(productInForm.getImage());
//        }

        productInDB.setName(productInForm.getName());


        return repo.save(productInDB);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any brand with ID " + id);
        }
    }


    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);

        var productByName = repo.findByName(name);

        if (isCreatingNew){
            if (productByName != null){
                return "Duplicate";
            }
        } else {
            if (productByName != null && productByName.getId() != id){
                return "Duplicate";
            }
        }

        return "OK";

    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    public void deleteProduct(Integer id) throws ProductNotFoundException {
        Long countById = repo.countById(id);

        if (countById == null || countById == 0){
            throw new ProductNotFoundException("Could not find any product with ID: " + id);
        }
        repo.deleteById(id);
    }
}
