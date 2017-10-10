/*
 * Copyright (c) 2017. iylyl.com. All rights Reserved.
 */

package com.iylyi.throwable.retry.test.service;

import com.iylyi.throwable.retry.annotation.Retry;
import org.springframework.stereotype.Service;

/**
 * TestService: Description of TestService.
 *
 * @author iylyi
 * @version 1.00
 * @since 2017-10-10 19:32
 */
@Service
public class TestService {

    @Retry(confirm = true)
    public void retry() throws Exception {
        Thread.sleep(3000);
        throw new Exception("测试异常信息");
    }
}
