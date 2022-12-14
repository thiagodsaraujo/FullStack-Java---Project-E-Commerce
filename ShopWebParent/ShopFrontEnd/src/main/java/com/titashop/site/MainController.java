package com.titashop.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/titashop")
public class MainController {
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

}
