package real_time_leaderboard.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // Printear el token
        System.out.println("Token"+exchange);
        System.out.println("Token2"+chain);
        // Log headers for debugging (remove in production)
        request.getHeaders().forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });

        // Ensure we're passing through the Authorization header
        return chain.filter(exchange);
    }
}