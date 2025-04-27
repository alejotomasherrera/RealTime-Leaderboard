package real_time_leaderboard.user_service.messasing;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import real_time_leaderboard.user_service.configs.RabbitMQConfig;
import real_time_leaderboard.user_service.dto.UsuarioRegistradoEvent;

@Component
@RequiredArgsConstructor
public class UsuarioRegistradoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarEvento(UsuarioRegistradoEvent event) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.USUARIO_EXCHANGE,           // el exchange declarado
            RabbitMQConfig.USUARIO_REGISTRADO_ROUTING_KEY, // la routing key
            event
        );
    }
}
