/*
 * Copyright (c) 2017. iylyi.com. All rights Reserved.
 */

package com.iylyi.throwable.retry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retry: 是否重试注解.（仅适用于完整操作，事务中间操作一般无效）
 *
 * @author iylyi
 * @version 1.00
 * @since 2017-04-14 11:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    /**
     * 是否重试
     */
    boolean confirm() default false;

    /**
     * 最大重试次数
     */
    int maxAttempts() default 3;

    /**
     * 重试异常类型
     */
    Class<? extends Throwable> when() default Exception.class;

    /**
     * 不包含异常
     */
    Class<? extends Throwable>[] excludes() default {};

}
