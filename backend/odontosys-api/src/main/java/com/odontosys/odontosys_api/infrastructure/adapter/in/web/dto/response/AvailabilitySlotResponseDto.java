package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.AvailabilitySlotResponse;
import com.odontosys.odontosys_api.domain.model.SlotStatus;

public record AvailabilitySlotResponseDto(
        UUID id,
        UUID dentistId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        SlotStatus status,
        Instant createdAt
) {
    public static AvailabilitySlotResponseDto fromApplication(AvailabilitySlotResponse response) {
        return new AvailabilitySlotResponseDto(
                response.id(),
                response.dentistId(),
                response.date(),
                response.startTime(),
                response.endTime(),
                response.status(),
                response.createdAt()
        );
    }
}
