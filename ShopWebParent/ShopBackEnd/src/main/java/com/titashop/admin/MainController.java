package com.titashop.admin;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("")
	public String viewHomePage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		System.out.println("username: " + auth.getName());
		return "index";
	}

	@GetMapping("/login")
	public String viewLoginPage(){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
			return "login";
		}
		return "redirect:/";

	}

}
