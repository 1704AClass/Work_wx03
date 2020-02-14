package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer01 {
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

            DefaultConsumer consumer=new DefaultConsumer(channel){
                public void handleDelivery(String concumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body)throws  IOException{
                    String exchange = envelope.getExchange();
                    String routingKey = envelope.getRoutingKey();
                    long deliveryTag = envelope.getDeliveryTag();
                    String msg = new String(body, "utf-8");
                    System.out.println("message..."+msg);
                }
            };
            channel.basicConsume(QUEUE, true, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
