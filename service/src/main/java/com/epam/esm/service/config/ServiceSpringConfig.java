package com.epam.esm.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
public class ServiceSpringConfig {
	
	@Autowired
	MessageSource messageSource;

	@Bean
	public ModelMapper getModelMapper() {

		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
