package com.ningmeng.test.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String QUEUE_INFORM_EMAIL ="queue_inform_email";


    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        Queue queue = new Queue(QUEUE_INFORM_SMS);
        return queue;
    }

    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        Queue queue = new Queue(QUEUE_INFORM_EMAIL);
        return queue;
    }
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange,@Qualifier(QUEUE_INFORM_SMS) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }


    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange,@Qualifier(QUEUE_INFORM_EMAIL) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }



}
