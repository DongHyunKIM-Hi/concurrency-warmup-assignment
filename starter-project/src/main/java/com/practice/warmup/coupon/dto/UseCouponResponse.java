package com.practice.warmup.coupon.dto;

/** 쿠폰 사용 응답. */
public record UseCouponResponse(long originalAmount, long discountAmount, long finalAmount, String couponType) {
}
