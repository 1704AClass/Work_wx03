package com.ningmeng.test.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by BJDGZJD on 14/2/2020.
 * 使用springBoot 初始化 RabbitConfig类
 * 之前用spring的时候 XML Bean
 * springBoot没有XML 使用javaBean的方式替代了 原来的XML方式的配置
 *
 * Configuration  == xml
 */
@Configuration
public class RabbitConfig {

    //交换器的名字
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //队列名字
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //队列Email
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    /**
     * 交换机配置 -- 声明一个交换机
     * durable 持续
     * @Bean(EXCHANGE_TOPICS_INFORM) 等价于 <bean id=""></bean>
     */
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    //声明队列

    /**
     *
     * @return
     */
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        Queue queue=new Queue(QUEUE_INFORM_SMS);
        return queue;

    }
    //声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        Queue queue=new Queue(QUEUE_INFORM_EMAIL);
        return queue;
    }

    /**
     * 绑定队列到交换机.
     * 括号中需要引用 Exchange 与 Queue 对象进行绑定
     * <ref id=""></ref>
     * <ref id=""></ref>
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange,@Qualifier(QUEUE_INFORM_SMS) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange,@Qualifier(QUEUE_INFORM_EMAIL) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }

}
