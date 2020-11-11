package com.epam.esm.dal.config;

import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
public class DalSpringConfig {

	@Autowired
	private Environment env;

	@Bean
	public BasicDataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(env.getProperty("db.driver"));
		ds.setUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty("db.user"));
		ds.setPassword(env.getProperty("db.password"));
		ds.setInitialSize(Integer.parseInt(env.getProperty("db.pool")));

		return ds;
	}

	@Bean
	public JdbcTemplate getJdbcTemplate() throws NamingException {
		return new JdbcTemplate(getDataSource());
	}

	@Bean
	public PlatformTransactionManager getTxManager() {
		return new DataSourceTransactionManager(getDataSource());
	}

}
