package com.altafjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ConversionFactoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversionFactoryApplication.class, args);
		log.info("=========================== APPLICATION STARTED SUCCESSFULLY ===========================");
	}

}
