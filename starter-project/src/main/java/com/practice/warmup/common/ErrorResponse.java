package com.practice.warmup.common;

/** 에러 응답 공통 형식. 계약이므로 필드를 바꾸지 않습니다. */
public record ErrorResponse(String code, String message) {

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message);
    }
}
