package com.titashop.category;

import com.titashop.common.entity.Category;
import com.titashop.common.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired private CategoryRepository repo;

    public List<Category> listNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = repo.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0) {
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        var category = repo.findByAliasEnabled(alias);

        if (category == null){
            throw new CategoryNotFoundException("Could not found any category with alias: " + alias);
        }

        return category;
    }

    public List<Category> getCategoryParents(Category child){
        List<Category> listParents = new ArrayList<>();

        var parent = child.getParent();

        while (parent != null){
            listParents.add(0,parent);
            parent = parent.getParent();
        }

        listParents.add(child);

        return listParents;
    }
}