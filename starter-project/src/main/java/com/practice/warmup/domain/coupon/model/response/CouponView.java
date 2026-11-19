package com.practice.warmup.domain.coupon.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 보유 쿠폰 조회 응답의 항목 하나. */
@Getter
@AllArgsConstructor
public class CouponView {
    private long id;
    private String type;
}
