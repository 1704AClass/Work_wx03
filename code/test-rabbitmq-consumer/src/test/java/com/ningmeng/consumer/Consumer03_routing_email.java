package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by BJDGZJD on 13/2/2020.
 */
public class Consumer03_routing_email {
    //发送邮件
    private static final String QUEUE_INFORM_EMAIL="queue_inform_email";
    //FANOUT类型的交换器
    private static final String EXCHANGE_ROUTING_INFORM="inform_exchange_routing";

    public static void main(String[] args) {

        try {
            //创建初始化连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            //浏览器管理页面使用端口：15672 后台使用端口:5672
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器

            //创建连接
            Connection connection = factory.newConnection();
            //生产者和Broker建立通道。（信道）
            // 每个连接可以创建多个通道，每个通道代表一个会话任务
            Channel channel = connection.createChannel();

            /**
             * 声明队列 如果Rabbit中没有此队列将自动创建
             * String queue  队列名称,
             * boolean durable 是否持久化 如果你的rabbitMQ重新启动，消息会不会丢失,如果durable为true持久化，为false不持久化
             * boolean exclusive 排他，互斥,队列是否独占此连接，如果true就独占连接 false就是不独占
             * boolean autoDelete 是否自动删除, true 用完队列就删除  false 用完队列不删除  如果exclusive为true和autoDelete为true 此队列变成临时队列
             * Map<String, Object> arguments  队列参数 设置队列存活时间等等
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            //交换机声明  String exchange交换机名称,BuiltinExchangeTypetype 交换机类型
            /**
             *  DIRECT("direct"),
             FANOUT("fanout"),发布/订阅
             TOPIC("topic"),
             HEADERS("headers")
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);

            //交换机和队列绑定String queue,String exchange,String routingKey
            //参数明细
            //队列名称
            //2、交换机名称
            //3、路由key 发布订阅不用设置路由
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);

            //消费消息方法
            Consumer consumer = new DefaultConsumer(channel){
                /**
                 *
                 * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
                 * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志 (收到消息失败后是否需要重新发送)
                 * @param properties 属性参数
                 * @param body 消息主体
                 * @throws IOException
                 */
                public void handleDelivery(String consumerTag,
                                           Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    String rouKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag=envelope.getDeliveryTag();
                    String str = new String(body,"utf-8");
                    System.out.println("receivemessage111.."+str);
                }

            };
            channel.basicConsume(QUEUE_INFORM_EMAIL,true,consumer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
