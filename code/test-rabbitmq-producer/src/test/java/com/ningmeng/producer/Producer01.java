package com.ningmeng.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * 入门程序 生产者
 * Created by BJDGZJD on 13/2/2020.
 */
public class Producer01 {
    private static final String QUEUE="helloworld";
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
           channel.queueDeclare(QUEUE,true,false,false,null);
           /**
            * 消息发布方法
            * String exchange, 交换机 如果用的是普通队列 交换机名称可以为""
            * String routingKey,消息的路由Key，是用于Exchange（交换机）根据routingKey将消息转发到指定的消息队列
            * BasicProperties props, 消息包含的属性，工作中用的很少
            * byte[] body 消息主体
            */
           String manage = "小明你好";
           System.out.println("send :"+manage+"，时间："+new Date());
           channel.basicPublish("",QUEUE,null,manage.getBytes());

       }catch (Exception e){
           e.printStackTrace();
       }

    }
}
