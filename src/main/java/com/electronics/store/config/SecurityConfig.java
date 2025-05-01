package com.electronics.store.config;

import com.electronics.store.security.CustomAccessDeniedHandler;
import com.electronics.store.security.JwtAuthenticationEntryPoint;
import com.electronics.store.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity //method level security in order controller
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private final AccessDeniedHandler customAccessDeniedHandler; // for custom meesage when any other than authorized role try to aceess the api
    public SecurityConfig(AccessDeniedHandler customAccessDeniedHandler) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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
                    .requestMatchers(HttpMethod.POST,"/categories").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                    .requestMatchers(HttpMethod.POST,"auth/login").permitAll()
                    .requestMatchers("auth/**").authenticated()
                    .anyRequest().permitAll() // should be here, so that method level security will work

        ).exceptionHandling(ex -> ex
                .accessDeniedHandler(customAccessDeniedHandler)
        );



//        httpSecurity.httpBasic(Customizer.withDefaults());

        //pehle humlog wala filter lagega
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //for checking if token is authorized or not
        httpSecurity.exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        //state less means server will not contains any information about user login as jwt token is stateless
        httpSecurity.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

}
