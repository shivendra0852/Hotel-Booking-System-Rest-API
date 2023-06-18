package com.staywell.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AppConfig {

	@Bean
	public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {

		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers(HttpMethod.POST, "/staywell/customers/register").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/admins/register").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/hotels/register").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/customers/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/admins/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/hotels/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/staywell/rooms/add").hasRole("HOTEL")
        .requestMatchers(HttpMethod.PUT, "/staywell/customers/update").hasRole("CUSTOMER")
        .requestMatchers(HttpMethod.GET, "/staywell/customers/getToBeDeletedAccounts").hasRole("ADMIN")
		.anyRequest()
		.authenticated()
		.and()
		.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
		.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
		.formLogin()
		.and()
		.httpBasic();

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

}
