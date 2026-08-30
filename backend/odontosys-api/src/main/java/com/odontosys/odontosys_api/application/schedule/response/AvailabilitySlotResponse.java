package com.odontosys.odontosys_api.application.schedule.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.SlotStatus;

public record AvailabilitySlotResponse(
        UUID id,
        UUID dentistId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        SlotStatus status,
        Instant createdAt
) {
    public static AvailabilitySlotResponse fromDomain(AvailabilitySlot slot) {
        return new AvailabilitySlotResponse(
                slot.getId(),
                slot.getDentistId(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getStatus(),
                slot.getCreatedAt()
        );
    }
}
