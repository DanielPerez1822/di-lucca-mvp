package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.appointment.command.CreateAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.command.RescheduleAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;
import com.odontosys.odontosys_api.domain.port.in.appointment.CancelAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.CreateAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.GetAppointmentByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.ListAppointmentsUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.RescheduleAppointmentUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CancelAppointmentRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CreateAppointmentRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.RescheduleAppointmentRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.AppointmentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Agendamiento de Citas", description = "Endpoints para agendar, reagendar, cancelar y consultar citas odontológicas")
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final RescheduleAppointmentUseCase rescheduleAppointmentUseCase;
    private final CancelAppointmentUseCase cancelAppointmentUseCase;
    private final GetAppointmentByIdUseCase getAppointmentByIdUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;

    public AppointmentController(CreateAppointmentUseCase createAppointmentUseCase,
                                 RescheduleAppointmentUseCase rescheduleAppointmentUseCase,
                                 CancelAppointmentUseCase cancelAppointmentUseCase,
                                 GetAppointmentByIdUseCase getAppointmentByIdUseCase,
                                 ListAppointmentsUseCase listAppointmentsUseCase) {
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.rescheduleAppointmentUseCase = rescheduleAppointmentUseCase;
        this.cancelAppointmentUseCase = cancelAppointmentUseCase;
        this.getAppointmentByIdUseCase = getAppointmentByIdUseCase;
        this.listAppointmentsUseCase = listAppointmentsUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Agendar cita", description = "Reserva una cita odontológica para un paciente en una franja horaria (slot) libre")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody CreateAppointmentRequestDto request) {
        CreateAppointmentCommand command = new CreateAppointmentCommand(
                request.patientId(),
                request.dentistId(),
                request.slotId(),
                request.reason()
        );

        AppointmentResponse response = createAppointmentUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentResponseDto.fromApplication(response));
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Cambiar fecha/hora de cita (Reagendar)", description = "Permite a Empleado, Administrador o Dentista modificar la hora de una cita. Se envía notificación por correo al paciente.")
    public ResponseEntity<AppointmentResponseDto> rescheduleAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody RescheduleAppointmentRequestDto request
    ) {
        RescheduleAppointmentCommand command = new RescheduleAppointmentCommand(request.newSlotId());
        AppointmentResponse response = rescheduleAppointmentUseCase.execute(id, command);
        return ResponseEntity.ok(AppointmentResponseDto.fromApplication(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Cancelar/Eliminar hora de cita", description = "Permite a Empleado, Administrador o Dentista cancelar una cita. Libera el slot y notifica al paciente por correo.")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable UUID id,
            @RequestBody(required = false) CancelAppointmentRequestDto request
    ) {
        String reason = request != null ? request.reason() : null;
        cancelAppointmentUseCase.execute(id, reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Obtener cita por ID", description = "Retorna los detalles de una cita odontológica específica")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponse response = getAppointmentByIdUseCase.execute(id);
        return ResponseEntity.ok(AppointmentResponseDto.fromApplication(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Listar citas con filtros", description = "Filtra citas por dentista (calendario), paciente, rango de fechas o estado")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointments(
            @RequestParam(required = false) UUID dentistId,
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) AppointmentStatus status
    ) {
        List<AppointmentResponse> responses = listAppointmentsUseCase.execute(dentistId, patientId, startDate, endDate, status);
        List<AppointmentResponseDto> dtos = responses.stream().map(AppointmentResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }
}
