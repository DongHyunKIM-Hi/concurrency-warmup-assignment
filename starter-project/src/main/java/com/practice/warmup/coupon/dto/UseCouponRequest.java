package com.practice.warmup.coupon.dto;

/** 쿠폰 사용 요청. {@code amount}는 1 이상이어야 합니다. */
public record UseCouponRequest(long amount) {
}
