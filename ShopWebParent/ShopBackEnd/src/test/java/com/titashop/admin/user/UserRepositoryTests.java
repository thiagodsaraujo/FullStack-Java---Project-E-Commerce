package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

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

}
