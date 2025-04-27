package real_time_leaderboard.score_service.service;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@Getter
public class RankingUpdatedEvent extends ApplicationEvent {
    private final Set<ZSetOperations.TypedTuple<String>> topScores;

    public RankingUpdatedEvent(Object source, Set<ZSetOperations.TypedTuple<String>> topScores) {
        super(source);
        this.topScores = topScores;
    }
}