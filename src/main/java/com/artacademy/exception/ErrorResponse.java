package com.artacademy.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
                int statusCode,
                String message,
                LocalDateTime timestamp,
                String path,
                Map<String, String> validationErrors) {
}