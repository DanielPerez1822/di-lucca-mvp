package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_record_procedures")
public class MedicalRecordProcedureJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecordJpaEntity medicalRecord;

    @Column(name = "procedure_id", nullable = false)
    private UUID procedureId;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "applied_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal appliedPrice;

    @Column(name = "tooth_number")
    private Integer toothNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public MedicalRecordProcedureJpaEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MedicalRecordJpaEntity getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecordJpaEntity medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public UUID getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(UUID procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public BigDecimal getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(BigDecimal appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public Integer getToothNumber() {
        return toothNumber;
    }

    public void setToothNumber(Integer toothNumber) {
        this.toothNumber = toothNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
