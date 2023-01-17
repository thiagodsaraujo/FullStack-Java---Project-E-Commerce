package com.titashop.admin.category;


import com.titashop.common.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CategoryService {

    private static final int CATEGORIES_PER_PAGE = 5;
    @Autowired
    private CategoryRepository cateRepo;

    public Category getByName(String name){
        return cateRepo.getCategoryByName(name);
    }

    public List<Category> listAll(){
        return (List<Category>) cateRepo.findAll(Sort.by("id").ascending());
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword){

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum -1, CATEGORIES_PER_PAGE, sort);

        if (keyword != null){
            return  cateRepo.findAll(keyword, pageable);
        }

        return  cateRepo.findAll(pageable);
    }

    public Category save(Category category){

        boolean isUpdatingCategory = (category.getId() != null);

        if (isUpdatingCategory){
            Category existingCategory = cateRepo.findById(category.getId()).get();

            if (category.getName().isEmpty()){
                category.setName(existingCategory.getName());
            }
        }
        return cateRepo.save(category);
    }

    public Category updateCategory(Category categoryInform){
        var categoryInDB = cateRepo.findById(categoryInform.getId()).get();

        if (categoryInform.getImage() != null){
            categoryInDB.setImage(categoryInform.getImage());
        }

        categoryInDB.setName(categoryInform.getName());
        categoryInDB.setAlias(categoryInform.getAlias());

        return cateRepo.save(categoryInDB);
    }


    public boolean isNameUnique(Integer id, String name){
        var categoryByName = cateRepo.getCategoryByName(name);

        if (categoryByName == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew){
            if (categoryByName != null) return false;
        } else {
            if (categoryByName.getId() != id){
                return false;
            }
        }
        return true;
    }

    public Category get(Integer id) throws CategoryNotFoundException{
        try {
            return cateRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Could not find any category with ID: " + id);
        }
    }

    public void delete(Integer id) throws CategoryNotFoundException{
        var countById = cateRepo.countById(id);
        if (countById == null || countById == 0){
            throw new CategoryNotFoundException("Could not find any category with ID: " + id);
        }
        cateRepo.deleteById(id);
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        cateRepo.updateEnabledStatus(id, enabled);
    }

}
