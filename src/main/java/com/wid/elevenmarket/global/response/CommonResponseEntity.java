package com.wid.elevenmarket.global.response;

import org.springframework.http.HttpStatus;

public record CommonResponseEntity<T>(boolean success, T response, CustomError error) {

    public static <T> CommonResponseEntity<T> success(T response) {
        return new CommonResponseEntity<>(true, response, null);
    }

    public static <T> CommonResponseEntity<T> error(HttpStatus status, String message) {
        return new CommonResponseEntity<>(false, null, new CustomError(message, status));
    }
}
