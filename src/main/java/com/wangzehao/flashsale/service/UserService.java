package com.wangzehao.flashsale.service;

import com.wangzehao.flashsale.dao.UserDao;
import com.wangzehao.flashsale.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    public UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }
}
