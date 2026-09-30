package com.practice.warmup.reservation.dto;

/** 방별 예약 조회 응답의 항목 하나. */
public record RoomReservationView(String userId, String from, String to) {
}
