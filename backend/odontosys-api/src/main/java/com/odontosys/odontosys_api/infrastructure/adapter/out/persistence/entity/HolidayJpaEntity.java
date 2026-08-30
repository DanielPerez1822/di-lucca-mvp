package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "holidays")
public class HolidayJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String reason;

    @Column(name = "is_global", nullable = false)
    private boolean global = true;

    @Column(name = "dentist_id")
    private UUID dentistId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public HolidayJpaEntity() {
    }

    public HolidayJpaEntity(UUID id, LocalDate date, String reason, boolean global, UUID dentistId) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.global = global;
        this.dentistId = dentistId;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public UUID getDentistId() {
        return dentistId;
    }

    public void setDentistId(UUID dentistId) {
        this.dentistId = dentistId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
