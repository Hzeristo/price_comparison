package com.example.price_comparison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.price_comparison.repository")
public class PriceComparisonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceComparisonApplication.class, args);
	}

}
