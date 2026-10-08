package com.practice.warmup.domain.coupon.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 쿠폰 사용 응답. */
@Getter
@AllArgsConstructor
public class UseCouponResponse {
    private long originalAmount;
    private long discountAmount;
    private long finalAmount;
    private String couponType;
}
