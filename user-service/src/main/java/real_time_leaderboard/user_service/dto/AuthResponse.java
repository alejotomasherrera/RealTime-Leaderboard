package real_time_leaderboard.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    @NotBlank(message = "Token cannot be blank")
    private String token;
}