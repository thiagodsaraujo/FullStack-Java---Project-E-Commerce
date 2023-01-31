package com.titashop.admin.brand;

import com.titashop.common.entity.Brand;
import com.titashop.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

    @Autowired
    private BrandRepository repo;


    @Test
    public void testCreateBrand1(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");
        acer.getCategories().add(laptops);

        var savedBrand = repo.save(acer);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Category cellphonesAndAcessories = new Category(4);
        Category tablets = new Category(7);
        Brand apple = new Brand("Apple");
        apple.getCategories().add(cellphonesAndAcessories);
        apple.getCategories().add(tablets);

        var savedBrand = repo.save(apple);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand3(){
        Category memory = new Category(29);
        Category hd = new Category(24);

        Brand samsung = new Brand("Samsung");

        samsung.getCategories().add(memory);
        samsung.getCategories().add(hd);

        var savedBrand = repo.save(samsung);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void  printAllBrands(){
        Iterable<Brand> brands = repo.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotNull();

//        for (Brand brand : brands){
//            System.out.println("ID: " + brand.getId());
//            System.out.println("Name: " + brand.getName());
//            System.out.println("Category:" + brand.getCategories());
//
//        }
    }

    @Test
    public void testGetById(){
        Brand brand = repo.findById(2).get();

        assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void getBrandById(){
        var brandById = repo.findById(2);
        System.out.println(brandById.get().toString());

        var categories = brandById.get().getCategories();

        for (Category category : categories){
            System.out.println(category.getName());
        }

        assertThat(categories.size()).isGreaterThan(0);
    }

    @Test
    public void updateBrand(){
        var samsung = repo.findByName("Samsung");
        System.out.println(samsung.toString());

        samsung.setName("Samsung Eletronics");
        repo.save(samsung);

        System.out.println(samsung.toString());

        assertThat(samsung.getName()).isEqualTo("Samsung Eletronics");
    }

    @Test
    public void deleteBrand(){
        var deletedBrand = repo.findByName("Apple");

        repo.delete(deletedBrand);

        Optional<Brand> result = repo.findById(deletedBrand.getId());

        assertThat(result.isEmpty());
    }

//    @Test
//    public void getBrand(){
//        repo.get
//    }

}
