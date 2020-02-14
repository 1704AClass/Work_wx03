package com.ningmeng.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

public class Producer02 {

    private static final String QUEUE_INFORM_EMAIL = "inform_queue_email";
    private static final String EXCHANGE_FANOUT_INFORM="inform_exchange_fanout";
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

            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            //交换机声明

            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");

            for (int i = 0;i<5;i++){
                String manage = "德玛西亚";
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,manage.getBytes());
                System.out.println("send"+manage+",时间"+new Date());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
