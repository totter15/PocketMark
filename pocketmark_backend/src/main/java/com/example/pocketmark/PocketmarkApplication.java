package com.example.pocketmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PocketmarkApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketmarkApplication.class, args);
	}

}
