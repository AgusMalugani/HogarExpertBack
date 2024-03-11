package com.Servicios.HogarExpert.Config;

import com.Servicios.HogarExpert.Security.Filter.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {


  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  @Bean
  SecurityFilterChain web(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // (2)
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/login","/usuario/crear").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .cors(withDefaults())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    
    
    http
        .formLogin(withDefaults()); // (1)
   /* http
        .httpBasic(withDefaults()); // (1)
     */
    return http.build();
  }
  /* (1) By default, Spring Security form login/http basic auth are enabled.
  However, as soon as any servlet-based configuration is provided,
  form based login or/and http basic auth must be explicitly provided.

  * (2) If our stateless API uses token-based authentication, such as JWT,
    we don't need CSRF protection
  */

  

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }



  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration
      authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}