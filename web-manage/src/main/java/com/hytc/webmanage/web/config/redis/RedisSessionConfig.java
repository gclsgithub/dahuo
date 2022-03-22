package com.hytc.webmanage.web.config.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * spring-session を redis に保存する設定クラス。<br>
 * @EnableRedisHttpSession と<br>
 * spring.session.store-type=redis->none<br>
 * セットで設定。<br>
 * redis じゃない場合のセッションID：F3AD7D9C16DADDF5F33BF7EF3F35BAC6<br>
 * redis の場合のセッションID：d6c12d19-20ac-43a8-8cf8-64f1be63b388<br>
 * 特徴：<br>
 * redis の場合、サーバー再起動しても、セッションんが残る。（redis に保存したため）
 *
 * @see apwebication.properties#server.servlet.session.timeout
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.redis.session", name = "enable", havingValue = "true") // 条件起動
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60) // seconds
public class RedisSessionConfig {
}
