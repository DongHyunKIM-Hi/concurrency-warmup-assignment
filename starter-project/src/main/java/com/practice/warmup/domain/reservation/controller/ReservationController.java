package com.practice.warmup.domain.reservation.controller;

import com.practice.warmup.domain.reservation.model.request.CreateReservationRequest;
import com.practice.warmup.domain.reservation.model.response.ReservationResponse;
import com.practice.warmup.domain.reservation.model.response.RoomReservationView;
import com.practice.warmup.domain.reservation.model.response.UserReservationView;
import com.practice.warmup.domain.reservation.repository.ReservationStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 회의실 예약 생성·조회 API. 경로와 요청·응답 형식(계약)은 바꾸지 않습니다.
 *
 * 생성과 사용자별 조회는 아직 구현되어 있지 않습니다 ({@code TODO}). 가이드 6장을 읽고
 * 구현하세요. 필요한 만큼 서비스·도메인 클래스를 자유롭게 추가해도 됩니다.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationStore reservationStore;

    public ReservationController(ReservationStore reservationStore) {
        this.reservationStore = reservationStore;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
        // TODO: 예약 생성을 구현하세요.
        //  - from, to는 "HH:mm" 형식이어야 하고 from < to여야 합니다. 아니면 400 INVALID_REQUEST
        //  - 같은 방에 겹치는 시간의 예약이 이미 있으면 409 TIME_CONFLICT
        //    (겹침 판정은 Reservation.overlaps(from, to)를 쓰면 됩니다)
        //  - 성공하면 201, 저장한 예약을 그대로 돌려주세요.
        //  - 같은 방에 동시에 여러 건이 겹치는 시간으로 요청해도, 정확히 1건만 성공해야 합니다.
        //    (가이드 7장 시나리오 S8 참고). 다른 방은 서로 막히면 안 됩니다 (S10).
        throw new UnsupportedOperationException("TODO: 예약 생성을 구현하세요");
    }

    @GetMapping("/rooms/{roomId}")
    public List<RoomReservationView> byRoom(@PathVariable String roomId) {
        return reservationStore.findByRoomId(roomId).stream()
                .map(r -> new RoomReservationView(r.getUserId(), r.getFrom().toString(), r.getTo().toString()))
                .toList();
    }

    @GetMapping("/users/{userId}")
    public List<UserReservationView> byUser(@PathVariable String userId) {
        // TODO: 사용자별 예약 조회를 구현하세요.
        //  - reservationStore.findAll()로 모든 방의 예약을 훑어, userId가 일치하는 것만 모으세요.
        throw new UnsupportedOperationException("TODO: 사용자별 예약 조회를 구현하세요");
    }
}
