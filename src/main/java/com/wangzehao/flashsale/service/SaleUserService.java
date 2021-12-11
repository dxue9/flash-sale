package com.wangzehao.flashsale.service;

import com.wangzehao.flashsale.dao.SaleUserDao;
import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.redis.RedisService;
import com.wangzehao.flashsale.redis.prefix.SaleUserKey;
import com.wangzehao.flashsale.util.Md5;
import com.wangzehao.flashsale.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
public class SaleUserService {

    @Autowired
    private SaleUserDao saleUserDao;

    @Autowired
    private RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";
    private static Logger logger = LoggerFactory.getLogger(SaleUserService.class);

    public SaleUser getByToken(HttpServletResponse response , String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }
        SaleUser user =redisService.get(SaleUserKey.token,token,SaleUser.class) ;
        if(user!=null) {
            addCookie(response, token, user);
        }
        return user ;

    }

    public SaleUser getByNickname(String nickName){
        // From Redis Cache
        SaleUser user = redisService.get(SaleUserKey.getByNickName, ""+nickName, SaleUser.class);
        if(user != null) {
            return user;
        }

        // From databae
        user = saleUserDao.getByNickname(nickName);
        if(user != null) {
            redisService.set(SaleUserKey.getByNickName, ""+nickName, user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo==null){
            return false;
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        SaleUser user = saleUserDao.getByNickname(mobile);
        if(user==null){
            return false;
        }
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        if(!Md5.formPass2DBPass(password,salt).equals(dbPass)){
            return false;
        }

        String token = UUID.randomUUID().toString().replace("-","");
        addCookie(response, token, user);
        return true;
    }

    public boolean register(HttpServletResponse response, String userName, String password, String salt){

        SaleUser saleUserOld = saleUserDao.getByNickname(userName);
        if(saleUserOld != null){
            return false;
        }

        SaleUser saleUser = new SaleUser();
        saleUser.setNickname(userName);
        saleUser.setPassword(Md5.formPass2DBPass(password,salt));
        saleUser.setRegisterDate(new Date());
        saleUser.setSalt(salt);
        try {
            saleUserDao.insert(saleUser);
            SaleUser saleUserNew = saleUserDao.getByNickname(userName);
            if(saleUserNew == null){
                return false;
            }
            // set Cookie: distributed sessions
            String token = UUID.randomUUID().toString().replace("-","");
            addCookie(response, token, saleUserNew);
        }catch (Exception e){
            logger.error("Register Failed", e);
            return false;
        }
        return true;
    }

    public boolean updatePassword(HttpServletResponse response, String nickName, String password){
        SaleUser saleUser = saleUserDao.getByNickname(nickName);
        if (saleUser == null){
            return false;
        }
        SaleUser saleUserUpdate = new SaleUser();
        saleUserUpdate.setPassword(Md5.formPass2DBPass(password, saleUser.getSalt()));
        saleUserDao.update(saleUserUpdate);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, SaleUser user) {
        redisService.set(SaleUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        System.out.println(token);
        response.addCookie(cookie);
    }

}
