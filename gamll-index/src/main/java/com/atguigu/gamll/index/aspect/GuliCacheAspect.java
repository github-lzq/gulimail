package com.atguigu.gamll.index.aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.gamll.index.annotation.GuliCache;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class GuliCacheAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 1.方法的返回值必须是Object
     * 2.方法的参数必须有一个ProceedingJoinPoint
     * 3.方法必须抛出Throwable
     * 4.执行目标方法:proceedingJoinPoint.proceed(args)
     */
    @Around("@annotation(com.atguigu.gamll.index.annotation.GuliCache)")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result=null;
        //获取方法上的注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        GuliCache annotation = method.getAnnotation(GuliCache.class);
        //获取注解中的属性
        String value = annotation.value();
        String prefix = StringUtils.isNotBlank(value) ? value : annotation.prefix();
        //缓存中的key值
        String arg = Arrays.asList(joinPoint.getArgs()).toString();
        String key = prefix + arg;

        //获取缓存中的数据
        result = this.cacheHit(method, key);
        if (result!=null)
            return result;

        //加分布式锁
        RLock lock = this.redissonClient.getLock("lock" + arg);
        lock.lock(2,TimeUnit.SECONDS);

        //获取缓存中的数据，防止加锁之后已经有缓存了
        result = this.cacheHit(method, key);
        if (result!=null){
            lock.unlock();
            return result;
        }
        //执行目标方法
        result = joinPoint.proceed(joinPoint.getArgs());
        //放入缓存
        this.stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(result),30,TimeUnit.DAYS);
        lock.unlock();
        return result;
    }

    //获取缓存中数据判断是否为空
    private Object cacheHit(Method method,String key){
        String jsonString = this.stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(jsonString)) {

            return JSON.parseObject(jsonString, method.getReturnType());
    }
   return null;
  }
}
