package com.titashop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.titashop.common.entity", "com.titashop.admin.user"})
public class ShopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopBackEndApplication.class, args);
	}

}
