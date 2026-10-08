package com.practice.warmup.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

/** 이 클래스는 수정하지 않습니다. 예약 1건을 나타냅니다 (방 ID는 저장소가 키로 관리하므로 포함하지 않습니다). */
@Getter
@AllArgsConstructor
public class Reservation {

    private final String userId;
    private final LocalTime from;
    private final LocalTime to;

    /** 이 예약이 주어진 구간과 겹치는지 확인합니다. 맞닿기만 한 경우는 겹치는 것이 아닙니다. */
    public boolean overlaps(LocalTime otherFrom, LocalTime otherTo) {
        return from.isBefore(otherTo) && otherFrom.isBefore(to);
    }
}
