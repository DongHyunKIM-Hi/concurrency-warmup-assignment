package com.practice.warmup.common.exception;

import com.practice.warmup.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** 에러 응답 공통 형식. 계약이므로 필드를 바꾸지 않습니다. */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code.name(), message);
    }
}
