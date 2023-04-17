package com.jack.clientauthority.processor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class NeedPermissionProcessor {

    @Pointcut("@annotation(com.jack.clientauthority.annotation.NeedPermission)")
    public void NeedPermission() {}

    @Before(value = "NeedPermission()")
    public void before(JoinPoint joinPoint) {
        log.error("====>测试切面逻辑");
        throw new RuntimeException("测试切面逻辑");
    }
}
