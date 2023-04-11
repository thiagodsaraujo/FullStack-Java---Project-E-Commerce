package com.titashop.category;

import com.titashop.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired private CategoryRepository repo;

    @Test
    public void testListEnabledCategories(){
        List<Category> categories = repo.findAllEnabled();

        categories.forEach(category -> {
            System.out.println(category.getName() + "( " + category.isEnabled() + ") ");
        });
        return;
    }


    @Test
    public void testListNoChildrenCategories() {
        // Cria as categorias
        Category cat1 = new Category("Categoria 1");

        // Executa a função que lista as categorias sem filhos
        List<Category> listNoChildrenCategories = repo.findAllNoChildren();

        listNoChildrenCategories.forEach(category -> {
            System.out.println(category.getName() + "( " + category.isEnabled() + ") ");
        });
    }

     @Test
    public void testFindCategoryByAlias(){
        String alias = "camera";
         var category = repo.findByAliasEnabled(alias);

         assertThat(category).isNotNull();
     }

}
