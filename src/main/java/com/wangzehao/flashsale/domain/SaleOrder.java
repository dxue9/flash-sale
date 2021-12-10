package com.wangzehao.flashsale.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrder {
    private Long id;
    private String userId;
    private Long orderId;
    private Long goodsId;
}
