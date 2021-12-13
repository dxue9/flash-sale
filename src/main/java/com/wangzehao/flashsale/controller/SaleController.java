package com.wangzehao.flashsale.controller;

import com.wangzehao.flashsale.access.AccessLimit;
import com.wangzehao.flashsale.common.CustomAbstractResponse;
import com.wangzehao.flashsale.common.CustomResponse;
import com.wangzehao.flashsale.common.enums.CustomResponseStatus;
import com.wangzehao.flashsale.domain.OrderInfo;
import com.wangzehao.flashsale.domain.SaleOrder;
import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.redis.RedisService;
import com.wangzehao.flashsale.redis.prefix.GoodsKey;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController{

    @Autowired
    private RedisService redisService;

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
    public CustomResponse<String> getSalePath(HttpServletRequest request, SaleUser user,
                              @RequestParam("goodsId") long goodsId){
        CustomResponse<String> result = CustomResponse.build();
        if(user == null){
            result.withError(CustomResponseStatus.REGISTRATION_ERROR.getCode(), CustomResponseStatus.REGISTRATION_ERROR.getMessage());
            return result;
        }

        String path = saleService.createBuyPath(user, goodsId);
        result.setData(path);
        return result;
    }

    @AccessLimit(needLogin = true)
    @RequestMapping(value = "/{path}/do_sale", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse<String> placeOrder(Model model, SaleUser user, @PathVariable("path") String path,
                             @RequestParam("goodsId") long goodsId){
        CustomResponse<String> result = CustomResponse.build();
        if(user == null) {
            result.withError(CustomResponseStatus.USER_NOT_FOUND.getCode(), CustomResponseStatus.USER_NOT_FOUND.getMessage());
            return result;
        }
        // check if have existing order
        SaleOrder order = orderService.getSaleOrderByUserIdGoodsId(user.getNickname(), goodsId);
        if (order != null) {
            result.withError(CustomResponseStatus.EXISTING_ORDER.getCode(), CustomResponseStatus.EXISTING_ORDER.getMessage());
            return result;
        }

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0) {
            goodsService.setOriginalStockByGoodsId(goodsId, 0);
            result.withError(CustomResponseStatus.OUT_OF_STOCK.getCode(), CustomResponseStatus.OUT_OF_STOCK.getMessage());
            return result;
        }
        OrderInfo orderInfo = saleService.sale(user, goodsVo);
        if(orderInfo == null) {
            result.withError(CustomResponseStatus.PLACE_ORDER_ERROR.getCode(), CustomResponseStatus.PLACE_ORDER_ERROR.getMessage());
            return result;
        }
        return result;
    }

    /**
     * orderId：success
     * -1：failed
     * 0： queueing
     */
    @AccessLimit(needLogin = true)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public CustomAbstractResponse saleResult(Model model, SaleUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return CustomResponse.build().withError("user is null");
        }
        model.addAttribute("user", user);
        Long saleResult = saleService.getSaleResult(user.getNickname(), goodsId);
        Integer currentStock = goodsService.getStockByGoodsId(goodsId);
        Integer originalStock = redisService.get(GoodsKey.getGoodsStock, String.valueOf(goodsId), Integer.class);
        CustomResponse<List<Long>> response = CustomResponse.build();
        ArrayList<Long> stocks = new ArrayList<Long>();
        stocks.add((long)originalStock);
        stocks.add((long)currentStock);
        stocks.add(saleResult);
        response.setData(stocks);
        return response;
    }
}
