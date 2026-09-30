package com.practice.warmup.coupon.dto;

/** 쿠폰 발급 요청. {@code type}은 {@code PERCENT_15}, {@code PERCENT_5}, {@code WON_3000}, {@code WON_800} 중 하나여야 합니다. */
public record IssueCouponRequest(String type) {
}
