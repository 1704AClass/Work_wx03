package com.ningmeng.test.mq;

import com.ningmeng.test.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by BJDGZJD on 14/2/2020.
 * 普通类被Spring管理
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues={RabbitConfig.QUEUE_INFORM_EMAIL})
    public void testEmailMQ(String msg){
        System.out.println("email:"+msg);
    }

    @RabbitListener(queues={RabbitConfig.QUEUE_INFORM_SMS})
    public void testSmsMQ(String msg){
        System.out.println("sms:"+msg);
    }
}
