package com.epam.esm.service.config;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
public class ServiceSpringConfiq {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
