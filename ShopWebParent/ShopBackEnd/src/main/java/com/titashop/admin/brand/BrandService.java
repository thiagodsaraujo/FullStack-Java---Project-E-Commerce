package com.titashop.admin.brand;


import com.titashop.common.entity.Brand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BrandService {


    @Autowired
    public BrandRepository repo;

    public Brand getByName(String name){
        return repo.findByName(name);
    }

    public List<Brand> listAllBrands(){
        return (List<Brand>) repo.findAll(Sort.by("id").ascending());
    }

    public Brand save(Brand brand){
        return repo.save(brand);
    }
}
