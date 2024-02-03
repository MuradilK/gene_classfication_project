package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication()
@ComponentScan(basePackages = "com.example.demo.*")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //LinkedHashSet
        //TreeSe
        //ArrayList al;
        //TransactionStatus transactionStatus;
        //PlatformTransactionManager;
        //ConcurrentHashMap concurrentHashMap;
    }

}

