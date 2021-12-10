package com.wangzehao.flashsale.dao;

import com.wangzehao.flashsale.domain.SaleUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SaleUserDao {
    @Select("select * from sale_user where nickname = #{nickname}")
    public SaleUser getByNickname(@Param("nickname") String nickname) ;

    @Select("select * from sale_user where id = #{id}")
    public SaleUser getById(@Param("id") long id) ;

    @Update("update sale_user set password = #{password} where id = #{id}")
    public void update(SaleUser toBeUpdate);

    @Insert("insert into sale_user (id , nickname ,password , salt ,head,register_date,last_login_date) value (#{id},#{nickname},#{password},#{salt},#{head},#{registerDate},#{lastLoginDate}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void insert(SaleUser saleUser);
}
