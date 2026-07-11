package com.liubo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configurable
@EnableScheduling
@MapperScan(basePackages = "com.liubo.infrastructure.persistent.dao")
@EnableDubbo
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
