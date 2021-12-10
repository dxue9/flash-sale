package com.wangzehao.flashsale.vo;

import com.wangzehao.flashsale.domain.SaleUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVo {
    private int status = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private SaleUser user;
}

