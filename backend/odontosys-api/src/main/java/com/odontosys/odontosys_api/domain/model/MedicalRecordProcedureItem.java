package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Procedimiento/Tratamiento realizado durante una consulta odontológica.
 */
public class MedicalRecordProcedureItem {

    private final UUID id;
    private final UUID procedureId;
    private String procedureName;
    private BigDecimal appliedPrice;
    private Integer toothNumber;
    private String notes;

    private MedicalRecordProcedureItem(UUID id, UUID procedureId, String procedureName,
                                       BigDecimal appliedPrice, Integer toothNumber, String notes) {
        this.id = id;
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.appliedPrice = appliedPrice;
        this.toothNumber = toothNumber;
        this.notes = notes;
    }

    public static MedicalRecordProcedureItem create(UUID procedureId, String procedureName,
                                                   BigDecimal appliedPrice, Integer toothNumber, String notes) {
        Objects.requireNonNull(procedureId, "El ID del procedimiento es obligatorio");
        Objects.requireNonNull(appliedPrice, "El precio aplicado es obligatorio");

        if (appliedPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio aplicado no puede ser negativo");
        }

        return new MedicalRecordProcedureItem(UUID.randomUUID(), procedureId, procedureName, appliedPrice, toothNumber, notes);
    }

    public static MedicalRecordProcedureItem reconstitute(UUID id, UUID procedureId, String procedureName,
                                                          BigDecimal appliedPrice, Integer toothNumber, String notes) {
        return new MedicalRecordProcedureItem(id, procedureId, procedureName, appliedPrice, toothNumber, notes);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProcedureId() {
        return procedureId;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public BigDecimal getAppliedPrice() {
        return appliedPrice;
    }

    public Integer getToothNumber() {
        return toothNumber;
    }

    public String getNotes() {
        return notes;
    }
}
