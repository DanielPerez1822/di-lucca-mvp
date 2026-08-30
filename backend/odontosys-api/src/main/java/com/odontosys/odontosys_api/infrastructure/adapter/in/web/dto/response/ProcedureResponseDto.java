package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;

public record ProcedureResponseDto(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer duration,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProcedureResponseDto fromApplication(ProcedureResponse response) {
        return new ProcedureResponseDto(
                response.id(),
                response.name(),
                response.description(),
                response.price(),
                response.duration(),
                response.active(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
