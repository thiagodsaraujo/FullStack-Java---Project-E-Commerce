package com.titashop.product;

import com.titashop.category.CategoryService;
import com.titashop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/c/{category_alias}")
    public String viewCategory(@PathVariable("category_alias") String alias, Model model){
        var category = categoryService.getCategory(alias);

        if (category == null){
            return "error/404";
        }

        var listCategoryParents = categoryService.getCategoryParents(category);
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listCategoryParents", listCategoryParents);

        return "products_by_category";
    }
}
