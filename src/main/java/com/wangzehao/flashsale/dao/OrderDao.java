package com.wangzehao.flashsale.dao;

import com.wangzehao.flashsale.domain.OrderInfo;
import com.wangzehao.flashsale.domain.SaleOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    @Select("select * from sale_order where user_id=#{userId} and goods_id=#{goodsId}")
    public SaleOrder getSaleOrderByUserIdGoodsId(@Param("userId") String userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into sale_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertSaleOrder(SaleOrder saleOrder);

    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId")long orderId);

    @Select("select * from order_info where status=#{status} and create_Date<=#{createDate}")
    public List<OrderInfo> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("createDate") String createDate);

    @Select("update order_info set status=0 where id=#{id}")
    public int closeOrderByOrderInfo(@Param("id") long id);
}
