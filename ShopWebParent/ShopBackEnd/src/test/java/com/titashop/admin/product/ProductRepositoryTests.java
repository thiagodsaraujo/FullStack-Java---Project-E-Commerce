package com.titashop.admin.product;


import com.titashop.common.entity.Brand;
import com.titashop.common.entity.Category;
import com.titashop.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 7);
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
}
