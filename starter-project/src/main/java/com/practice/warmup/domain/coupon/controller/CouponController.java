package com.practice.warmup.domain.coupon.controller;

import com.practice.warmup.domain.coupon.model.response.CouponView;
import com.practice.warmup.domain.coupon.model.request.IssueCouponRequest;
import com.practice.warmup.domain.coupon.model.request.UseCouponRequest;
import com.practice.warmup.domain.coupon.model.response.UseCouponResponse;
import com.practice.warmup.domain.coupon.repository.CouponStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 쿠폰 발급·조회·사용 API. 경로와 요청·응답 형식(계약)은 바꾸지 않습니다.
 *
 * 발급과 사용은 아직 구현되어 있지 않습니다 ({@code TODO}). 가이드 5장을 읽고 구현하세요.
 * 필요한 만큼 서비스·도메인 클래스를 자유롭게 추가해도 됩니다.
 */
@RestController
@RequestMapping("/api/users/{userId}/coupons")
public class CouponController {

    private final CouponStore couponStore;

    public CouponController(CouponStore couponStore) {
        this.couponStore = couponStore;
    }

    @PostMapping
    public ResponseEntity<Void> issue(@PathVariable String userId, @RequestBody IssueCouponRequest request) {
        // TODO: 쿠폰 발급을 구현하세요.
        //  - type이 4가지(PERCENT_15, PERCENT_5, WON_3000, WON_800) 중 하나가 아니면 400 INVALID_REQUEST
        //  - 성공하면 204 No Content (본문 없음)
        //  - Coupon.issue(type)으로 쿠폰을 만들고 couponStore.save(userId, coupon)으로 저장하세요.
        throw new UnsupportedOperationException("TODO: 쿠폰 발급을 구현하세요");
    }

    @GetMapping
    public List<CouponView> list(@PathVariable String userId) {
        return couponStore.findByUserId(userId).stream()
                .map(c -> new CouponView(c.getId(), c.getType().name()))
                .toList();
    }

    @PostMapping("/use")
    public UseCouponResponse use(@PathVariable String userId, @RequestBody UseCouponRequest request) {
        // TODO: 쿠폰 사용을 구현하세요.
        //  - amount가 0 이하면 400 INVALID_REQUEST
        //  - 보유 쿠폰 중 이 금액에 적용했을 때 할인액이 가장 큰 쿠폰 1장을 고르세요
        //    (할인액 계산은 CouponType.discountFor(amount)를 쓰면 됩니다).
        //  - 쓸 수 있는 쿠폰이 없으면 404 NO_COUPON_AVAILABLE
        //  - 고른 쿠폰은 사라져야 합니다 (couponStore.delete).
        //  - 같은 사용자에게 동시에 여러 사용 요청이 와도, 쿠폰 한 장이 두 번 쓰이면 안 됩니다.
        //    (가이드 7장 시나리오 S2, S3 참고)
        throw new UnsupportedOperationException("TODO: 쿠폰 사용을 구현하세요");
    }
}
