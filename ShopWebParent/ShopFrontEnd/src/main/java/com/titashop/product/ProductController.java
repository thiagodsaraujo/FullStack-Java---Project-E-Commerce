package com.titashop.product;

import com.titashop.category.CategoryService;
import com.titashop.common.entity.Category;
import com.titashop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    //31:58
    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                     Model model){
        return viewCategoryByPage(alias,1, model);

    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                               @PathVariable("pageNum") int pageNum,
                               Model model){
        var category = categoryService.getCategory(alias);

        if (category == null){
            return "error/404";
        }

        var listCategoryParents = categoryService.getCategoryParents(category);

        Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
        var listProducts = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }


        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listCategoryParents", listCategoryParents);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("category", category);

        return "products_by_category";
    }
}
