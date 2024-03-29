package com.titashop.admin.brand;


import com.titashop.common.entity.Brand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class BrandService {

    public static final int BRANDS_PER_PAGE = 10;


    @Autowired
    public BrandRepository repo;

    public Brand getByName(String name){
        return repo.findByName(name);
    }

    public List<Brand> listAllBrands(){
        return (List<Brand>) repo.findAll();
    }


    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null){
            return repo.findAll(keyword, pageable);
        }

        return repo.findAll(pageable);
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
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
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
