package com.learnspringsecurity.config;


import com.learnspringsecurity.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        requestAttributeHandler.setCsrfRequestAttributeName("_csrf");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //This configuration is required for creating JWT token
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));    //header name of jwt token
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }).and()
                .csrf((csrf)->csrf.csrfTokenRequestHandler(requestAttributeHandler).ignoringRequestMatchers("/contact","/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))//It will generate csrf token
                        .addFilterBefore(new RequestValidationBeforeFilter(),BasicAuthenticationFilter.class)
                        .addFilterAt(new AuthenticationLoggingAtFilter(),BasicAuthenticationFilter.class)
                        .addFilterAfter(new CsrfCookieFilter(),BasicAuthenticationFilter.class)//CsrfCookieFilter will add csrf token to response header after basic authentication completed.
                        .addFilterAfter(new AuthoritiesLoggingAfterFilter(),BasicAuthenticationFilter.class)
                        .addFilterAfter(new JWTTokenGeneratorFilter(),BasicAuthenticationFilter.class)
                        .addFilterBefore(new JWTTokenValidatorFilter(),BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/account").hasRole("USER")
                .requestMatchers("/balance").hasAnyRole("USER","ADMIN")
                .requestMatchers("/cards").hasRole("USER")
                .requestMatchers("/loans").authenticated()
                .requestMatchers("/user").authenticated()
                .requestMatchers("/contact","/notices","/register").permitAll()
        .and().formLogin()
        .and().httpBasic();
        return (SecurityFilterChain)http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
