package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.service.GoodsService;
import com.wangzehao.flashsale.service.SaleUserService;
import com.wangzehao.flashsale.service.UserService;
import com.wangzehao.flashsale.vo.GoodsDetailVo;
import com.wangzehao.flashsale.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SaleUserService saleUserService;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SaleUser user){
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return render(request, response, model, "goods_list");
    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public GoodsDetailVo detail(HttpServletRequest request, HttpServletResponse response, SaleUser user,
                                             @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status = 0;
        int remainSeconds = 0;
        if(now < startAt ) { // not begin
            status = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){ // end
            status = 2;
            remainSeconds = -1;
        }else { // onging
            status = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setStatus(status);
        return vo;
    }

    @RequestMapping(value="/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String renderDetail(HttpServletRequest request, HttpServletResponse response, Model model, SaleUser user,
                                @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status = 0;
        int remainSeconds = 0;
        if(now < startAt ) { // not begin
            status = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){ // end
            status = 2;
            remainSeconds = -1;
        }else { // onging
            status = 1;
            remainSeconds = 0;
        }
        model.addAttribute("status", status);
        model.addAttribute("remainSeconds", remainSeconds);

        return render(request, response, model, "goods_detail");
    }
}
