package com.practice.warmup.domain.reservation.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 사용자별 예약 조회 응답의 항목 하나. */
@Getter
@AllArgsConstructor
public class UserReservationView {
    private String roomId;
    private String from;
    private String to;
}
