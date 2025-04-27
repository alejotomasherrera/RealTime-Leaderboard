package real_time_leaderboard.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import real_time_leaderboard.user_service.dto.AuthRequest;
import real_time_leaderboard.user_service.dto.AuthResponse;
import real_time_leaderboard.user_service.dto.UsuarioRegistradoEvent;
import real_time_leaderboard.user_service.messasing.UsuarioRegistradoPublisher;
import real_time_leaderboard.user_service.model.User;
import real_time_leaderboard.user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final UsuarioRegistradoPublisher usuarioRegistradoPublisher;


    // Registrarr usuario y almacenar en base de datos mongodb
    public String register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Publicar el evento de usuario registrado
        UsuarioRegistradoEvent event = UsuarioRegistradoEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nombre(user.getNombre())
                .build();
        usuarioRegistradoPublisher.publish(event);

        return "Usuario registrado exitosamente";
    }

    // login
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña inválida");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    // logout invalidar token y almacenar en base de datos redis
    public void logout(String token) {
        // Verrificar si el token es válido y no ha expirado
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }
        // Revisar si el token no esta ya almacenado en redis
        String key = "auth:token:" + token;
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("Token ya invalidado");
        }
        // Almacenar el token en redis con un tiempo de expiración
        redisTemplate.opsForValue().set(key, "invalid", 2, java.util.concurrent.TimeUnit.HOURS);
    }
}