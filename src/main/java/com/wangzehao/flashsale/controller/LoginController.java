package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.service.SaleUserService;
import com.wangzehao.flashsale.service.UserService;
import com.wangzehao.flashsale.vo.LoginVo;
import org.apache.zookeeper.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SaleUserService saleUserService;

    @RequestMapping("/to_login")
    public String toLogin(LoginVo loginVo, Model model) {
        logger.info(loginVo.toString());
        model.addAttribute("count",1);
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public String doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        if(saleUserService.login(response, loginVo)){
            return "success";
        }
        return "false";
    }
}
