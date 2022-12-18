package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {


    @Autowired
    UserRepository repo;

    // Classe do Spring Data JPA Test, para fazer teste unitarios com repositorios
    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testNewUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1); // vai pegar no bd da classe Role
        User userThg = new User("tita@email.com", "123", "Ojuara" , "Araujo" );
        userThg.addRole(roleAdmin);

        User savedUser = repo.save(userThg);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testNewUserWithTwoRoles() {

//        Role roleShipper = entityManager.find(Role.class, 4);
//        Role roleAssistent = entityManager.find(Role.class, 2);

        Role roleSalesperson = new Role(5);
        Role roleAssistant = new Role(2);

        User otherUser = new User("tita@email.com","123", "Tita", "Araujo");

        otherUser.addRole(roleSalesperson);
        otherUser.addRole(roleAssistant);

        User savedUser = repo.save(otherUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    // vai testar a listagem dos usuarios

    @Test
    public void testListAllUsers(){
        var listUsers = repo.findAll();

        listUsers.forEach(user -> System.out.println(user));

    }

    // vai testar o get por Id

    @Test
    public void testGetUserById(){
        var userId1 = repo.findById(1).get();
        var userId2 = repo.findById(5).get();
        System.out.println(userId1);
        System.out.println(userId2);

        assertThat(userId1).isNotNull();
        assertThat(userId2).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        var userId1 = repo.findById(1).get();

        userId1.setEnabled(true);
        userId1.setPassword("12345");

        repo.save(userId1);
    }

    @Test
    public void testUpdateUserRoles(){
        var userId2 = repo.findById(5).get();

        Role roleSalesPerson = new Role(5);
        Role roleShipper = new Role(4);

        userId2.getRoles().remove(roleSalesPerson);
        userId2.addRole(roleShipper);

        repo.save(userId2);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 5;
        repo.deleteById(userId);
    }

}
