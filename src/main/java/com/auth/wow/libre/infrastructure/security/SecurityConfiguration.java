package com.auth.wow.libre.infrastructure.security;

import com.auth.wow.libre.domain.model.security.UserDetailsServiceCustom;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.auth.wow.libre.infrastructure.filter.AuthenticationFilter;
import com.auth.wow.libre.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserDetailsServiceCustom userDetailsServiceCustom;
  private final JwtPort jwtPort;

  public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                               UserDetailsServiceCustom userDetailsServiceCustom, JwtPort jwtPort) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsServiceCustom = userDetailsServiceCustom;
    this.jwtPort = jwtPort;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfigurationSource = new CorsConfiguration();
    corsConfigurationSource.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
    corsConfigurationSource.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()));
    corsConfigurationSource.setAllowedHeaders(List.of(HttpHeaders.CONTENT_TYPE,
        HttpHeaders.AUTHORIZATION));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfigurationSource);
    return source;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
            endpoints -> endpoints.requestMatchers("/api/auth/login").authenticated()
        ).addFilterBefore(new AuthenticationFilter(authenticationProvider(), jwtPort),
            UsernamePasswordAuthenticationFilter.class)
        .cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request ->
            request.requestMatchers(
                    "/api/store/products",
                    "/api/account/validate/email",
                    "/api/account/search",
                    "/api/resources/country",
                    "/api/resources/benefit",
                    "/api/account", "/v2/api-docs",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**")
                .permitAll().anyRequest().authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider()).addFilterBefore(
            jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsServiceCustom);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }


}
