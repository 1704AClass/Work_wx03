package com.ningmeng.test.config.mq;

import com.ningmeng.test.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_EMAIL})
    public void testEmailMq(String msg){
    System.out.println(msg);
}
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_SMS})
    public void testSmsMq(String msg){
        System.out.println(msg);
    }
}
