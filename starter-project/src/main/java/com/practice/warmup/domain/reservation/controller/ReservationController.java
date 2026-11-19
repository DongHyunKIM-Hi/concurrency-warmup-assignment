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
 * 회의실 예약 생성, 조회 API. 경로와 요청, 응답 형식은 바꾸지 않습니다.
 *
 * 생성과 사용자별 조회는 아직 구현되어 있지 않습니다 ({@code TODO}). README 6장을 읽고 구현하세요.
 *
 * 구현 코드를 이 컨트롤러 안에 직접 쓰지 말고 <b>서비스 클래스로 분리</b>하세요.
 * 컨트롤러는 요청을 받아 서비스에 넘기는 역할만 합니다.
 * 코드 분리는 평가 항목입니다 (README 9장 B 영역, 8점).
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
        //  - 같은 회의실에 겹치는 시간의 예약이 이미 있으면 409 TIME_CONFLICT
        //    (겹침 판정은 Reservation.overlaps(from, to)를 쓰면 됩니다)
        //  - 성공하면 201과 함께 요청한 roomId, userId, from, to를 ReservationResponse로 돌려주세요.
        //  - 같은 회의실에 동시에 여러 건이 겹치는 시간으로 요청해도, 정확히 1건만 성공해야 합니다.
        //    (README 7장 시나리오 S8 참고). 다른 회의실의 요청은 서로 기다리게 하면 안 됩니다 (S10).
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
        //  - reservationStore.findAll()로 모든 회의실의 예약을 훑어, userId가 일치하는 것만 모으세요.
        throw new UnsupportedOperationException("TODO: 사용자별 예약 조회를 구현하세요");
    }
}
