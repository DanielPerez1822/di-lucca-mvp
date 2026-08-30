package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;

public record InvoiceResponseDto(
        UUID id,
        String invoiceNumber,
        UUID patientId,
        UUID medicalRecordId,
        Instant issueDate,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        InvoiceStatus status,
        List<InvoiceItemResponseDto> items,
        List<PaymentResponseDto> payments,
        Instant createdAt,
        Instant updatedAt
) {
    public static InvoiceResponseDto fromApplication(InvoiceResponse response) {
        List<InvoiceItemResponseDto> itemDtos = response.items() != null ?
                response.items().stream().map(InvoiceItemResponseDto::fromApplication).toList() : List.of();

        List<PaymentResponseDto> paymentDtos = response.payments() != null ?
                response.payments().stream().map(PaymentResponseDto::fromApplication).toList() : List.of();

        return new InvoiceResponseDto(
                response.id(),
                response.invoiceNumber(),
                response.patientId(),
                response.medicalRecordId(),
                response.issueDate(),
                response.subtotal(),
                response.taxAmount(),
                response.discountAmount(),
                response.totalAmount(),
                response.paidAmount(),
                response.status(),
                itemDtos,
                paymentDtos,
                response.createdAt(),
                response.updatedAt()
        );
    }
}
