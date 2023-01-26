package com.titashop.admin.brand;


import com.titashop.common.entity.Brand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Brand editBrand(Brand brandInForm){
        Brand brandInDB = repo.findById(brandInForm.getId()).get();


        if (brandInForm.getLogo() != null){
            brandInDB.setLogo(brandInForm.getLogo());
        }

        brandInDB.setName(brandInForm.getName());


        return repo.save(brandInDB);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e){
            throw new BrandNotFoundException("This Brand with ID:" + id + "dont exist");
        }
    }

    public void delete(Integer id) throws BrandNotFoundException{
        var brandId = repo.countById(id);

        if (brandId == null || brandId == 0){
            throw new BrandNotFoundException("This Brand with ID:" + id + "dont exist");
        }
        repo.deleteById(id);
    }

    public String checkUniqueBrand(Integer id, String name){
        boolean isCreatingNew = id == null || id == 0;

        var brandByName = repo.findByName(name);

        if (isCreatingNew){
            if (brandByName != null){
                return "duplicatedBrand";
            }
        } else {
            if (brandByName != null && brandByName.getId() != null){
                return "duplicatedBrand";
            }
        }

        return "OK";

    }

}
