package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Ítem/Línea de detalle dentro de una factura.
 */
public class InvoiceItem {

    private final UUID id;
    private final UUID procedureId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    private InvoiceItem(UUID id, UUID procedureId, String description, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.id = id;
        this.procedureId = procedureId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal != null ? lineTotal : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static InvoiceItem create(UUID procedureId, String description, int quantity, BigDecimal unitPrice) {
        Objects.requireNonNull(description, "La descripción del ítem es obligatoria");
        Objects.requireNonNull(unitPrice, "El precio unitario es obligatorio");

        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }

        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new InvoiceItem(UUID.randomUUID(), procedureId, description, quantity, unitPrice, lineTotal);
    }

    public static InvoiceItem reconstitute(UUID id, UUID procedureId, String description, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        return new InvoiceItem(id, procedureId, description, quantity, unitPrice, lineTotal);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProcedureId() {
        return procedureId;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
