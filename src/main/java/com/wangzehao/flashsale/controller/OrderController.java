package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.domain.OrderInfo;
import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.service.GoodsService;
import com.wangzehao.flashsale.service.OrderService;
import com.wangzehao.flashsale.service.UserService;
import com.wangzehao.flashsale.vo.GoodsVo;
import com.wangzehao.flashsale.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public OrderDetailVo orderInfo(Model model, SaleUser user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return null;
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
            return null;
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoods(goods);
        return orderDetailVo;
    }
}
