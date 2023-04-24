package com.jack.clientauthority.config;

import com.jack.clientauthority.annotation.ClientRabbitmqBeanDefinitionRegistrar;
import com.jack.clientauthority.service.impl.SpringUserMqMessageConsumer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
@Import(ClientRabbitmqBeanDefinitionRegistrar.class)
public class ClientRabbitConfig {

    @Bean
    public RabbitDefaultErrorHandler rabbitDefaultErrorHandler() {
        return new RabbitDefaultErrorHandler();
    }

    @Bean
    public SpringUserMqMessageConsumer customUserMqMessageConsumer() {
        return new SpringUserMqMessageConsumer();
    }

    /**
     * 构建转发到死信队列需要的参数
     * <p></p>
     * 如果一个队列A里边的消息，要转发到死信队列B，那么A队列创建时要加上这些参数
     *
     * @return  参数
     */
    public static Map<String, Object> assembleDeadQueueArgs() {
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("x-dead-letter-exchange", ClientRabbitmqConstant.DEAD_LETTER.exchange);
        headMap.put("x-dead-letter-routing-key", ClientRabbitmqConstant.DEAD_LETTER.routeKey);
        return headMap;
    }
}
