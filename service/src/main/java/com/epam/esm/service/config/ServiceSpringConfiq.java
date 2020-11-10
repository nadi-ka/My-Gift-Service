package com.epam.esm.service.config;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
public class ServiceSpringConfiq {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
