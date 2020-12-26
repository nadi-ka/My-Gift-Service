package com.epam.esm.rest.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.epam.esm.rest.exception.handler.CustomAccessDeniedHandler;
import com.epam.esm.rest.exception.handler.CustomSecurityEntryPoint;
import com.epam.esm.security.filter.JwtFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private JwtFilter jwtFilter;
	private UserDetailsService userDetailsService;
	private CustomSecurityEntryPoint securityEntryPoint;
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	private static final String ROLE_USER = "USER";
	private static final String ROLE_ADMIN = "ADMIN";
	
	private static final Logger LOG = LogManager.getLogger(SecurityConfig.class);

	@Autowired
	public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService,
			CustomSecurityEntryPoint securityEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
		this.jwtFilter = jwtFilter;
		this.userDetailsService = userDetailsService;
		this.securityEntryPoint = securityEntryPoint;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		
		LOG.info("&&&&&&&&&&&&&&&&&&&&&&&&&FROM_SEC_CONFIG");

		http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/login", "/users/signup").permitAll()
				.antMatchers(HttpMethod.GET, "/certificates").permitAll()
				.antMatchers(HttpMethod.GET, "/tags/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
				.antMatchers(HttpMethod.GET, "/users/{userId}", "/users/{userId}/purchases",  "/users/{userId}/purchases/{\\d+}")
				.access("@userSecurity.hasUserId(authentication, #userId) or hasRole('" + ROLE_ADMIN + "')")
				.antMatchers(HttpMethod.POST, "/users/{userId}/purchases")
				.access("@userSecurity.hasUserId(authentication, #userId) or hasRole('" + ROLE_ADMIN + "')")
				.antMatchers(HttpMethod.POST, "/certificates", "/tags").hasRole(ROLE_ADMIN)
				.antMatchers(HttpMethod.DELETE, "/certificates/{\\d+}", "/tags/{\\d+}").hasRole(ROLE_ADMIN)
				.antMatchers(HttpMethod.PATCH, "/certificates/{\\d+}").hasRole(ROLE_ADMIN)
				.antMatchers(HttpMethod.PUT, "/certificates/{\\d+}", "/tags/{\\d+}").hasRole(ROLE_ADMIN);

		http.exceptionHandling().authenticationEntryPoint(securityEntryPoint).and().exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
