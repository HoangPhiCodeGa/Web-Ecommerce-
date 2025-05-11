package com.backend;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.backend","com.backend.commonservice"})
public class UserServiceApplication {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		logger.info("RestApiDemoApplication Start");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
