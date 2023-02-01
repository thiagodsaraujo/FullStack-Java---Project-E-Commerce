package com.titashop.admin.product;

import com.titashop.admin.FileUploadUtil;
import com.titashop.admin.brand.BrandService;
import com.titashop.common.entity.Brand;
import com.titashop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model){

        var listProducts = productService.listAll();

        model.addAttribute("listProducts", listProducts);

        return "products/products";
    }


    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAllBrands();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");

        return "products/product_form";
    }


    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam("fileImage")MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(fileName);

            Product savedProduct = productService.save(product);
            String uploadDir = "../product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            productService.save(product);
        }

        redirectAttributes.addFlashAttribute("message", "This product has been saved" +
                "sucessfully!");
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes){
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Product ID: " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                                Model model,
                                RedirectAttributes redirectAttributes){
        try{
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "The prodcut ID: " + id + " has been deleted sucessfully!");
        } catch (ProductNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/products";
    }

//    @GetMapping("/products/edit/{id}")
//    public String editBrand(@PathVariable(name = "id") Integer id,
//                            Model model,
//                            RedirectAttributes redirectAttributes){
//        try {
//            var product = productService.get(id);
//            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//
//            model.addAttribute("brand", brand);
//            model.addAttribute("listCategories", listCategories);
//            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
//
//            return "brands/brand_form";
//        } catch (ProductNotFoundException ex) {
//            redirectAttributes.addFlashAttribute("message", ex.getMessage());
//            return "redirect:/brands";
//        }
//    }
}


