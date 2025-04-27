package real_time_leaderboard.user_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import real_time_leaderboard.user_service.model.User;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "MWFzOThkNGYxYTU2c2Q0MWY4OWE3c2QxZjZhNTFzZGYxYXM5OGQ0ZjFhNTZzZDQxZjg5YTdzZDFmNmE1MTVkZg==";
    private final long EXPIRATION = 1000 * 60 * 60 * 12; // 12 horas

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("rol", user.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        return extractUsername(token).equals(user.getEmail());
    }
}