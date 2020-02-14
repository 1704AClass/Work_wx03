package com.ningmeng.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

public class Producer04 {

    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    private static final String QUEUE_INFORM_SMS="queue_inform_sms";


    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("127.0.0.1");

            factory.setPort(5672);

            factory.setUsername("guest");

            factory.setPassword("guest");

            factory.setVirtualHost("/");

            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();

            //交换机声明

            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);

            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,"inform.email");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,"inform.sms");
            for (int i = 0;i<5;i++){
                String message = "德玛西亚";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform email",null,message.getBytes());
                System.out.println("send"+message+",时间"+new Date());
            }
            for (int i=0;i<10;i++){
                String message = "我就是暗裔"+i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform sms", null, message.getBytes());
                System.out.println("Send Message is:'" + message + "'");
            }
            for (int i=0;i<10;i++){
                String message = "我是拉雅斯特"+i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms.email", null, message.getBytes());
                System.out.println("Send Message is:'" + message + "'");
            }
                channel.close();
                connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }


}
