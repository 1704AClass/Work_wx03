package com.ningmeng.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

public class Producer01 {
    private static final String QUEUE="helloworld";
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

                channel.queueDeclare(QUEUE,true,false,false,null);

                String manage = "德玛西亚";
                System.out.println("send"+manage+",时间"+new Date());
                channel.basicPublish("",QUEUE,null,manage.getBytes());

            }catch (Exception e){
                e.printStackTrace();
            }

    }


}
