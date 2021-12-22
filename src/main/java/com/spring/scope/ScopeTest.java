package com.spring.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ScopeTest {

	@Autowired
	private SingletonDto singletonDto1;
	
	@Autowired
	private SingletonDto singletonDto2;
	
	
	public void testSingleton() {
		singletonDto1.setId(10);
		singletonDto1.setName("shekhar");
		
		System.out.println("singletonDto1 : "+singletonDto1.getId());
		System.out.println("singletonDto2 : "+singletonDto2.getId());
		
		System.out.println("singletonDto1 : "+singletonDto1.hashCode());
		System.out.println("singletonDto2 : "+singletonDto2.hashCode());
		
	}
	
}
