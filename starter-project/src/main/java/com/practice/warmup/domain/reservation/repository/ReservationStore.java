package com.practice.warmup.domain.reservation.repository;

import com.practice.warmup.common.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 이 클래스는 수정하지 않습니다.
 *
 * 회의실 예약을 <b>방 단위로만</b> 보관하는 인메모리 저장소입니다. 사용자별 인덱스는
 * 따로 없으므로, 사용자별 조회가 필요하면 {@link #findAll()}로 모든 방을 훑어야 합니다.
 * 내부 맵은 {@link ConcurrentHashMap}이라 서로 다른 방을 동시에 건드려도 호출 하나하나는
 * 안전합니다.
 *
 * 다만 "겹침을 확인하고 저장하는" 두 단계처럼 <b>여러 번의 호출을 하나의 흐름으로 묶어야
 * 하는 경우</b>, 그 사이에 같은 방의 다른 요청이 끼어들 수 있다는 점은 여전합니다. 그 부분은
 * 여러분이 막아야 합니다.
 */
@Component
public class ReservationStore {

    private final Map<String, List<Reservation>> byRoom = new ConcurrentHashMap<>();

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
