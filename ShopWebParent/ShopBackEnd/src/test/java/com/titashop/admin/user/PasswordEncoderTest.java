package com.titashop.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodedPassword);

        var matches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertThat(matches).isTrue();

    }
}
