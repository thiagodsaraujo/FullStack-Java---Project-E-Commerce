package com.titashop.admin.category;

import com.titashop.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateCategory(){
        Category category = new Category("Livraria");

        Category savedCategory = repo.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(13);
        Category memoryRam = new Category("Comentários sobre Lacan", parent);

        var savedCategory = repo.saveAll(List.of(memoryRam));

        assertThat(memoryRam.getId()).isGreaterThan(0);
//        assertThat(smartphones.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        var category = repo.findById(2).get();
        System.out.println(category.getName());

        var children = category.getChildren();

        for (Category subcategory : children){
            System.out.println(subcategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        var categories = repo.findAll();

        for (Category category : categories){
            if (category.getParent() == null){
                System.out.println(category.getName());

                var children = category.getChildren();

                for (Category subCategory : children){
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }


    private void printChildren(Category parent, int subLevel)    {

        int newSubLevel = subLevel + 1;
        var children = parent.getChildren();

        for (Category subCategory : children){
            for (int i = 0; i < newSubLevel; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = repo.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(category -> System.out.println(category.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers1";
        Category category = repo.findByName(name);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String alias = "Eletronics";
        Category category = repo.findByAlias(alias);

        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }


}
