package com.ai_plateform_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AiPlateformApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiPlateformApiApplication.class, args);
	}

}
