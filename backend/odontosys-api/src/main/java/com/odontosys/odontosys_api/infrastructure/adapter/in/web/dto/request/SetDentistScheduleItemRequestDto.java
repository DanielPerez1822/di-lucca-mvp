package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import java.time.DayOfWeek;
import java.time.LocalTime;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SetDentistScheduleItemRequestDto(
        @NotNull(message = "El día de la semana es obligatorio")
        DayOfWeek dayOfWeek,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime,

        @Min(value = 1, message = "La duración de los slots debe ser al menos de 1 minuto")
        int slotDurationMinutes,

        boolean hasBreak,
        LocalTime breakStartTime,
        LocalTime breakEndTime
) {
}
