package com.titashop.admin.category;


import com.titashop.common.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 3;

    @Autowired
    private CategoryRepository cateRepo;

    public Category getByName(String name){
        return cateRepo.getCategoryByName(name);
    }

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,
                                     String keyword){
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")){
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum  -1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = null;

        if (keyword != null && !keyword.isEmpty()){
            pageCategories = cateRepo.search(keyword, pageable);
        } else {
            pageCategories = cateRepo.findRootCategories(pageable);
        }

        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()){
            List<Category> searchResult = pageCategories.getContent();

            for (Category category : searchResult){
                category.setHasChildren(category.getChildren().size() > 0);
            }

            return searchResult;

        } else {
            return listHierachicalCategories(rootCategories, sortDir);
        }

    }

    private List<Category> listHierachicalCategories(List<Category> rootCategories, String sortDir ){
        List<Category> hierachicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories){
            hierachicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children){
                String name = "--" + subCategory.getName();
                hierachicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierachicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierachicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierachicalCategories,
                                               Category parent, int subLevel, String sortDir){
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

        int newSubLevel = subLevel + 1;

        for (Category subCategory : children){
            String name = "";
            for (int i = 0; i < newSubLevel; i++){
                name += "--";
            }
            name += subCategory.getName();
            hierachicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierachicalCategories, subCategory, newSubLevel,sortDir);
        }
    }

    public List<Category> listCategoriesUsedInForm(){

        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = cateRepo.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB){
            if (category.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

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

        Set<Category> children = sortSubCategories(parent.getChildren());

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

        Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORIES_PER_PAGE, sort);

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


    public String checkIsNameUnique(Integer id, String name, String alias){
        boolean isCreatingNew = (id == null || id == 0);

        var categoryByName = cateRepo.findByName(name);

        if (isCreatingNew){
            if (categoryByName != null){
                return "DuplicatedName";
            } else {
                var categoryByAlias = cateRepo.findByAlias(alias);
                if (categoryByAlias != null){
                    return "DuplicatedAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id){
                return "DuplicatedName";
            }
            var categoryByAlias = cateRepo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplicatedAlias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children){
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")){
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat1.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
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
