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
 * 회의실 예약을 <b>회의실 단위로만</b> 메모리에 보관하는 저장소입니다. 사용자별로 따로 모아 둔
 * 목록은 없으므로, 사용자별 조회가 필요하면 {@link #findAll()}로 모든 회의실을 훑어야 합니다.
 * 내부 맵은 {@link ConcurrentHashMap}이라 서로 다른 회의실을 동시에 건드려도 메서드 호출 하나하나는
 * 안전합니다. 단, 회의실마다의 예약 목록은 일반 리스트라 같은 회의실을 동시에 바꾸면 안전하지 않습니다.
 *
 * 겹침 확인과 저장처럼 여러 호출을 묶는 일은 직접 해야 합니다. 두 호출 사이에 같은 회의실의
 * 다른 요청이 끼어들 수 있으므로, 직접 작성하는 코드(컨트롤러나 새로 만든 서비스 클래스)에서 막아야 합니다.
 */
@Component
public class ReservationStore {

    private final Map<String, List<Reservation>> byRoom = new ConcurrentHashMap<>();

    /** 회의실에 예약 1건을 추가합니다. */
    public void save(String roomId, Reservation reservation) {
        byRoom.computeIfAbsent(roomId, k -> new ArrayList<>()).add(reservation);
    }

    /** 그 방의 예약 목록을 돌려줍니다. 없으면 빈 리스트입니다. */
    public List<Reservation> findByRoomId(String roomId) {
        return byRoom.getOrDefault(roomId, Collections.emptyList());
    }

    /** 모든 방의 예약을 {@code 회의실 ID → 예약 목록} 형태로 돌려줍니다. 사용자별 조회에 활용하세요. */
    public Map<String, List<Reservation>> findAll() {
        return Collections.unmodifiableMap(byRoom);
    }

    /** 테스트에서만 사용합니다. 저장소를 완전히 비웁니다. */
    public void resetForTest() {
        byRoom.clear();
    }
}
