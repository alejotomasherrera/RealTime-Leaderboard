package real_time_leaderboard.user_service.service;

import lombok.RequiredArgsConstructor;
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
    private final UsuarioRegistradoPublisher usuarioRegistradoPublisher;

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        usuarioRegistradoPublisher.publicarEvento(
                UsuarioRegistradoEvent.builder()
                        .userId(savedUser.getId())
                        .email(savedUser.getEmail())
                        .nombre(savedUser.getNombre())
                        .build()
        );

        return "Usuario registrado con éxito con contraseña: " + user.getPassword();
    }


    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña inválida");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}