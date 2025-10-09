package com.timstanford.bookmarkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookmarkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkServiceApplication.class, args);
	}

}
