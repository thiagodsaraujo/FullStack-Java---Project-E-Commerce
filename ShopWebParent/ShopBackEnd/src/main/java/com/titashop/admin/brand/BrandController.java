package com.titashop.admin.brand;

import com.titashop.admin.FileUploadUtil;
import com.titashop.admin.category.CategoryService;
import com.titashop.common.entity.Brand;
import com.titashop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BrandController {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/brands")
    public String listAllBrands(Model model){

        var listBrands = brandService.listAllBrands();

        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand_form";
    }

//    @PostMapping("/brands/save")
//    public String saveBrand(Brand brand,
//                            Model model,
//                            @Param("fileLogo")MultipartFile multipartFile,
//                            RedirectAttributes redirectAttributes){
//
//        if (!multipartFile.isEmpty()){
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            brand.setLogo(fileName);
//
//            Brand savedBrand = brandService.save(brand);
//            String uploadDir = "../brand-image/" + savedBrand.getId();
//
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//        } else {
//            brandService.save(brand);
//        }
//        redirectAttributes.addFlashAttribute("message", "The brand has been save successfully.");
//
//        return "redirect:/brands";
//    }
}
