package com.practice.warmup.reservation.dto;

/** 사용자별 예약 조회 응답의 항목 하나. */
public record UserReservationView(String roomId, String from, String to) {
}
