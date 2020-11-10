package com.epam.esm.rest.config;

import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db.properties")
public class SpringConfiq {
 
	@Autowired
	private Environment env;

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(env.getProperty("db.driver"));
		ds.setUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty("db.user"));
		ds.setPassword(env.getProperty("db.password"));
		ds.setInitialSize(Integer.parseInt(env.getProperty("db.pool")));

		return ds;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() throws NamingException {

		return new JdbcTemplate(dataSource());
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.modules(new JavaTimeModule());

		return builder;
	}

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
}
