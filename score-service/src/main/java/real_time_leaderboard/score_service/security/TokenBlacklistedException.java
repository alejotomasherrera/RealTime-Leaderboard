package real_time_leaderboard.score_service.security;

import org.springframework.security.core.AuthenticationException;

public class TokenBlacklistedException extends AuthenticationException {
    public TokenBlacklistedException(String msg) {
        super(msg);
    }
}