package com.epam.esm;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CertificateServiceApplication extends SpringBootServletInitializer {
	
	private static final Logger LOG = LogManager.getLogger(CertificateServiceApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CertificateServiceApplication.class, args);
		 
		 String[] beanNames = ctx.getBeanDefinitionNames();
	        Arrays.sort(beanNames);
	        for (String beanName : beanNames) {
	            LOG.info("BEANNAME$$$$$$$$$$$$$$: " + beanName);
	        }
	    }

}
