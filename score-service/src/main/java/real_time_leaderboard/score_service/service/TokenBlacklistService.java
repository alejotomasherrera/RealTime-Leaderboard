package real_time_leaderboard.score_service.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "auth:token:";

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        Boolean exists = redisTemplate.hasKey(key);

        // Si el token existe en Redis, está en la lista negra
        return exists != null && exists;
    }

    /**
     * Método auxiliar para obtener el valor asociado con el token
     * Puede ser útil para fines de depuración o auditoría
     */
    public String getTokenStatus(String token) {
        String key = BLACKLIST_PREFIX + token;
        return redisTemplate.opsForValue().get(key);
    }
}