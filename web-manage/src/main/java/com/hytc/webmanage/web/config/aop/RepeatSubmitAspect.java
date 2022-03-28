package com.hytc.webmanage.web.config.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.hytc.webmanage.web.config.handler.ErrorResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@ConditionalOnProperty(prefix = "spring.redis.data", name = "enable", havingValue = "true") // 条件起動
@Aspect
public class RepeatSubmitAspect extends FwBaseAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(repeatSubmit)")
    public void pointcut(RepeatSubmit repeatSubmit) {
    }

    @Around("pointcut(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final String uniqueKey = super.parseKey(repeatSubmit.uniqueKey(), method, joinPoint.getArgs());
        log.info("uniqueKey:{}", uniqueKey);
        final String redisKey = String.format("lock:%s:%s", repeatSubmit.funcKey(), uniqueKey);
        if (redisTemplate.opsForValue().setIfAbsent(redisKey, "1", 10, TimeUnit.SECONDS)) {
            try {
                return joinPoint.proceed(joinPoint.getArgs());
            } finally {
                log.info("delete key: {}", redisKey);
                redisTemplate.delete(redisKey);
            }
        } else {
            log.warn("can not get lock: {}", redisKey);
            ErrorResponse response = new ErrorResponse();
            response.setType("RepeatSubmitException");
            response.setCode("xxxxx");
            response.setMessage("Repeat Submit");
            return response;
        }
    }
}
