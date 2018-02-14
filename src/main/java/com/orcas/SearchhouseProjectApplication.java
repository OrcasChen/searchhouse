package com.orcas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SearchhouseProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchhouseProjectApplication.class, args);
	}


	@GetMapping("/hello")
	public String hello() {
		return "hey";
	}
}
