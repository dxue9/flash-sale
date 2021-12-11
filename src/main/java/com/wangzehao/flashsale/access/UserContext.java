package com.wangzehao.flashsale.access;
import com.wangzehao.flashsale.domain.SaleUser;

public class UserContext {

    private static ThreadLocal<SaleUser> userHolder = new ThreadLocal<SaleUser>();

    public static void setUser(SaleUser user) {
        userHolder.set(user);
    }

    public static SaleUser getUser() {
        return userHolder.get();
    }

    public static void removeUser() {
        userHolder.remove();
    }

}
