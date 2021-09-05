package com.db.tcity.Spring.Boot.paypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.db.tcity.Spring.Boot.paypal.service.PaypalService;


@Controller
public class PaypallController {
	
	@Autowired
	 PaypalService service;
	
	
	@GetMapping("/")
	public String home() {
		return "home";
	}

}
