package com.project.commerce.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String requestPath;
    private LocalDateTime timestamp;
}
