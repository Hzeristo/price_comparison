package com.haydenshui.pricecomparison.user;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import com.haydenshui.pricecomparison.shared.exception.GlobalExceptionHandler;


import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {
    "com.haydenshui.pricecomparison.user", 
    "com.haydenshui.pricecomparison.shared"
})
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EntityScan(basePackages = "com.haydenshui.pricecomparison.shared.model")
public class UserServiceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UserServiceApplication.class, args);
		GlobalExceptionHandler handler = context.getBean(GlobalExceptionHandler.class);
		System.out.println("Exception Handler loaded: " + handler);
	}
}
