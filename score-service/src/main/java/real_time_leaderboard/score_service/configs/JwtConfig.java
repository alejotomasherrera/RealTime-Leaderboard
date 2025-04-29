package real_time_leaderboard.score_service.configs;

@Bean
public JwtDecoder jwtDecoder() {
    String secretKey = "MWFzOThkNGYxYTU2c2Q0MWY4OWE3c2QxZjZhNTFzZGYxYXM5OGQ0ZjFhNTZzZDQxZjg5YTdzZDFmNmE1MTVkZg=="; // Debe coincidir con user-service
    SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(key).build();
}