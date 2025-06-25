package com.bootcamp_ms.infrastructure.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .pathMatchers("/bootcamp/{id}").authenticated()
                        .pathMatchers("/bootcamp").hasRole("ADMIN")
                        .pathMatchers("/bootcamp/delete").hasRole("ADMIN")
                        .pathMatchers("/bootcamp/list").authenticated()
                        .pathMatchers("/bootcamp/popular").hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .bearerTokenConverter(new ServerBearerTokenAuthenticationConverter())
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesConverter()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaim("role");
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${jwt.secret}") String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
