package com.titashop.admin.product;


import com.titashop.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @MockBean private ProductRepository repo;
    @InjectMocks private ProductService service;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate(){
        Integer id = null;
        String name = "teste";

        var byName = repo.findByName(name);

        System.out.println(byName.getName());
        assertThat(byName.getId()).isGreaterThan(0);
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {

    }
    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {

    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {

    }


}
