package com.practice.warmup.domain.reservation.repository;

import com.practice.warmup.common.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 회의실 예약을 <b>방 단위로만</b> 보관하는 인메모리 저장소입니다. 사용자별 인덱스는
 * 따로 없으므로, 사용자별 조회가 필요하면 {@link #findAll()}로 모든 방을 훑어야 합니다.
 *
 * 쿠폰 저장소와 달리 호출 지연은 없습니다. 지연이 없어도 "겹침을 확인하고 저장하는"
 * 두 단계 사이에는 여전히 다른 요청이 끼어들 수 있다는 점을 눈여겨보세요.
 */
@Component
public class ReservationStore {

    private final Map<String, List<Reservation>> byRoom = new HashMap<>();

    /** 방에 예약 1건을 추가합니다. */
    public void save(String roomId, Reservation reservation) {
        byRoom.computeIfAbsent(roomId, k -> new ArrayList<>()).add(reservation);
    }

    /** 그 방의 예약 목록을 돌려줍니다. 없으면 빈 리스트입니다. */
    public List<Reservation> findByRoomId(String roomId) {
        return byRoom.getOrDefault(roomId, Collections.emptyList());
    }

    /** 모든 방의 예약을 {@code 방 ID → 예약 목록} 형태로 돌려줍니다. 사용자별 조회에 활용하세요. */
    public Map<String, List<Reservation>> findAll() {
        return Collections.unmodifiableMap(byRoom);
    }

    /** 테스트에서만 사용합니다. 저장소를 완전히 비웁니다. */
    public void resetForTest() {
        byRoom.clear();
    }
}
