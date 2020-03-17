package com.ningmeng.test;

import com.ningmeng.test.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by BJDGZJD on 14/2/2020.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class rabbitmq {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopics(){
        for(int i=0;i<5;i++){
            String message="sms email inform to user"+i;
            /**
             * 第一个参数：交换器名字
             * 第二个参数：路由Key
             * 第三个参数：发送的信息
             */
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_INFORM,"inform.sms.email",message);
            System.out.println("SendMessageis:'"+message+"'");
        }
    }

}
