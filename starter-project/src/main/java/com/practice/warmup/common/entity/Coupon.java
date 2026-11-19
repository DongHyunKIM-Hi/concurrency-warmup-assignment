package com.practice.warmup.common.entity;

import com.practice.warmup.common.enums.CouponType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/** 이 클래스는 수정하지 않습니다. 쿠폰 1장을 나타냅니다. */
@Getter
public class Coupon {

    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private final long id;
    private final CouponType type;

    private Coupon(long id, CouponType type) {
        this.id = id;
        this.type = type;
    }

    /** 새 쿠폰을 발급합니다. ID는 서버 전체에서 순서대로 증가합니다. */
    public static Coupon issue(CouponType type) {
        return new Coupon(SEQUENCE.incrementAndGet(), type);
    }
}
