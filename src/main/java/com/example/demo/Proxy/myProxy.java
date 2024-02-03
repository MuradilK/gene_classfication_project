package com.example.demo.Proxy;


import com.example.demo.logAOP.WriteLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class myProxy {

//    @Bean
//    public RocketMQController RocketMQController(){
//        return new RocketMQController();
//    }

    @Bean
    public WriteLog writeLog(){
        return new WriteLog();
    }

}
