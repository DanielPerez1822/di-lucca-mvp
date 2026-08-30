package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceItemResponse;

public record InvoiceItemResponseDto(
        UUID id,
        UUID procedureId,
        String description,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
    public static InvoiceItemResponseDto fromApplication(InvoiceItemResponse response) {
        return new InvoiceItemResponseDto(
                response.id(),
                response.procedureId(),
                response.description(),
                response.quantity(),
                response.unitPrice(),
                response.lineTotal()
        );
    }
}
