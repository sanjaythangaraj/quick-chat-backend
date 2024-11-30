package com.example.quick_chat_back.shared.authentication.infrastructure.primary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
            .requestMatchers(HttpMethod.GET, "assets/*").permitAll()
            .requestMatchers("/api/**").authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .oauth2ResourceServer(oauth2ResourceServerCustomizer -> oauth2ResourceServerCustomizer
            .jwt(jwtCustomizer -> jwtCustomizer
                .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

    return httpSecurity.build();
  }
}
