package com.odontosys.odontosys_api.application.schedule.response;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;

public record DentistScheduleResponse(
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
    public static DentistScheduleResponse fromDomain(DentistSchedule schedule) {
        return new DentistScheduleResponse(
                schedule.getId(),
                schedule.getDentistId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getSlotDurationMinutes(),
                schedule.isHasBreak(),
                schedule.getBreakStartTime(),
                schedule.getBreakEndTime(),
                schedule.isActive(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
