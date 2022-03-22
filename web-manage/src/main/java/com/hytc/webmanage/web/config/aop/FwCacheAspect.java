package com.hytc.webmanage.web.config.aop;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import jp.co.gt.fw.common.jackson.FwJacksonConverter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@ConditionalOnProperty(prefix = "spring.redis.data", name = "enable", havingValue = "true") // 条件起動
@Aspect
public class FwCacheAspect extends FwBaseAspect {

    private static final Map<String, Object> _g_keyLocks = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FwJacksonConverter jsonConverter;

    @Pointcut("@annotation(jp.co.jsto.web.config.aop.FwCacheable)")
    public void cacheableAspect() {
    }

    @Pointcut("@annotation(jp.co.jsto.web.config.aop.FwCacheEvict)")
    public void cacheEvictAspect() {
    }

    @Around("cacheableAspect()")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        log.debug("{}", args);

        // 戻り値タイプ取得
        Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();

        // メソッド取得
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // メソッドからアノテーション取得
        FwCacheable ca = method.getAnnotation(FwCacheable.class);

        // SPEL解析済みKEY
        final String key = super.parseKey(ca.key(), method, args);
        Class<?> elementClass = ca.type();

        // アノテーションからname取得
        String name = ca.value();

        synchronized (_g_keyLocks.computeIfAbsent(key, it -> new Object())) { // 有效缓解缓存雪崩
            try {
                /** L2:redis から取得してみる */
                final String redisKey = String.format("%s:%s", name, key);
                String strL2 = redisTemplate.opsForValue().get(redisKey);
                if (strL2 != null) {
                    return this.jsonToObj(strL2, returnType, elementClass);
                }
                /** L3:DB から取得 */
                Object objDB = joinPoint.proceed(args);
                if (objDB != null) {
                    // L1 を更新
                    final String writeValueAsString = jsonConverter.objToJson(objDB);
                    // L2 を更新
                    redisTemplate.opsForValue().set(redisKey, writeValueAsString);
                }
                return objDB;
            } finally {
                _g_keyLocks.remove(key);
            }
        }
    }

    @Around("cacheEvictAspect()")
    public Object evict(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.debug("{}", args);
        return joinPoint.proceed(args);
    }

    /**
     * キャッシュJSON情報から対応オブジェクト変換
     *
     * @param json
     * @param returnType
     * @param elementClass
     * @return Object
     */
    private Object jsonToObj(String json, Class<?> returnType, Class<?> elementClass) {
        // Listの場合、正しいJSON変換にする
        if (Collection.class.isAssignableFrom(returnType)) {
            JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass);
            return jsonConverter.jsonToObj(json, type);
        }
        // Mapの場合、JSON変換可
        if (Map.class.isAssignableFrom(returnType)) {
            JavaType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, elementClass);
            return jsonConverter.jsonToObj(json, type);
        }
        return jsonConverter.jsonToObj(json, returnType);
    }
}
