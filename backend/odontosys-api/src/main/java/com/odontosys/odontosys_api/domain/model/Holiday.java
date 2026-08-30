package com.odontosys.odontosys_api.domain.model;

import java.time.LocalDate;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Holiday {

    private final UUID id;
    private final LocalDate date;
    private final String reason;
    private final boolean global;
    private final UUID dentistId;
    private final Instant createdAt;

    private Holiday(UUID id, LocalDate date, String reason, boolean global, UUID dentistId, Instant createdAt) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.global = global;
        this.dentistId = dentistId;
        this.createdAt = createdAt;
    }

    public static Holiday create(LocalDate date, String reason, boolean global, UUID dentistId) {
        Objects.requireNonNull(date, "La fecha del festivo es obligatoria");
        Objects.requireNonNull(reason, "El motivo es obligatorio");
        return new Holiday(UUID.randomUUID(), date, reason, global, dentistId, Instant.now());
    }

    public static Holiday reconstitute(UUID id, LocalDate date, String reason, boolean global, UUID dentistId, Instant createdAt) {
        return new Holiday(id, date, reason, global, dentistId, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public boolean isGlobal() {
        return global;
    }

    public UUID getDentistId() {
        return dentistId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
