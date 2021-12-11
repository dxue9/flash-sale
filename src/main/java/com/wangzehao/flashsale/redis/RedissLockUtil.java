package com.wangzehao.flashsale.redis;

import java.util.concurrent.TimeUnit;

public class RedissLockUtil {
    private static DistributedLocker redissLock;

    public static void setLocker(DistributedLocker locker) {
        redissLock = locker;
    }

    public static void lock(String lockKey) {
        redissLock.lock(lockKey);
    }

    public static void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    public static void lock(String lockKey, int timeout) {
        redissLock.lock(lockKey, timeout);
    }

    public static void lock(String lockKey, TimeUnit unit , int timeout) {
        redissLock.lock(lockKey, unit, timeout);
    }
}