package com.odontosys.odontosys_api.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Cita Odontológica.
 */
public class Appointment {

    private final UUID id;
    private final UUID patientId;
    private final UUID dentistId;
    private UUID slotId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private AppointmentStatus status;
    private String notes;
    private final Instant createdAt;
    private Instant updatedAt;

    private Appointment(UUID id, UUID patientId, UUID dentistId, UUID slotId,
                        LocalDate appointmentDate, LocalTime startTime, LocalTime endTime,
                        String reason, AppointmentStatus status, String notes,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.slotId = slotId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Appointment create(UUID patientId, UUID dentistId, UUID slotId,
                                     LocalDate appointmentDate, LocalTime startTime, LocalTime endTime,
                                     String reason) {
        Objects.requireNonNull(patientId, "El ID del paciente es obligatorio");
        Objects.requireNonNull(dentistId, "El ID del dentista es obligatorio");
        Objects.requireNonNull(slotId, "El ID del slot es obligatorio");
        Objects.requireNonNull(appointmentDate, "La fecha de la cita es obligatoria");
        Objects.requireNonNull(startTime, "La hora de inicio es obligatoria");
        Objects.requireNonNull(endTime, "La hora de fin es obligatoria");

        Instant now = Instant.now();
        return new Appointment(UUID.randomUUID(), patientId, dentistId, slotId,
                appointmentDate, startTime, endTime, reason, AppointmentStatus.SCHEDULED, null, now, now);
    }

    public static Appointment reconstitute(UUID id, UUID patientId, UUID dentistId, UUID slotId,
                                           LocalDate appointmentDate, LocalTime startTime, LocalTime endTime,
                                           String reason, AppointmentStatus status, String notes,
                                           Instant createdAt, Instant updatedAt) {
        return new Appointment(id, patientId, dentistId, slotId, appointmentDate, startTime,
                endTime, reason, status, notes, createdAt, updatedAt);
    }

    public void reschedule(UUID newSlotId, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede reagendar una cita cancelada");
        }
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("No se puede reagendar una cita completada");
        }

        this.slotId = newSlotId;
        this.appointmentDate = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.status = AppointmentStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    public void cancel(String cancelReason) {
        if (this.status == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("La cita ya fue cancelada previamente");
        }
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("No se puede cancelar una cita que ya fue completada");
        }

        this.status = AppointmentStatus.CANCELLED;
        if (cancelReason != null && !cancelReason.isBlank()) {
            this.notes = (this.notes == null ? "" : this.notes + " | ") + "Motivo cancelación: " + cancelReason;
        }
        this.updatedAt = Instant.now();
    }

    public void changeStatus(AppointmentStatus newStatus, String notes) {
        this.status = newStatus;
        if (notes != null && !notes.isBlank()) {
            this.notes = notes;
        }
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getDentistId() {
        return dentistId;
    }

    public UUID getSlotId() {
        return slotId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
