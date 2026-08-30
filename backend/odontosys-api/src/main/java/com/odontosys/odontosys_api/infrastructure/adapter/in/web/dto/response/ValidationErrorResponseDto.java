package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponseDto(
        int status,
        String error,
        List<FieldErrorDto> errors,
        Instant timestamp
) {
    public ValidationErrorResponseDto(int status, String error, List<FieldErrorDto> errors) {
        this(status, error, errors, Instant.now());
    }

    public record FieldErrorDto(String field, String message) {
    }
}
