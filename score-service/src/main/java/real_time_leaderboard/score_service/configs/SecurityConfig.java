package real_time_leaderboard.score_service.configs;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import real_time_leaderboard.score_service.security.CustomJwtAuthenticationConverter;
import real_time_leaderboard.score_service.security.JwtAuthenticationEntryPoint;

import javax.crypto.SecretKey;
import java.security.Key;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomJwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public SecurityConfig(
            CustomJwtAuthenticationConverter jwtAuthenticationConverter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/scores/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        System.out.println(jwtSecret);
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        System.out.println(key);
        return NimbusJwtDecoder.withSecretKey((SecretKey) key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}