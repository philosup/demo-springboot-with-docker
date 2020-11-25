package com.philosup.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void printAccountData(){
		System.out.println(accountRepository.findAll().toString());
	}
	// TestRestTemplate testRestTemplate;


	// @Test
	// void contextLoads() {
	// 	String result = testRestTemplate.getForObject("/sample", String.class);
	// 	assertEquals("1", result);
	// 	// assertEquals(1, 1);
    // }

}
