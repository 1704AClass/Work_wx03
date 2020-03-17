package com.ningmeng.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    //添加选课任务交换机
    public static final String EX_LEARNING_ADDCHOOSECOURSE = "ex_learning_addchoosecourse";

    //添加选课消息队列
    public static final String NM_LEARNING_ADDCHOOSECOURSE = "nm_learning_addchoosecourse";

    //完成添加选课消息队列
    public static final String NM_LEARNING_FINISHADDCHOOSECOURSE = "nm_learning_finishaddchoosecourse";

    //添加选课路由key
    public static final String NM_LEARNING_ADDCHOOSECOURSE_KEY = "addchoosecourse";
    //完成添加选课路由key
    public static final String NM_LEARNING_FINISHADDCHOOSECOURSE_KEY = "finishaddchoosecourse";

    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean(EX_LEARNING_ADDCHOOSECOURSE)
    public Exchange EX_DECLARE() {
        return ExchangeBuilder.directExchange(EX_LEARNING_ADDCHOOSECOURSE).durable(true).build();
    }
    //声明队列
    @Bean(NM_LEARNING_FINISHADDCHOOSECOURSE)
    public Queue QUEUE_DECLARE() {
        Queue queue = new Queue(NM_LEARNING_FINISHADDCHOOSECOURSE,true,false,true);
        return queue;
    }

    @Bean(NM_LEARNING_ADDCHOOSECOURSE)
    public Queue QUEUE_NM_LEARNING_ADDCHOOSECOURSE(){
        Queue queue = new Queue(NM_LEARNING_ADDCHOOSECOURSE);
        return queue;
    }

    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_FINISHADDCHOOSECOURSE(@Qualifier(NM_LEARNING_FINISHADDCHOOSECOURSE) Queue queue,
                                                       @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(NM_LEARNING_FINISHADDCHOOSECOURSE_KEY).noargs();
    }


    @Bean
    public Binding binding_queue_media_processtask(@Qualifier("nm_learning_finishaddchoosecourse") Queue queue, @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(NM_LEARNING_FINISHADDCHOOSECOURSE_KEY).noargs();
    }






















}
