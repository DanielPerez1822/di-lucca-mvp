package com.odontosys.odontosys_api.application.invoice.response;

import java.math.BigDecimal;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.InvoiceItem;

public record InvoiceItemResponse(
        UUID id,
        UUID procedureId,
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
    public static InvoiceItemResponse fromDomain(InvoiceItem item) {
        return new InvoiceItemResponse(
                item.getId(),
                item.getProcedureId(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
    }
}
