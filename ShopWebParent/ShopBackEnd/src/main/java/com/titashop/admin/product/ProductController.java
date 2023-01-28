package com.titashop.admin.product;

import com.titashop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public String listAll(Model model){

        var listProducts = service.listAll();

        model.addAttribute("listProducts", listProducts);

        return "products/products";
    }

}
