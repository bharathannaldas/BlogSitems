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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                 //   .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in Postman (you can enable it for form-based login)
                    .httpBasic()                   // Enable Basic Authentication
                    .and()
                    .authorizeRequests(auth -> auth
                            //.antMatchers("/api/v1.0/blogsite/user/register").permitAll() // Allow public access to registration
                            .anyRequest().authenticated() // All other requests need authentication
                    )
                    .headers(headers -> headers
                            .xssProtection(xss -> xss.block(true)) // XSS Protection
                            .frameOptions(frame -> frame.sameOrigin()) // Clickjacking Protection
                            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'")) // CSP
                    );

            return http.build();
        }

}

