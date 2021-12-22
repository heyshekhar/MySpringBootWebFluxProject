package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.scope.ScopeTest;

@RestController
@RequestMapping("/api/springexamplecontroller")
public class SpringExampleController {

	@Autowired
	private ScopeTest scopeTest;
	
	@GetMapping(value = "/welcome")
	public String welcomeMessage() {
		return "this is springexamplecontroller ms example";
	}
	
	@GetMapping(value = "/scopetest")
	public String scopeTestMethod() {
		
		scopeTest.testSingleton();
		return "scopetest completed";
	}
	
}
