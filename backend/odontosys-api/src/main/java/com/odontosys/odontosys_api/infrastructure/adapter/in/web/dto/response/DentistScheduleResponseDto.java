package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;

public record DentistScheduleResponseDto(
        UUID id,
        UUID dentistId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        int slotDurationMinutes,
        boolean hasBreak,
        LocalTime breakStartTime,
        LocalTime breakEndTime,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static DentistScheduleResponseDto fromApplication(DentistScheduleResponse response) {
        return new DentistScheduleResponseDto(
                response.id(),
                response.dentistId(),
                response.dayOfWeek(),
                response.startTime(),
                response.endTime(),
                response.slotDurationMinutes(),
                response.hasBreak(),
                response.breakStartTime(),
                response.breakEndTime(),
                response.active(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
