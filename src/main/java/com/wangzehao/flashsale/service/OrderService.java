package com.wangzehao.flashsale.service;

import com.wangzehao.flashsale.dao.OrderDao;
import com.wangzehao.flashsale.domain.OrderInfo;
import com.wangzehao.flashsale.domain.SaleOrder;
import com.wangzehao.flashsale.domain.SaleUser;
import com.wangzehao.flashsale.vo.GoodsVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public SaleOrder getSaleOrderByUserIdGoodsId(String userId, long goodsId) {
        return orderDao.getSaleOrderByUserIdGoodsId(userId, goodsId);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(SaleUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSalePrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getNickname()));
        orderDao.insert(orderInfo);

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setGoodsId(goods.getId());
        saleOrder.setOrderId(orderInfo.getId());
        saleOrder.setUserId(user.getNickname());
        orderDao.insertSaleOrder(saleOrder);
        return orderInfo;
    }
}
