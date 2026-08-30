package com.odontosys.odontosys_api.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Franja / Slot individual de disponibilidad del dentista.
 */
public class AvailabilitySlot {

    private final UUID id;
    private final UUID dentistId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private SlotStatus status;
    private final Instant createdAt;

    private AvailabilitySlot(UUID id, UUID dentistId, LocalDate date, LocalTime startTime,
                             LocalTime endTime, SlotStatus status, Instant createdAt) {
        this.id = id;
        this.dentistId = dentistId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static AvailabilitySlot create(UUID dentistId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Objects.requireNonNull(dentistId, "El ID del dentista es obligatorio");
        Objects.requireNonNull(date, "La fecha es obligatoria");
        Objects.requireNonNull(startTime, "La hora de inicio es obligatoria");
        Objects.requireNonNull(endTime, "La hora de fin es obligatoria");

        return new AvailabilitySlot(UUID.randomUUID(), dentistId, date, startTime, endTime, SlotStatus.AVAILABLE, Instant.now());
    }

    public static AvailabilitySlot reconstitute(UUID id, UUID dentistId, LocalDate date, LocalTime startTime,
                                                LocalTime endTime, SlotStatus status, Instant createdAt) {
        return new AvailabilitySlot(id, dentistId, date, startTime, endTime, status, createdAt);
    }

    public void markAsBooked() {
        if (this.status == SlotStatus.BOOKED) {
            throw new IllegalStateException("El slot ya se encuentra reservado");
        }
        this.status = SlotStatus.BOOKED;
    }

    public void markAsAvailable() {
        this.status = SlotStatus.AVAILABLE;
    }

    public void block() {
        this.status = SlotStatus.BLOCKED;
    }

    public boolean isAvailable() {
        return this.status == SlotStatus.AVAILABLE;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDentistId() {
        return dentistId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
