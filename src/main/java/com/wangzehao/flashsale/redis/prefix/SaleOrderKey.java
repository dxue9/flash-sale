package com.wangzehao.flashsale.redis.prefix;

public class SaleOrderKey extends BasePrefix{
    private SaleOrderKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SaleOrderKey isGoodsOver = new SaleOrderKey(0, "go");
    public static SaleOrderKey getSalePath = new SaleOrderKey(60, "mp");
    public static SaleOrderKey getVerifyCode = new SaleOrderKey(300, "vc");
    public static SaleOrderKey getVerifyCodeRegister = new SaleOrderKey(300, "register");
}
