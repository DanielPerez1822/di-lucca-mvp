package com.odontosys.odontosys_api.application.schedule.command;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record SetDentistScheduleCommand(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        int slotDurationMinutes,
        boolean hasBreak,
        LocalTime breakStartTime,
        LocalTime breakEndTime
) {
    public SetDentistScheduleCommand(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int slotDurationMinutes) {
        this(dayOfWeek, startTime, endTime, slotDurationMinutes, false, null, null);
    }
}
