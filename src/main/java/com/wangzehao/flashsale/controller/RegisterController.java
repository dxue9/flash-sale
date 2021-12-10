package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.dao.SaleUserDao;
import com.wangzehao.flashsale.service.SaleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class RegisterController {
    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    SaleUserService saleUserService;

    @RequestMapping("/do_register")
    public String doRegister(){
        return "register";
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("salt") String salt,
                           HttpServletResponse response){
        if(!saleUserService.register(response, username, password, salt)){
            return "false";
        }
        return "true";
    }
}
