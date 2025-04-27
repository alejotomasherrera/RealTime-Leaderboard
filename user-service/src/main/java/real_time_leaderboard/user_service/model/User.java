package real_time_leaderboard.user_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    private String id;
    private String nombre;
    private String email;
    private String password;
    private String rol; // ej: "USER", "ADMIN"
}
