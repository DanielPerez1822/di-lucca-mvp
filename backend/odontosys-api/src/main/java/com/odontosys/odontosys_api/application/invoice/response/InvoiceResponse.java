package com.odontosys.odontosys_api.application.invoice.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Invoice;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;

public record InvoiceResponse(
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
        List<InvoiceItemResponse> items,
        List<PaymentResponse> payments,
        Instant createdAt,
        Instant updatedAt
) {
    public static InvoiceResponse fromDomain(Invoice invoice, List<PaymentResponse> payments) {
        List<InvoiceItemResponse> itemResponses = invoice.getItems().stream()
                .map(InvoiceItemResponse::fromDomain)
                .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getPatientId(),
                invoice.getMedicalRecordId(),
                invoice.getIssueDate(),
                invoice.getSubtotal(),
                invoice.getTaxAmount(),
                invoice.getDiscountAmount(),
                invoice.getTotalAmount(),
                invoice.getPaidAmount(),
                invoice.getStatus(),
                itemResponses,
                payments != null ? payments : List.of(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt()
        );
    }
}
