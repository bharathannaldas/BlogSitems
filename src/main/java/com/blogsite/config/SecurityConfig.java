package com.blogsite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SuppressWarnings("squid:S5145") // Suppress CSRF disable warning
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()  // Enable Basic Authentication
                .and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/**").authenticated() // Require authentication for all requests
                )
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/api/**")  // Disable CSRF only for API testing routes  // Disable CSRF protection for API testing
                )
                .headers(headers -> headers
                        .xssProtection(xss -> xss.block(true)) // XSS Protection
                        .frameOptions(frame -> frame.sameOrigin()) // Clickjacking Protection
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'")) // CSP
                );

        return http.build();
    }
}
