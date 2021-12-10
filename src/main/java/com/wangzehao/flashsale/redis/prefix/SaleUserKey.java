package com.wangzehao.flashsale.redis.prefix;

public class SaleUserKey extends BasePrefix{
    public static final int TOKEN_EXPIRE = 3600*24*2;
    public static SaleUserKey token = new SaleUserKey(TOKEN_EXPIRE,"tk") ;
    public static SaleUserKey getByNickName = new SaleUserKey(0, "nickName");

    public SaleUserKey(int expireSeconds ,String prefix) {
        super(expireSeconds,prefix);
    }
}
