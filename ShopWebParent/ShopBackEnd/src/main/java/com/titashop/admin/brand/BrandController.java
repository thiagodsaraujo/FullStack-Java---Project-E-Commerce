package com.titashop.admin.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {

    @Autowired
    BrandService service;

    @GetMapping("/brands")
    public String listAllBrands(Model model){

        var listBrands = service.listAllBrands();

        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }
}
