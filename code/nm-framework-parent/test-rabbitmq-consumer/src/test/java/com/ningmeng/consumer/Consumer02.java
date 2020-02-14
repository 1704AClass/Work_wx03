package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer02 {

    private static final String QUEUE_INFORM_EMAIL = "inform_queue_email";
    private static final String EXCHANGE_FANOUT_INFORM="inform_exchange_fanout";

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

            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");

            DefaultConsumer consumer=new DefaultConsumer(channel){
                public void handleDelivery(String concumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body)throws  IOException{
                    String exchange = envelope.getExchange();
                    String routingKey = envelope.getRoutingKey();
                    long deliveryTag = envelope.getDeliveryTag();
                    String msg = new String(body, "utf-8");
                    System.out.println("message..."+msg);
                }
            };
            channel.basicConsume(QUEUE_INFORM_EMAIL,true, consumer);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
