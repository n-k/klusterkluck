package com.github.nk.klusterfuck.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Created by nk on 6/6/17.
 */
@SpringBootApplication
public class Main {

	@PostConstruct
	public void init() {
		System.out.println("initing...");
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
