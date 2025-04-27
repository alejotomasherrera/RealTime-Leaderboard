package real_time_leaderboard.user_service.dto;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegistradoEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String email;
    private String nombre;
}