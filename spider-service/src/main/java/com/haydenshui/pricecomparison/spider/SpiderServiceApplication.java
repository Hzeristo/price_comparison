package com.haydenshui.pricecomparison.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = "com.haydenshui.pricecomparison.shared.model")
public class SpiderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiderServiceApplication.class, args);
	}

}
