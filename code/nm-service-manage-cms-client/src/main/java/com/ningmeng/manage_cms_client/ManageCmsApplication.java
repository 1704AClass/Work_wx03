package com.ningmeng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//扫描实体类
@EntityScan("com.ningmeng.framework.domain.cms")
//扫描common工程下的类
@ComponentScan(basePackages = "com.ningmeng.framework")
//扫描本项目下的所有类
@ComponentScan(basePackages={"com.ningmeng.manage_cms_client"})
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class,args);
    }
}
