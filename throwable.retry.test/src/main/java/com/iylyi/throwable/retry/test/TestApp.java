/*
 * Copyright (c) 2014 - 2017. MOBCB Technology Co.,Ltd. All rights Reserved.
 */

package com.iylyi.throwable.retry.test;

import com.iylyi.throwable.retry.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TestApp: Description of TestApp.
 *
 * @author shen.zhibing
 * @version 1.00
 * @since 2017-10-10 15:55
 */
@SpringBootApplication
@ComponentScan(value = "com.iylyi")
public class TestApp implements CommandLineRunner {
    private TestService testService;

    @Autowired
    public TestApp(TestService testService) {
        this.testService = testService;
    }


    @Override
    public void run(String... strings) throws Exception {
        testService.retry();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestApp.class, args);
    }
}
