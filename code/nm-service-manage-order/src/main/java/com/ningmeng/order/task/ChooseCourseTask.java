package com.ningmeng.order.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChooseCourseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);


    @Scheduled(cron = "0/5 * * * * ?")
    public void tsak1(){
        LOGGER.info("===================测试定时任务1开始=================");
        try {
            //Thread.sleep(5000);
            System.out.println("测试定时1");
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("===================测试定时任务1结束==================");
    }

    @Scheduled(fixedRate = 3000)//上次执行开始时间后5秒执行
    public void task2(){
        LOGGER.info("===================测试定时任务2开始=================");
        try {
            //Thread.sleep(5000);
            System.out.println("测试定时2");
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("===================测试定时任务2结束==================");
    }



}
