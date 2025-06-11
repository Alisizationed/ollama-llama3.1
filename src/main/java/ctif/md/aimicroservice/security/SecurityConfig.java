package ctif.md.aimicroservice.security;

import ctif.md.aimicroservice.converters.KeycloakJwtRolesConverter;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private final KeycloakJwtRolesConverter rolesConverter = new KeycloakJwtRolesConverter();
    @Autowired
    private Environment env;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
//                                .pathMatchers(HttpMethod.GET, "/api/recipe/images/v2/**").permitAll()
                                .pathMatchers("/api", "/swagger-ui/**", "/v3/api-docs.yaml/swagger-config", "v3/api-docs.yaml").permitAll()
//                                .pathMatchers("/**").authenticated()
//                                .pathMatchers("/api/recipe", "/api/recipe/**")
//                                .permitAll()
//                                .access((mono, context) ->
//                                        mono.flatMap(auth -> {
//                                            boolean hasRole = auth.getAuthorities().stream()
//                                                    .anyMatch(granted -> granted.getAuthority()
//                                                            .equals(KeycloakJwtRolesConverter.PREFIX_RESOURCE_ROLE + "second-client_user"));
//                                            return Mono.just(new AuthorizationDecision(hasRole));
//                                        })
//                                )
                                .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(this::jwtAuthenticationConverter))
                )
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))

        ;

        return http.build();
    }

    private Mono<JwtAuthenticationToken> jwtAuthenticationConverter(Jwt jwt) {
        Collection<? extends GrantedAuthority> authorities = rolesConverter.convert(jwt);
        return Mono.just(new JwtAuthenticationToken(jwt, authorities));
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "https://localhost:8453",
                "https://localhost:7443",
                "https://localhost:8040",
                "https://www.mykeycloak.com:8040",
                "http://localhost:14012",
                "http://localhost:8080",
                "http://localhost:3000",
                "http://localhost:5173",
                "*"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "Authorization", "*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(
                                SslContextBuilder.forClient()
                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                        .build());
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        String jwkSetUri = env.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri");

        assert jwkSetUri != null;
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri)
                .webClient(webClient)
                .build();
    }
}