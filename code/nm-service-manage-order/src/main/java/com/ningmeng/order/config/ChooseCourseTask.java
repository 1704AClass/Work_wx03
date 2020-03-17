package com.ningmeng.order.config;

import com.ningmeng.framework.domain.task.NmTask;
import com.ningmeng.order.service.TaskService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Resource
    private TaskService taskService;

    //每隔一分钟扫描消息表
    //@Scheduled(fixedDelay = 60000)
    @Scheduled(cron = "0 * * * * ? ")
    public void sendChoosecourseTask(){
        //取出当前时间1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<NmTask> taskList = taskService.findTaskList(time,1000);
        for(NmTask nmTask:taskList){
            //任务Id
            String taskId = nmTask.getId();
            //版本号
            Integer version = nmTask.getVersion();
            //调用乐观锁方法效验任务是否可以执行
            if(taskService.getTask(taskId,version) > 0){
                //发送选课消息
                taskService.publish(nmTask,nmTask.getMqExchange(),nmTask.getMqRoutingkey());
                LOGGER.info("send choose course task id:{}",nmTask.getId());
            }
        }
    }


    @RabbitListener(queues = {RabbitMQConfig.NM_LEARNING_FINISHADDCHOOSECOURSE})
    public void receiveFinishChoosecourseTask(NmTask task, Message message, Channel channel) throws IOException {
        LOGGER.info("receiveChoosecourseTask. . .{}",task.getId());
        //接收到的消息id
        String id = task.getId();
        //删除任务，添加历史任务
        taskService.finishTask(id);
    }



}
