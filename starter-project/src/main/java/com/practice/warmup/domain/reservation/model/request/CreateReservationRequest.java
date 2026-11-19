package com.practice.warmup.domain.reservation.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 예약 생성 요청. {@code from}, {@code to}는 {@code HH:mm} 형식입니다. */
@Getter
@NoArgsConstructor
public class CreateReservationRequest {
    private String roomId;
    private String userId;
    private String from;
    private String to;
}
