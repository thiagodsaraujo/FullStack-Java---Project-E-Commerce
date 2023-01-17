package com.titashop.admin.category.controller;

import com.titashop.admin.category.CategoryService;
import com.titashop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listAll(Model model){
        List<Category> listCategories = service.listAll();
        model.addAttribute("listCategories", listCategories);

        return "categories/categories";
    }
}
