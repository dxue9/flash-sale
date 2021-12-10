package com.wangzehao.flashsale.vo;

import com.wangzehao.flashsale.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    private Double salePrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
