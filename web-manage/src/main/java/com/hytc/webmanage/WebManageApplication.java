package com.hytc.webmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication( //
        scanBasePackageClasses = {WebManageApplication.class} //
        , exclude = {RedisAutoConfiguration.class} //
)
public class WebManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebManageApplication.class, args);
    }

}



