package com.electronics.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity //method level security in order controller
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //later learning cors and csrf, currently disabling it
        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //the order of requestMatchers() matters.
        // Authorization rules are evaluated in the sequence they are declared, and the first matching rule is applied.
        httpSecurity.authorizeHttpRequests( request->
            // only admin can access all delete methods starting from /users/ url
            request.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                    // admin and normal users can access all put methods starting from /users/ url
                    .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("ADMIN","NORMAL")
                    .requestMatchers(HttpMethod.POST,"/users/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                    .requestMatchers(HttpMethod.GET,"/products").permitAll()
                    .requestMatchers(HttpMethod.POST,"/products").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                    .requestMatchers("/categories/**").hasRole("ADMIN")
                    .anyRequest().permitAll() // should be here , so that method level security will work
        );
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
