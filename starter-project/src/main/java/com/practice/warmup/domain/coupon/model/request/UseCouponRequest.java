package com.practice.warmup.domain.coupon.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 쿠폰 사용 요청. {@code amount}는 1 이상이어야 합니다. */
@Getter
@NoArgsConstructor
public class UseCouponRequest {
    private long amount;
}
