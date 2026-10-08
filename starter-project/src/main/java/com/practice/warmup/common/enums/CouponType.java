package com.practice.warmup.common.enums;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 쿠폰 종류. 이 4개뿐입니다 (추가·변경 금지).
 * 할인액 계산 규칙(정률은 원 단위 미만 절사, 정액은 {@code min(정액, 금액)})은
 * 이미 구현되어 있으니 {@link #discountFor(long)}를 그대로 호출해서 쓰면 됩니다.
 */
public enum CouponType {
    PERCENT_15(0.15, 0),
    PERCENT_5(0.05, 0),
    WON_3000(0, 3000),
    WON_800(0, 800);

    private final double rate;
    private final long fixedAmount;

    CouponType(double rate, long fixedAmount) {
        this.rate = rate;
        this.fixedAmount = fixedAmount;
    }

    /** 이 쿠폰을 주어진 금액에 적용했을 때의 할인액을 계산합니다. */
    public long discountFor(long amount) {
        if (rate > 0) {
            return (long) Math.floor(amount * rate);
        }
        return Math.min(fixedAmount, amount);
    }
}
