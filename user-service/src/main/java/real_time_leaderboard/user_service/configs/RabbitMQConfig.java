package real_time_leaderboard.user_service.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USUARIO_REGISTRADO_QUEUE = "usuario.registrado";
    public static final String USUARIO_EXCHANGE = "usuario.exchange";
    public static final String USUARIO_REGISTRADO_ROUTING_KEY = "usuario.registrado";

    @Bean
    public Queue usuarioRegistradoQueue() {
        return new Queue(USUARIO_REGISTRADO_QUEUE, true);
    }

    @Bean
    public TopicExchange usuarioExchange() {
        return new TopicExchange(USUARIO_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingUsuarioRegistrado(Queue usuarioRegistradoQueue, TopicExchange usuarioExchange) {
        return BindingBuilder.bind(usuarioRegistradoQueue)
                .to(usuarioExchange)
                .with(USUARIO_REGISTRADO_ROUTING_KEY);
    }
}
