package com.staywell.config;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Configuration
public class AppConfig {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Bean
    public Key secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/customer/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/admin/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/staywell/hotels/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/staywell/rooms/add").hasRole("HOTEL")
                .requestMatchers(HttpMethod.POST, "/customer/delete").hasRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(new JwtTokenGeneratorFilter(secretKey()), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(secretKey()), BasicAuthenticationFilter.class)
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
