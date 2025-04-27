package real_time_leaderboard.score_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private static final String LEADERBOARD_KEY = "leaderboard:global";

    public void addScore(String playerId, String scoreNum) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, playerId, Double.parseDouble(scoreNum));
        redisTemplate.opsForHash().put("player:" + playerId, "id", playerId);
        redisTemplate.opsForHash().put("player:" + playerId, "timestamp", Instant.now().toString());

        // Publish the ranking update event
        Set<ZSetOperations.TypedTuple<String>> topScores = getTopScores(10);
        eventPublisher.publishEvent(new RankingUpdatedEvent(this, topScores));
    }

    public Set<ZSetOperations.TypedTuple<String>> getTopScores(int topN) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, topN - 1);
    }

    public Set<ZSetOperations.TypedTuple<String>> getAllScores(int page, int size) {
        int start = page * size;
        int end = start + size - 1;
        return redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, start, end);
    }

    // Devolver rredis template
    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}