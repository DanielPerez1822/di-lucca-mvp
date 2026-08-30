package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Factura Clínica Odontológica.
 */
public class Invoice {

    private final UUID id;
    private final String invoiceNumber;
    private final UUID patientId;
    private final UUID medicalRecordId;
    private final Instant issueDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private InvoiceStatus status;
    private final List<InvoiceItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    private Invoice(UUID id, String invoiceNumber, UUID patientId, UUID medicalRecordId, Instant issueDate,
                    BigDecimal subtotal, BigDecimal taxAmount, BigDecimal discountAmount, BigDecimal totalAmount,
                    BigDecimal paidAmount, InvoiceStatus status, List<InvoiceItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.patientId = patientId;
        this.medicalRecordId = medicalRecordId;
        this.issueDate = issueDate;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.status = status;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Invoice create(String invoiceNumber, UUID patientId, UUID medicalRecordId,
                                 List<InvoiceItem> items, BigDecimal taxAmount, BigDecimal discountAmount) {
        Objects.requireNonNull(invoiceNumber, "El número de factura es obligatorio");
        Objects.requireNonNull(patientId, "El ID del paciente es obligatorio");

        BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;

        List<InvoiceItem> safeItems = items != null ? new ArrayList<>(items) : new ArrayList<>();
        BigDecimal subtotal = safeItems.stream().map(InvoiceItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = subtotal.add(tax).subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        Instant now = Instant.now();
        return new Invoice(UUID.randomUUID(), invoiceNumber, patientId, medicalRecordId, now,
                subtotal, tax, discount, total, BigDecimal.ZERO, InvoiceStatus.PENDING, safeItems, now, now);
    }

    public static Invoice reconstitute(UUID id, String invoiceNumber, UUID patientId, UUID medicalRecordId, Instant issueDate,
                                       BigDecimal subtotal, BigDecimal taxAmount, BigDecimal discountAmount, BigDecimal totalAmount,
                                       BigDecimal paidAmount, InvoiceStatus status, List<InvoiceItem> items, Instant createdAt, Instant updatedAt) {
        return new Invoice(id, invoiceNumber, patientId, medicalRecordId, issueDate, subtotal, taxAmount,
                discountAmount, totalAmount, paidAmount, status, items, createdAt, updatedAt);
    }

    public void addPaymentAmount(BigDecimal paymentAmount) {
        if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a 0");
        }
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("La factura ya se encuentra totalmente pagada");
        }
        if (this.status == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("No se pueden registrar pagos en una factura cancelada");
        }

        this.paidAmount = this.paidAmount.add(paymentAmount);

        if (this.paidAmount.compareTo(this.totalAmount) >= 0) {
            this.status = InvoiceStatus.PAID;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        this.status = InvoiceStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void updateStatusAndPaidAmount(InvoiceStatus newStatus, BigDecimal newPaidAmount) {
        if (newPaidAmount != null) {
            if (newPaidAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El monto pagado no puede ser negativo");
            }
            this.paidAmount = newPaidAmount;
        }
        if (newStatus != null) {
            this.status = newStatus;
        } else if (this.paidAmount != null) {
            if (this.paidAmount.compareTo(this.totalAmount) >= 0) {
                this.status = InvoiceStatus.PAID;
            } else if (this.paidAmount.compareTo(BigDecimal.ZERO) > 0) {
                this.status = InvoiceStatus.PARTIALLY_PAID;
            } else {
                this.status = InvoiceStatus.PENDING;
            }
        }
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getMedicalRecordId() {
        return medicalRecordId;
    }

    public Instant getIssueDate() {
        return issueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public List<InvoiceItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
