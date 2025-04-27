package real_time_leaderboard.score_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "scores")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Score {

    @Id
    private String id;

    private String userId;

    private int score;

    @Builder.Default
    private Instant timestamp = Instant.now();
}