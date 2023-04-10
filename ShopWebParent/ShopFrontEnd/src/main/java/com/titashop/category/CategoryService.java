package com.titashop.category;

import com.titashop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired CategoryRepository repo;

    public List<Category> listNoChildrenCategories(){

        List<Category> listNoChildrenCategories = new ArrayList<>();
        var listEnabledCategories = repo.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            var children = category.getChildren();

            if (children == null || children.size() == 0){
                listNoChildrenCategories.add(category);
            }
        });


        return listNoChildrenCategories;
    }
}
