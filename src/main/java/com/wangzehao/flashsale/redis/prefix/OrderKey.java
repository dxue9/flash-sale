package com.wangzehao.flashsale.redis.prefix;

public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSaleOrderByUidGid = new OrderKey("moug");
}
