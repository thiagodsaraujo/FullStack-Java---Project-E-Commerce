package com.titashop.admin.category;


import com.titashop.common.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
        var rootCategories = cateRepo.findRootCategories();
        return listHierachicalCategories(rootCategories);
    }

    private List<Category> listHierachicalCategories(List<Category> rootCategories){
        List<Category> hierachicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories){
            hierachicalCategories.add(Category.copyFull(rootCategory));

            var children = rootCategory.getChildren();

            for (Category subCategory : children){
                String name = "--" + subCategory.getName();
                hierachicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierachicalCategories, subCategory, 1);
            }
        }

        return hierachicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierachicalCategories,
                                               Category parent, int subLevel){
        var children = parent.getChildren();

        int newSubLevel = subLevel + 1;

        for (Category subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();
            hierachicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierachicalCategories, subCategory, newSubLevel);
        }
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();

        var categoriesInDB = cateRepo.findAll();

        for (Category category : categoriesInDB){
            if (category.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                var children = category.getChildren();

                for (Category subCategory : children){
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,
                                             Category parent, int subLevel)    {

        int newSubLevel = subLevel + 1;
        var children = parent.getChildren();

        for (Category subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
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
