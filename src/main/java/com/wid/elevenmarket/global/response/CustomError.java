package com.wid.elevenmarket.global.response;

import org.springframework.http.HttpStatus;

public record CustomError(String message, HttpStatus status) {}