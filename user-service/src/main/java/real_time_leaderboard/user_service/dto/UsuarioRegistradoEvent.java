package real_time_leaderboard.user_service.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegistradoEvent {
    private String userId;
    private String email;
    private String nombre;
}