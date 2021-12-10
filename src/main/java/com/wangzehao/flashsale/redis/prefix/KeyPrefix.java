package com.wangzehao.flashsale.redis.prefix;

public interface KeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;
}