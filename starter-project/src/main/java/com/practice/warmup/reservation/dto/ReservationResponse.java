package com.practice.warmup.reservation.dto;

/** 예약 생성 응답. */
public record ReservationResponse(String roomId, String userId, String from, String to) {
}
