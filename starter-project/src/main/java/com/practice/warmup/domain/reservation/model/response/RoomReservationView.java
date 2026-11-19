package com.practice.warmup.domain.reservation.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 방별 예약 조회 응답의 항목 하나. */
@Getter
@AllArgsConstructor
public class RoomReservationView {
    private String userId;
    private String from;
    private String to;
}
