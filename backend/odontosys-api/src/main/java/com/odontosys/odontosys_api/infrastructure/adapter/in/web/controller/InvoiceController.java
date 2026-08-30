package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.invoice.command.RegisterPaymentCommand;
import com.odontosys.odontosys_api.application.invoice.response.InvoiceResponse;
import com.odontosys.odontosys_api.domain.model.InvoiceStatus;
import com.odontosys.odontosys_api.domain.port.in.invoice.CreateInvoiceFromMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.GetInvoiceByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.ListInvoicesUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.RegisterPaymentUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.SendInvoiceEmailUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.UpdateInvoiceUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.RegisterPaymentRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.UpdateInvoiceRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.InvoiceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Facturación y Pagos", description = "Endpoints para generación de facturas desde atención médica, registro de pagos y envío de comprobante HTML por correo")
public class InvoiceController {

    private final CreateInvoiceFromMedicalRecordUseCase createInvoiceFromMedicalRecordUseCase;
    private final RegisterPaymentUseCase registerPaymentUseCase;
    private final GetInvoiceByIdUseCase getInvoiceByIdUseCase;
    private final ListInvoicesUseCase listInvoicesUseCase;
    private final SendInvoiceEmailUseCase sendInvoiceEmailUseCase;
    private final UpdateInvoiceUseCase updateInvoiceUseCase;

    public InvoiceController(CreateInvoiceFromMedicalRecordUseCase createInvoiceFromMedicalRecordUseCase,
                             RegisterPaymentUseCase registerPaymentUseCase,
                             GetInvoiceByIdUseCase getInvoiceByIdUseCase,
                             ListInvoicesUseCase listInvoicesUseCase,
                             SendInvoiceEmailUseCase sendInvoiceEmailUseCase,
                             UpdateInvoiceUseCase updateInvoiceUseCase) {
        this.createInvoiceFromMedicalRecordUseCase = createInvoiceFromMedicalRecordUseCase;
        this.registerPaymentUseCase = registerPaymentUseCase;
        this.getInvoiceByIdUseCase = getInvoiceByIdUseCase;
        this.listInvoicesUseCase = listInvoicesUseCase;
        this.sendInvoiceEmailUseCase = sendInvoiceEmailUseCase;
        this.updateInvoiceUseCase = updateInvoiceUseCase;
    }

    @PostMapping("/from-medical-record/{medicalRecordId}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Generar factura desde atención clínica", description = "Genera la factura automáticamente con los procedimientos y precios cobrados en la consulta del paciente")
    public ResponseEntity<InvoiceResponseDto> createInvoiceFromMedicalRecord(@PathVariable UUID medicalRecordId) {
        InvoiceResponse response = createInvoiceFromMedicalRecordUseCase.execute(medicalRecordId);
        return ResponseEntity.status(HttpStatus.CREATED).body(InvoiceResponseDto.fromApplication(response));
    }

    @PostMapping("/{invoiceId}/payments")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Registrar pago de factura", description = "Registra un pago (Efectivo, Tarjeta, Transferencia, etc.) y envía automáticamente el comprobante HTML al correo del paciente")
    public ResponseEntity<InvoiceResponseDto> registerPayment(
            @PathVariable UUID invoiceId,
            @Valid @RequestBody RegisterPaymentRequestDto request
    ) {
        RegisterPaymentCommand command = new RegisterPaymentCommand(
                request.amount(),
                request.paymentMethod(),
                request.referenceNumber(),
                request.notes()
        );

        InvoiceResponse response = registerPaymentUseCase.execute(invoiceId, command);
        return ResponseEntity.ok(InvoiceResponseDto.fromApplication(response));
    }

    @PostMapping("/{invoiceId}/send-email")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Reenviar factura/comprobante por correo", description = "Envía la factura y recibo HTML al correo electrónico registrado del paciente")
    public ResponseEntity<Void> sendInvoiceEmail(@PathVariable UUID invoiceId) {
        sendInvoiceEmailUseCase.execute(invoiceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Obtener factura por ID", description = "Retorna el detalle de la factura, sus ítems y la lista de pagos realizados")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable UUID id) {
        InvoiceResponse response = getInvoiceByIdUseCase.execute(id);
        return ResponseEntity.ok(InvoiceResponseDto.fromApplication(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Listar facturas", description = "Lista las facturas filtrando opcionalmente por paciente o estado (PENDING, PAID, PARTIALLY_PAID)")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoices(
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) InvoiceStatus status
    ) {
        List<InvoiceResponse> responses = listInvoicesUseCase.execute(patientId, status);
        List<InvoiceResponseDto> dtos = responses.stream().map(InvoiceResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Actualizar factura", description = "Permite cambiar el estado y el monto pagado manteniendo el monto total fijo, y reenvía automáticamente el comprobante actualizado por correo al paciente")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInvoiceRequestDto request
    ) {
        InvoiceResponse response = updateInvoiceUseCase.execute(id, request.status(), request.paidAmount());
        return ResponseEntity.ok(InvoiceResponseDto.fromApplication(response));
    }
}
