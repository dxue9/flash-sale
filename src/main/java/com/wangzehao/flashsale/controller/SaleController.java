package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.access.AccessLimit;
import com.wangzehao.flashsale.domain.OrderInfo;
import com.wangzehao.flashsale.domain.SaleOrder;
import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.service.GoodsService;
import com.wangzehao.flashsale.service.OrderService;
import com.wangzehao.flashsale.service.SaleService;
import com.wangzehao.flashsale.service.SaleUserService;
import com.wangzehao.flashsale.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController{

    @Autowired
    private SaleUserService saleUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SaleService saleService;

    private static Logger logger = LoggerFactory.getLogger(SaleController.class);

//    /**
//     * system initialization
//     *
//     * @throws Exception
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        List<GoodsVo> goodsList = goodsService.listGoodsVo();
//        if (goodsList == null) {
//            return;
//        }
//        for (GoodsVo goods : goodsList) {
////            缓存
////            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
////            localOverMap.put(goods.getId(), false);
//        }
//    }

//    @RequestMapping(value = "/verifyCodeRegister")
//    @ResponseBody
//    public String getVerifyCode(HttpServletResponse response){
//        try {
//            BufferedImage image = saleService.createVerifyCodeRegister();
//            OutputStream out = response.getOutputStream();
//            ImageIO.write(image, "JPEG", out);
//            out.flush();
//            out.close();
//            return "success";
//        } catch (Exception e) {
//            logger.error("generate verify code error in register{}", e);
//            return "error";
//        }
//    }

//    @RequestMapping(value = "/verifyCode")
//    @ResponseBody
//    public String getVerifyCode(HttpServletResponse response, SaleUser user,
//                                                   @RequestParam("goodsId") long goodsId) {
//        if (user == null) {
//            return "error";
//        }
//        try {
//            BufferedImage image = saleService.createVerifyCode(user, goodsId);
//            OutputStream out = response.getOutputStream();
//            ImageIO.write(image, "JPEG", out);
//            out.flush();
//            out.close();
//            return "success";
//        } catch (Exception e) {
//            logger.error("generate verify code error in register{}", e);
//            return "error";
//        }
//    }

    @AccessLimit(needLogin = true)
    @RequestMapping(value="/path")
    @ResponseBody
    public String getSalePath(HttpServletRequest request, SaleUser user,
                              @RequestParam("goodsId") long goodsId){
        if(user == null){
            return "error";
        }
        return saleService.createBuyPath(user, goodsId);
    }

    @AccessLimit(needLogin = true)
    @RequestMapping(value = "/{path}/do_sale", method = RequestMethod.POST)
    @ResponseBody
    public String placeOrder(Model model, SaleUser user, @PathVariable("path") String path,
                             @RequestParam("goodsId") long goodsId){
        if(user == null)    {
            return "error";
        }
        System.out.println(user);
        // check if have existing order
        SaleOrder order = orderService.getSaleOrderByUserIdGoodsId(user.getNickname(), goodsId);
        if (order != null) {
            return "error";
        }

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0) {
            return "error";
        }
        OrderInfo orderInfo = saleService.sale(user, goodsVo);
        if(orderInfo == null) {
            return "error";
        }
        return "success";
    }

    /**
     * orderId：success
     * -1：failed
     * 0： queueing
     */
    @AccessLimit(needLogin = true)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Long saleResult(Model model, SaleUser user,@RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return -1l;
        }
        model.addAttribute("user", user);
        Long saleResult = saleService.getSaleResult(user.getNickname(), goodsId);
        return saleResult;
    }
}
