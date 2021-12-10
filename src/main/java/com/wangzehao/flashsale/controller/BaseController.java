package com.wangzehao.flashsale.controller;

import com.google.common.net.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
public class BaseController {
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    public String render(HttpServletRequest request, HttpServletResponse response, Model model, String tplName){
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        String html = thymeleafViewResolver.getTemplateEngine().process(tplName, ctx);
        out(response, html);
        return null;
    }

    public static void out(HttpServletResponse response, String html){
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try{
            OutputStream out = response.getOutputStream();
            out.write(html.getBytes("UTF-8"));
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
