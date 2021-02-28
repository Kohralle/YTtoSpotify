package com.koral.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.koral.application.controllers"})
@EnableJpaRepositories(basePackages = "com.koral.application.repository")
@EntityScan(basePackages = "com.koral.application.model")
public class YtspotApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtspotApplication.class, args);
	}

}
