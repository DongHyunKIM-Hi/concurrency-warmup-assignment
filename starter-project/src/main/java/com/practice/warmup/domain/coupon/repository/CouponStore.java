package com.practice.warmup.domain.coupon.repository;

import com.practice.warmup.common.entity.Coupon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 사용자별 보유 쿠폰을 메모리에 보관하는 저장소입니다. 내부 맵은 {@link ConcurrentHashMap}이라
 * 서로 다른 사용자가 동시에 저장소를 건드려도 메서드 호출 하나하나는 안전합니다.
 * 단, 사용자마다의 쿠폰 목록은 일반 리스트라 같은 사용자의 목록을 동시에 바꾸면 안전하지 않습니다.
 *
 * 여러 호출을 묶는 일은 직접 해야 합니다. 예를 들어 할인액이 가장 큰 쿠폰 찾기와 그 쿠폰 지우기
 * 사이에 같은 사용자의 다른 요청이 끼어들 수 있습니다. 이 부분은 직접 작성하는 코드(컨트롤러나
 * 새로 만든 서비스 클래스)에서 막아야 합니다.
 *
 * {@link #findByUserId(String)}는 저장소 안의 리스트를 복사하지 않고 <b>그대로</b> 돌려줍니다.
 * 이 리스트를 변수에 담아 두었다가 나중에 쓰면, 그 사이 다른 요청이 같은 리스트를 바꿀 수 있습니다.
 *
 * 모든 메서드는 실제 원격 저장소를 흉내 내기 위해 호출마다 50~100ms의 지연이 있습니다.
 */
@Component
public class CouponStore {

    private final Map<String, List<Coupon>> coupons = new ConcurrentHashMap<>();

    /** 사용자에게 쿠폰 1장을 추가합니다. */
    public void save(String userId, Coupon coupon) {
        delay();
        coupons.computeIfAbsent(userId, k -> new ArrayList<>()).add(coupon);
    }

    /**
     * 사용자의 보유 쿠폰 목록을 돌려줍니다. 없으면 빈 리스트입니다.
     * <b>주의</b>: 사용자가 쿠폰을 1장이라도 가진 적이 있다면, 이 메서드는 저장소
     * 내부의 실제 리스트를 복사하지 않고 그대로 돌려줍니다.
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
