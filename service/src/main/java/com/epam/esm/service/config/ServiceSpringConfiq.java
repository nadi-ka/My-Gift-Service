package com.epam.esm.service.config;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ServiceSpringConfiq {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
