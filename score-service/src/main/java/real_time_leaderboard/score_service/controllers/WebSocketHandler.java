package real_time_leaderboard.score_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import real_time_leaderboard.score_service.service.RankingUpdatedEvent;
import real_time_leaderboard.score_service.service.ScoreService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ScoreService scoreService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Client connected: " + session.getId());

        // Obtener y enviar el ranking actual al cliente
        Set<ZSetOperations.TypedTuple<String>> topScores = scoreService.getTopScores(10);
        String jsonResponse = buildJsonResponse(topScores);
        session.sendMessage(new TextMessage(jsonResponse));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    @EventListener
    public void handleRankingUpdatedEvent(RankingUpdatedEvent event) {
        try {
            String jsonResponse = buildJsonResponse(event.getTopScores());
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonResponse));
                }
            }
        } catch (Exception e) {
            System.err.println("Error broadcasting ranking update: " + e.getMessage());
        }
    }

    private String buildJsonResponse(Set<ZSetOperations.TypedTuple<String>> topScores) throws Exception {
        HashMap[] scoresArray = topScores.stream()
                .map(score -> {
                    Map<String, Object> scoreMap = new HashMap<>();
                    String playerId = score.getValue();
                    scoreMap.put("playerId", playerId);
                    scoreMap.put("score", score.getScore());
                    // Recuperar el timestamp desde Redis
                    String timestamp = (String) scoreService.getRedisTemplate()
                            .opsForHash()
                            .get("player:" + playerId, "timestamp");
                    scoreMap.put("timestamp", timestamp);
                    return scoreMap;
                })
                .toArray(HashMap[]::new);
        return objectMapper.writeValueAsString(scoresArray);
    }
}