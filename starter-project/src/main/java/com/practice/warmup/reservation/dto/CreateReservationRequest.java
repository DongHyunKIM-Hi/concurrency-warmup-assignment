package com.practice.warmup.reservation.dto;

/** 예약 생성 요청. {@code from}, {@code to}는 {@code HH:mm} 형식입니다. */
public record CreateReservationRequest(String roomId, String userId, String from, String to) {
}
