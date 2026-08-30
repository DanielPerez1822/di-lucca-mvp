package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Historial / Registro de Atención Clínica Médica.
 */
public class MedicalRecord {

    private final UUID id;
    private final UUID patientId;
    private final UUID dentistId;
    private UUID appointmentId;
    private String diagnosis;
    private String notes;
    private final List<MedicalRecordProcedureItem> items;
    private BigDecimal totalAmount;
    private final Instant createdAt;
    private Instant updatedAt;

    private MedicalRecord(UUID id, UUID patientId, UUID dentistId, UUID appointmentId,
                          String diagnosis, String notes, List<MedicalRecordProcedureItem> items,
                          BigDecimal totalAmount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.totalAmount = totalAmount != null ? totalAmount : calculateTotal(this.items);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MedicalRecord create(UUID patientId, UUID dentistId, UUID appointmentId,
                                      String diagnosis, String notes, List<MedicalRecordProcedureItem> items) {
        Objects.requireNonNull(patientId, "El ID del paciente es obligatorio");
        Objects.requireNonNull(dentistId, "El ID del dentista es obligatorio");
        Objects.requireNonNull(diagnosis, "El diagnóstico es obligatorio");

        if (diagnosis.isBlank()) {
            throw new IllegalArgumentException("El diagnóstico no puede estar vacío");
        }

        Instant now = Instant.now();
        List<MedicalRecordProcedureItem> safeItems = items != null ? new ArrayList<>(items) : new ArrayList<>();
        BigDecimal total = calculateTotal(safeItems);

        return new MedicalRecord(UUID.randomUUID(), patientId, dentistId, appointmentId, diagnosis, notes, safeItems, total, now, now);
    }

    public static MedicalRecord reconstitute(UUID id, UUID patientId, UUID dentistId, UUID appointmentId,
                                            String diagnosis, String notes, List<MedicalRecordProcedureItem> items,
                                            BigDecimal totalAmount, Instant createdAt, Instant updatedAt) {
        return new MedicalRecord(id, patientId, dentistId, appointmentId, diagnosis, notes, items, totalAmount, createdAt, updatedAt);
    }

    public void update(String diagnosis, String notes, List<MedicalRecordProcedureItem> newItems) {
        if (diagnosis != null && !diagnosis.isBlank()) {
            this.diagnosis = diagnosis;
        }
        this.notes = notes;
        if (newItems != null) {
            this.items.clear();
            this.items.addAll(newItems);
            this.totalAmount = calculateTotal(this.items);
        }
        this.updatedAt = Instant.now();
    }

    private static BigDecimal calculateTotal(List<MedicalRecordProcedureItem> items) {
        return items.stream()
                .map(MedicalRecordProcedureItem::getAppliedPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public List<MedicalRecordProcedureItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
