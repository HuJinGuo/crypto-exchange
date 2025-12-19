package com.exchange.common.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.Time;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisLock {

    private final RedissonClient redissonClient;

    public RedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> T executeWithLock(
            String key,
            long waitSeconds,
            long leaseSeconds,
            Supplier<T> supplier
    ){
        RLock lock = redissonClient.getLock(key);
        try{
            if (!lock.tryLock(waitSeconds,
                    leaseSeconds, TimeUnit.SECONDS)){
                throw new RuntimeException("获取锁失败");
            }
            return supplier.get();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("锁中断",e);
        }finally {
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }



    }
}
