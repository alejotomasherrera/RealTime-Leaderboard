package real_time_leaderboard.user_service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import real_time_leaderboard.user_service.dto.AuthRequest;
import real_time_leaderboard.user_service.dto.AuthResponse;
import real_time_leaderboard.user_service.model.User;
import real_time_leaderboard.user_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        String response = authService.register(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.logout(token);
        return ResponseEntity.ok("Token invalidated successfully");
    }

    private String extractBearerToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header format");
    }
}