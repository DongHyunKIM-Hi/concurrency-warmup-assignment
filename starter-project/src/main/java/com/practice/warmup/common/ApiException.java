package com.practice.warmup.common;

import org.springframework.http.HttpStatus;

/** 계약에 정의된 에러 상황(400/404/409)을 표현하는 예외. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode code;

    public ApiException(HttpStatus status, ErrorCode code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus status() {
        return status;
    }

    public ErrorCode code() {
        return code;
    }
}
