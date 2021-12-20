package com.bookmarkmanager.bookmarkmanager.configuration;

import com.bookmarkmanager.bookmarkmanager.configuration.filter.JwtAuthenticationFilter;

import org.apache.coyote.http11.Http11InputBuffer;
import org.apache.tomcat.util.net.NioEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ch.qos.logback.classic.joran.action.ConfigurationAction;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity(debug=true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    // @Override
    // public void configure(WebSecurity web) throws Exception{
    //     web.ignoring().antMatchers("/api/login","/api/sign-up");
        
    // }

    // SecurityContextPersistenceFilter f;
    // Http11InputBuffer d;
    // NioEndpoint dd;
    // FilterSecurityInterceptor s;
    private final RESTAuthenticationEntryPoint authenticationEntryPoint;
    // FilterSecurityInterceptor d;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
            .password("admin").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterAt(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                
                ;
            
            // .headers().cacheControl().disable().and()
            
            // .exceptionHandling().and() // 인증,허가 에러시 공통적으로 처리해주는부분 이란다...?
            
            // .antMatcher("/api/header-test")
    }
    


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
