package com.ningmeng.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by BJDGZJD on 10/2/2020.
 */
@SpringBootApplication
//扫描类
@ComponentScan(basePackages = {"com.ningmeng.test"})
@ComponentScan(basePackages = {"com.ningmeng.framework"})
public class ManageCmsApplication {
    public static void main(String[]args){
        SpringApplication.run(ManageCmsApplication.class,args);
    }
}
