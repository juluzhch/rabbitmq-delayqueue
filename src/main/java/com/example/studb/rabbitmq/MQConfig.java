package com.example.studb.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mqconfig
 *
 * @author zhangchao01
 * @date 2021/6/17
 */
@Configuration
public class MQConfig {
//
//    /**
//     * 功能描述：Direct服务交换机
//     */
//    @Bean
//    public DirectExchange rcxDirectExchange() {
////        TopicExchange  e=new TopicExchange()
////            e.setDelayed(true);
//        return new DirectExchange("rcx_change");
//    }
//
//    @Bean
//    public Queue rcxPublishMessageQueue() {
//        return new Queue("rcx_queue", true);
//    }
//
//    @Bean
//    public Binding rcxMessageBinding() {
//        return BindingBuilder.bind(rcxPublishMessageQueue()).to(rcxDirectExchange()).with("rcx_queue");
//    }

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        // 消息是否成功发送到Exchange,这里只对失败的信息进行日志打印
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.printf("send msg to Exchange error, correlationData ={}, cause ={}%n", correlationData, cause);
            }
        });

        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
            System.out.printf("msg from Exchange to Queue error: exchange ={}, route ={}, replyCode ={}, replyText ={}, message ={}", exchange, routingKey, replyCode, replyText, message));

        return rabbitTemplate;
    }

    @Bean
    public JacksonMessageConverter converter() {
        return new JacksonMessageConverter();
    }

}
