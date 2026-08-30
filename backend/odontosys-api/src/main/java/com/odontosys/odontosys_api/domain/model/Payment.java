package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Transacción / Registro de Pago realizado a una factura.
 */
public class Payment {

    private final UUID id;
    private final UUID invoiceId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private String referenceNumber;
    private String notes;
    private final Instant paymentDate;

    private Payment(UUID id, UUID invoiceId, BigDecimal amount, PaymentMethod paymentMethod,
                    String referenceNumber, String notes, Instant paymentDate) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
        this.paymentDate = paymentDate;
    }

    public static Payment create(UUID invoiceId, BigDecimal amount, PaymentMethod paymentMethod,
                                 String referenceNumber, String notes) {
        Objects.requireNonNull(invoiceId, "El ID de la factura es obligatorio");
        Objects.requireNonNull(amount, "El monto del pago es obligatorio");
        Objects.requireNonNull(paymentMethod, "El método de pago es obligatorio");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a 0");
        }

        return new Payment(UUID.randomUUID(), invoiceId, amount, paymentMethod, referenceNumber, notes, Instant.now());
    }

    public static Payment reconstitute(UUID id, UUID invoiceId, BigDecimal amount, PaymentMethod paymentMethod,
                                       String referenceNumber, String notes, Instant paymentDate) {
        return new Payment(id, invoiceId, amount, paymentMethod, referenceNumber, notes, paymentDate);
    }

    public UUID getId() {
        return id;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }
}
