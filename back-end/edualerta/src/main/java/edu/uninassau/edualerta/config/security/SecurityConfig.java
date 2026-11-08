package edu.uninassau.edualerta.config.security;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {

    return http
        .cors(Customizer.withDefaults())

        .csrf(csrf -> csrf.disable())

        .sessionManagement(
            session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated())

        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(Customizer.withDefaults()))

        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuracao = new CorsConfiguration();

    configuracao.setAllowedOriginPatterns(List.of(
        "http://localhost:*",
        "http://127.0.0.1:*"
    ));

    configuracao.setAllowedMethods(List.of(
        "GET",
        "POST",
        "PUT",
        "DELETE",
        "OPTIONS"
    ));

    configuracao.setAllowedHeaders(List.of(
        "Authorization",
        "Content-Type"
    ));

    configuracao.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource origem =
        new UrlBasedCorsConfigurationSource();

    origem.registerCorsConfiguration("/**", configuracao);

    return origem;
  }
}
