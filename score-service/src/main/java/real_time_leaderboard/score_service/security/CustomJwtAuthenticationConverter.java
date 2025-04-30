package real_time_leaderboard.score_service.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import real_time_leaderboard.score_service.service.TokenBlacklistService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final TokenBlacklistService tokenBlacklistService;

    public CustomJwtAuthenticationConverter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Verificar si el token está en la lista negra
        if (tokenBlacklistService.isTokenBlacklisted(jwt.getTokenValue())) {
            throw new TokenBlacklistedException("El token ha sido invalidado");
        }

        // Extraer el username del subject
        String username = jwt.getSubject();

        // Extraer roles del claim "rol"
        String rol = jwt.getClaim("rol");
        Collection<GrantedAuthority> authorities = Collections.emptySet();

        if (rol != null) {
            authorities = Stream.of(rol)
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}