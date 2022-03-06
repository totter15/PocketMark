package com.example.pocketmark.config;


import com.example.pocketmark.security.filter.*;
import com.example.pocketmark.security.provider.JwtUtil;
import com.example.pocketmark.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;


@EnableWebSecurity(debug=false)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers(
            "/v2/api-docs", "/v3/api-docs", 
            "/configuration/ui",
             "/swagger-resources/**", "/configuration/security",
              "/swagger-ui.html", "/webjars/**","/swagger/**");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(jwtAuthenticationProvider())
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter loginFilter = new JwtLoginFilter(authenticationManager(), userService, jwtUtil);
        JwtCheckFilter checkFilter = new JwtCheckFilter(authenticationManager(), jwtUtil);
        
        JwtCheckExceptionHandler checkExceptionHandler = new JwtCheckExceptionHandler(authenticationManager(),objectMapper);
        http
                .cors()
                // .configurationSource(corsConfigurationSource())
                .and()
                // .headers().cacheControl().disable().and()
                .csrf().disable()
                // //h2-console
                // .headers().frameOptions().disable().and()
                // //h2-console
                // .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/api/v1/sign-up","/api/v1/login",
                        "/api/v1/email-check","/api/v1/alias-check",
                        "/home",
                        "/swagger-ui/**","/swagger-resources/**",
                        "/api/v1/refresh-token",
                        "/api/v1/send-authentication-email",
                        "/api/v1/authentication-email","/"
                ).permitAll()
                
                // //h2 console
                // .antMatchers("/h2-console/**","**").permitAll()
                // //h2 console
                .anyRequest().hasAuthority("ROLE_USER")
        ;

        http
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper));

        http
                .addFilterBefore(new CustomCorsFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilterAt(loginFilter,UsernamePasswordAuthenticationFilter.class)

                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
        ;

        http
                .addFilterBefore(checkExceptionHandler,JwtCheckFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource(){
    //     CorsConfiguration config = new CorsConfiguration();

    //     // config.addAllowedOrigin("https://pocketmark.site/*");
    //     // config.addAllowedOrigin("http://pocketmark.site/*");
    //     // config.addAllowedOrigin("https://back.pocketmark.site/*");
    //     // config.addAllowedOrigin("http://back.pocketmark.site/*");
    //     config.addAllowedOrigin("http://localhost:3000/");

    //     config.addAllowedHeader("*");
    //     config.addAllowedMethod("*");

    //     config.setMaxAge(3600L);
    //     config.addExposedHeader("*");

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config);

    //     return source;

    // }

}
