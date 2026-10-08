package com.practice.warmup.domain.coupon.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 쿠폰 발급 요청. {@code type}은 {@code PERCENT_15}, {@code PERCENT_5}, {@code WON_3000}, {@code WON_800} 중 하나여야 합니다. */
@Getter
@NoArgsConstructor
public class IssueCouponRequest {
    private String type;
}
