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

//        for (Category category: categories) {
//            System.out.println(category);
//        }

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

//        // Verifica se a lista retornada contém apenas a categoria 3
//        assertEquals(1, listNoChildrenCategories.size());
//        assertEquals(cat3.getName(), listNoChildrenCategories.get(0).getName());
    }


    @Test
    public void compararListas() {


        List<Category> categories = repo.findAllEnabled();

        List<Category> listNoChildrenCategories = repo.findAllNoChildren();

        System.out.println("---------------------");

//        listNoChildrenCategories.forEach(category -> {
//            System.out.println(category.getName() + "( " + category.isEnabled() + ") ");
//        });
//
//        System.out.println("---------------------");
//
//        categories.forEach(category -> {
//            System.out.println(category.getName() + "( " + category.isEnabled() + ") ");
//        });
        System.out.println("1: " + categories.size());
        System.out.println("2: " + listNoChildrenCategories.size());

    }

}
