package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer03 {

    private static final String QUEUE_INFORM_EMAIL = "inform_queue_email";
    private static final String EXCHANGE_ROUTING_INFORM="inform_exchange_routing";
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
        channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 long deliveryTag = envelope.getDeliveryTag(); String exchange = envelope.getExchange();
                 String message = new String(body, "UTF-8");
                 System.out.println(message);

            }
        };
        channel.basicConsume(QUEUE_INFORM_EMAIL, true, defaultConsumer);
    }
}
