package com.practice.warmup.domain.coupon.repository;

import com.practice.warmup.common.entity.Coupon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 사용자별 보유 쿠폰을 보관하는 인메모리 저장소입니다. 내부 자료구조는 평범한
 * {@link HashMap}/{@link ArrayList}로, 스레드 안전하지 않습니다. 여러 요청이 동시에
 * 이 저장소를 건드리면 여러분의 서비스 코드가 안전하게 막아 주어야 합니다.
 *
 * {@link #findByUserId(String)}는 저장소 내부의 리스트를 <b>그대로</b> 돌려줍니다
 * (호출할 때마다 복사본을 만들어 주지 않습니다). 이 리스트를 들고 있다가 나중에 쓰면
 * 그 사이 다른 요청이 같은 리스트를 바꿀 수 있다는 점을 생각하세요.
 *
 * 모든 메서드는 실제 원격 저장소를 흉내 내기 위해 호출마다 50~100ms의 지연이 있습니다.
 */
@Component
public class CouponStore {

    private final Map<String, List<Coupon>> coupons = new HashMap<>();

    /** 사용자에게 쿠폰 1장을 추가합니다. */
    public void save(String userId, Coupon coupon) {
        delay();
        coupons.computeIfAbsent(userId, k -> new ArrayList<>()).add(coupon);
    }

    /**
     * 사용자의 보유 쿠폰 목록을 돌려줍니다. 없으면 빈 리스트입니다.
     * <b>주의</b>: 사용자가 쿠폰을 1장이라도 가진 적이 있다면, 이 메서드는 저장소
     * 내부의 실제 리스트 참조를 그대로 돌려줍니다 (방어적 복사 없음).
     */
    public List<Coupon> findByUserId(String userId) {
        delay();
        return coupons.getOrDefault(userId, Collections.emptyList());
    }

    /** 사용자의 쿠폰 중 주어진 ID를 가진 것을 제거합니다. 없으면 아무 일도 하지 않습니다. */
    public void delete(String userId, long couponId) {
        delay();
        List<Coupon> list = coupons.get(userId);
        if (list != null) {
            list.removeIf(c -> c.getId() == couponId);
        }
    }

    /** 테스트에서만 사용합니다. 저장소를 완전히 비웁니다. */
    public void resetForTest() {
        coupons.clear();
    }

    private void delay() {
        try {
            Thread.sleep(50 + ThreadLocalRandom.current().nextLong(51));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
