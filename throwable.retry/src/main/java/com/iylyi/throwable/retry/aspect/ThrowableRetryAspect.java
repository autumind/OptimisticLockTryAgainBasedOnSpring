/*
 * Copyright (c) 2014 - 2017. MOBCB Technology Co.,Ltd. All rights Reserved.
 */

package com.iylyi.throwable.retry.aspect;

import com.iylyi.throwable.retry.annotation.Retry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ThrowableRetryAspect: 异常重试切面,适用于@{@link Retry}注解.<br/><br/>
 * {@link Retry}注解参数可以设置是否重试以及重试次数, 除非配置文件中包含并且全部包含以下参数, 配置文件参数将覆盖注解参数：<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;1. retry.confirm({@link Boolean}) <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;2. retry.max.attempts({@link Integer})<br/>
 * <small>注：配置文件是以 @{@link Value} 方式注入，如果想使用配置文件，需要注意配置文件是否加载到Spring中。<small/>
 *
 * @author shen.zhibing
 * @version 1.00
 * @since 2017-04-14 11:09
 */
@Aspect
@Order(1) // 定义本切面的优先级设定为高于事务通知
@Component
public class ThrowableRetryAspect {

    /**
     * 日志
     */
    public static final Log LOGGER = LogFactory.getLog(ThrowableRetryAspect.class);

    @Value("${retry.confirm:null}")
    private String confirm;

    @Value("${retry.max.attempts:null}")
    private String maxAttempts;

    /**
     * 含有Retry注解的方法将会被拦截
     *
     * @param pjp  执行切入点
     * @param anno 切入执行方法注解
     * @return 执行结果
     */
    @Around(value = "@annotation(anno)")
    public Object retry(ProceedingJoinPoint pjp, Retry anno) throws Throwable {
        int attempts = 0;
        Throwable ta;
        Object proceed;

        boolean retry;
        int maxAttempts;
        if (this.confirm == null || this.maxAttempts == null) {
            retry = anno.confirm();
            maxAttempts = anno.maxAttempts();
        } else {
            retry = Boolean.valueOf(this.confirm);
            maxAttempts = Integer.valueOf(this.maxAttempts);
        }
        do {
            attempts++;
            ta = null;
            try {
                proceed = pjp.proceed();
            } catch (Throwable throwable) {
                LOGGER.error("任务执行时发生异常：", throwable);
                if (attempts > 1) {
                    LOGGER.info("【" + pjp.getSignature().toString() + "】任务第(" + (attempts - 1) + "/" + maxAttempts + ")次重试失败!!!");
                }
                ta = throwable;
                if (anno.retryFor().isInstance(ta)) {
                    if (retry && attempts <= maxAttempts) { // 确认重试且重试次数小于最大重试次数时进行任务重试，否则直接抛出异常
                        LOGGER.info("【" + pjp.getSignature().toString() + "】任务执行第(" + attempts + "/" + maxAttempts + ")次重试...");
                    } else {
                        throw throwable;
                    }
                } else {
                    throw throwable;
                }
                proceed = null;
            }
            if (attempts > 1 && ta == null) {
                LOGGER.info("【" + pjp.getSignature().toString() + "】任务第(" + (attempts - 1) + "/" + maxAttempts + ")次重试成功!!!");
            }
        } while (ta != null); // 异常不为空

        return proceed;
    }

}
