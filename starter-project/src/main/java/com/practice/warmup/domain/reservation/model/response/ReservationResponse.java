package com.practice.warmup.domain.reservation.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 예약 생성 응답. */
@Getter
@AllArgsConstructor
public class ReservationResponse {
    private String roomId;
    private String userId;
    private String from;
    private String to;
}
