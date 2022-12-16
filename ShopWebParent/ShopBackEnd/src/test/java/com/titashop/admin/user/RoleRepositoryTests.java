package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

    @Autowired
    RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = repo.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSalesperson = new Role("Salesperson", "manage product price, costumers," +
                "shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "manage categories, brands" +
                "articles and menus");
        Role roleShipper = new Role("Shipper", "view products, view orders" +
                "and update order status");
        Role roleAssistent = new Role("Assistent", "manage questions and review" +
                "of products");

        var savedRole = repo.saveAll(List.of(roleAssistent, roleSalesperson, roleEditor, roleShipper));


    }


}
