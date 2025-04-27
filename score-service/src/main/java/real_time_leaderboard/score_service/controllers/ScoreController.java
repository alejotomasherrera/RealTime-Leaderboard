package real_time_leaderboard.score_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import real_time_leaderboard.score_service.dto.AddScoreRequest;
import real_time_leaderboard.score_service.model.Score;
import real_time_leaderboard.score_service.service.JwtService;
import real_time_leaderboard.score_service.service.ScoreService;

import java.util.Set;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<String> addScore(@RequestHeader("Authorization") String token, @RequestBody AddScoreRequest request) {
        jwtService.validateTokenOrThrow(token);
        scoreService.addScore(request.getPlayerId(), request.getScoreNum());
        return ResponseEntity.ok("Score added successfully");
    }

    @GetMapping("/top/{topN}")
    public ResponseEntity<Set<TypedTuple<String>>> getTopScores(@RequestHeader("Authorization") String token, @PathVariable int topN) {
        jwtService.validateTokenOrThrow(token);
        return ResponseEntity.ok(scoreService.getTopScores(topN));
    }

    // GetAll Scores paginado
    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<Set<TypedTuple<String>>> getAllScores(@RequestHeader("Authorization") String token, @PathVariable int page, @PathVariable int size) {
        jwtService.validateTokenOrThrow(token);
        return ResponseEntity.ok(scoreService.getAllScores(page, size));
    }


}