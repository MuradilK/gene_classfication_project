package com.example.demo.logAOP;



import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashSet;


@Aspect
public class WriteLog {


    @Before("execution(* com.example.demo.Controller.RocketMQController.myLogAndAspectTest(..))")
    public void logText(){      //IDEA导航到顾问方法
        Logger logger = LoggerFactory.getLogger(WriteLog.class);
        logger.info(this.getClass().toString()+"记录前置通知");
        HashSet set = new HashSet();
        int i=1;
        String resource = "mybatis-config.xml";
        
        InputStream inputStream = null;
        //SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }


}
