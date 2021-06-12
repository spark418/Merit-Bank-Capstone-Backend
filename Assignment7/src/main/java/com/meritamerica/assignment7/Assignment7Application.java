package com.meritamerica.assignment7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableSwagger2
public class Assignment7Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment7Application.class, args);
	}
	
	@Bean
	public Docket SwaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().build();
	}
	
	@Bean
    CommandLineRunner runner() {
        return args -> {
            System.out.println("CommandLineRunner running in the UnsplashApplication class...");
        };
    }

}
