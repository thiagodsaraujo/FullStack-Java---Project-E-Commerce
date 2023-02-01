package com.titashop.admin.product;


import com.titashop.common.entity.Brand;
import com.titashop.common.entity.Category;
import com.titashop.common.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 1);
        Category category = entityManager.find(Category.class, 10);

        Product product = new Product();

        product.setName("Camera Digital");
        product.setAlias("camera_digital");
        product.setShortDescription("A good Camera");
        product.setFullDescription("A good camera from Canon for Nature Photos");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(5000);
        product.setCost(400);
        product.setInStock(true);
        product.setEnabled(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        var savedProduct = repo.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }
    
    @Test
    public void listAllProducts(){
       var iterableProcuts = repo.findAll();
       iterableProcuts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        var findById = repo.findById(id).get();
        System.out.println(findById.getName());

        assertThat(findById).isNotNull();

        assertThat(findById.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 1;
        var findById = repo.findById(id).get();
        findById.setPrice(999);
        repo.save(findById);


        var updatedProduct = entityManager.find(Product.class, id);

        assertThat(updatedProduct.getPrice()).isEqualTo(999);
    }

    @Test
    public void testDeleteProductById(){
        Integer id = 3;
        repo.deleteById(id);

        var result = repo.findById(id);

        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer id = 1;
        var product = repo.findById(1).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra_image1.png");
        product.addExtraImage("extra_image2.png");
        product.addExtraImage("extra_image3.png");

        var savedProduct = repo.save(product);
        System.out.println(savedProduct.getImages());

        assertThat(savedProduct.getImages().size()).isEqualTo(3);

    }

    @Test
    public void getProductByName(){
        String name = "Acer Nitro 5";

        var byName = repo.findByName(name);

        System.out.println(byName.getName());

        Assertions.assertThat(byName).isNotNull();
    }
}
