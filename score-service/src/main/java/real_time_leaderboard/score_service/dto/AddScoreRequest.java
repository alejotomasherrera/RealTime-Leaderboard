package real_time_leaderboard.score_service.dto;

import lombok.*;


@Data
public class AddScoreRequest {
    private String playerId;
    private String scoreNum;

}