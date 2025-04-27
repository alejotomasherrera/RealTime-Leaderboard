package real_time_leaderboard.user_service.messasing;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import real_time_leaderboard.user_service.dto.UsuarioRegistradoEvent;

@Component
@RequiredArgsConstructor
public class UsuarioRegistradoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(UsuarioRegistradoEvent event) {
        rabbitTemplate.convertAndSend("usuario-registrado-exchange", "usuario.registrado", event);
    }

}