package com.odontosys.odontosys_api.domain.model;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Horario semanal configurado para un dentista.
 */
public class DentistSchedule {

    private final UUID id;
    private final UUID dentistId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private boolean hasBreak;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private DentistSchedule(UUID id, UUID dentistId, DayOfWeek dayOfWeek, LocalTime startTime,
                            LocalTime endTime, int slotDurationMinutes, boolean hasBreak,
                            LocalTime breakStartTime, LocalTime breakEndTime, boolean active,
                            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.dentistId = dentistId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
        this.hasBreak = hasBreak;
        this.breakStartTime = breakStartTime;
        this.breakEndTime = breakEndTime;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DentistSchedule create(UUID dentistId, DayOfWeek dayOfWeek, LocalTime startTime,
                                         LocalTime endTime, int slotDurationMinutes, boolean hasBreak,
                                         LocalTime breakStartTime, LocalTime breakEndTime) {
        Objects.requireNonNull(dentistId, "El ID del dentista es obligatorio");
        Objects.requireNonNull(dayOfWeek, "El día de la semana es obligatorio");
        Objects.requireNonNull(startTime, "La hora de inicio es obligatoria");
        Objects.requireNonNull(endTime, "La hora de fin es obligatoria");

        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        if (hasBreak) {
            if (breakStartTime == null || breakEndTime == null) {
                throw new IllegalArgumentException("Las horas de inicio y fin del descanso son obligatorias");
            }
            if (breakEndTime.isBefore(breakStartTime) || breakEndTime.equals(breakStartTime)) {
                throw new IllegalArgumentException("La hora de fin del descanso debe ser posterior a su inicio");
            }
        }
        if (slotDurationMinutes <= 0 || slotDurationMinutes > 240) {
            throw new IllegalArgumentException("La duración del slot debe estar entre 1 y 240 minutos");
        }

        Instant now = Instant.now();
        return new DentistSchedule(UUID.randomUUID(), dentistId, dayOfWeek, startTime, endTime, slotDurationMinutes, hasBreak, breakStartTime, breakEndTime, true, now, now);
    }

    public static DentistSchedule reconstitute(UUID id, UUID dentistId, DayOfWeek dayOfWeek, LocalTime startTime,
                                               LocalTime endTime, int slotDurationMinutes, boolean hasBreak,
                                               LocalTime breakStartTime, LocalTime breakEndTime, boolean active,
                                               Instant createdAt, Instant updatedAt) {
        return new DentistSchedule(id, dentistId, dayOfWeek, startTime, endTime, slotDurationMinutes, hasBreak, breakStartTime, breakEndTime, active, createdAt, updatedAt);
    }

    public void update(LocalTime startTime, LocalTime endTime, int slotDurationMinutes) {
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        if (slotDurationMinutes <= 0 || slotDurationMinutes > 240) {
            throw new IllegalArgumentException("La duración del slot debe estar entre 1 y 240 minutos");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getDentistId() {
        return dentistId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public boolean isHasBreak() {
        return hasBreak;
    }

    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }

    public LocalTime getBreakEndTime() {
        return breakEndTime;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
