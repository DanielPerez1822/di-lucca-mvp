package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.schedule.command.SetDentistScheduleCommand;
import com.odontosys.odontosys_api.application.schedule.response.AvailabilitySlotResponse;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;
import com.odontosys.odontosys_api.domain.port.in.schedule.GenerateSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetAvailableSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetDentistScheduleUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.SetDentistScheduleUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.GenerateSlotsRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.SetDentistScheduleItemRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.SetDentistSchedulePayloadRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.AvailabilitySlotResponseDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.DentistScheduleResponseDto;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Horarios y Disponibilidad", description = "Endpoints para gestionar horarios semanales de dentistas y franjas de disponibilidad")
public class ScheduleController {

    private final SetDentistScheduleUseCase setDentistScheduleUseCase;
    private final GetDentistScheduleUseCase getDentistScheduleUseCase;
    private final GenerateSlotsUseCase generateSlotsUseCase;
    private final GetAvailableSlotsUseCase getAvailableSlotsUseCase;
    private final UserRepositoryPort userRepository;
    private final com.odontosys.odontosys_api.application.schedule.CopyDentistScheduleService copyDentistScheduleService;

    public ScheduleController(SetDentistScheduleUseCase setDentistScheduleUseCase,
                              GetDentistScheduleUseCase getDentistScheduleUseCase,
                              GenerateSlotsUseCase generateSlotsUseCase,
                              GetAvailableSlotsUseCase getAvailableSlotsUseCase,
                              UserRepositoryPort userRepository,
                              com.odontosys.odontosys_api.application.schedule.CopyDentistScheduleService copyDentistScheduleService) {
        this.setDentistScheduleUseCase = setDentistScheduleUseCase;
        this.getDentistScheduleUseCase = getDentistScheduleUseCase;
        this.generateSlotsUseCase = generateSlotsUseCase;
        this.getAvailableSlotsUseCase = getAvailableSlotsUseCase;
        this.userRepository = userRepository;
        this.copyDentistScheduleService = copyDentistScheduleService;
    }

    @GetMapping("/dentists")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Listar odontólogos", description = "Retorna los odontólogos disponibles en la clínica")
    public ResponseEntity<List<UserResponseDto>> getDentists() {
        List<UserResponseDto> dentists = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.DENTIST) || u.getRoles().contains(Role.ADMIN))
                .map(UserResponse::fromDomain)
                .map(UserResponseDto::fromApplication)
                .toList();
        return ResponseEntity.ok(dentists);
    }

    public record CopyScheduleRequestDto(
            UUID sourceDentistId,
            UUID targetDentistId,
            boolean copyToAll
    ) {}

    @PostMapping("/copy")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Copiar horario de odontólogo", description = "Copia el horario plantilla de un odontólogo hacia otro odontólogo o hacia todos")
    public ResponseEntity<List<DentistScheduleResponseDto>> copySchedule(@Valid @RequestBody CopyScheduleRequestDto request) {
        List<DentistScheduleResponse> responses = copyDentistScheduleService.copySchedule(
                request.sourceDentistId(),
                request.targetDentistId(),
                request.copyToAll()
        );
        List<DentistScheduleResponseDto> dtos = responses.stream().map(DentistScheduleResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Guardar horario semanal (Payload general)", description = "Configura o reemplaza el horario semanal usando un objeto payload {dentistId, days}")
    public ResponseEntity<List<DentistScheduleResponseDto>> setSchedulePayload(@Valid @RequestBody SetDentistSchedulePayloadRequestDto request) {
        List<SetDentistScheduleCommand> commands = request.days().stream()
                .map(dto -> new SetDentistScheduleCommand(
                        dto.dayOfWeek(), dto.startTime(), dto.endTime(), dto.slotDurationMinutes(),
                        dto.hasBreak(), dto.breakStartTime(), dto.breakEndTime()
                ))
                .toList();

        List<DentistScheduleResponse> responses = setDentistScheduleUseCase.execute(request.dentistId(), commands);
        List<DentistScheduleResponseDto> dtos = responses.stream().map(DentistScheduleResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping({"/dentists/{dentistId}", "/dentist/{dentistId}"})
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Configurar horario semanal por dentista ID", description = "Configura los días y horas de atención semanal enviando dentistId en la ruta")
    public ResponseEntity<List<DentistScheduleResponseDto>> setSchedule(
            @PathVariable UUID dentistId,
            @Valid @RequestBody List<SetDentistScheduleItemRequestDto> request
    ) {
        List<SetDentistScheduleCommand> commands = request.stream()
                .map(dto -> new SetDentistScheduleCommand(
                        dto.dayOfWeek(), dto.startTime(), dto.endTime(), dto.slotDurationMinutes(),
                        dto.hasBreak(), dto.breakStartTime(), dto.breakEndTime()
                ))
                .toList();

        List<DentistScheduleResponse> responses = setDentistScheduleUseCase.execute(dentistId, commands);
        List<DentistScheduleResponseDto> dtos = responses.stream().map(DentistScheduleResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping({"/dentists/{dentistId}", "/dentist/{dentistId}"})
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Obtener horario semanal de un dentista", description = "Retorna la configuración semanal de trabajo de un dentista")
    public ResponseEntity<List<DentistScheduleResponseDto>> getSchedule(@PathVariable UUID dentistId) {
        List<DentistScheduleResponse> responses = getDentistScheduleUseCase.execute(dentistId);
        List<DentistScheduleResponseDto> dtos = responses.stream().map(DentistScheduleResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/generate-slots")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Generar slots (Payload general)", description = "Genera las franjas de atención recibiendo dentistId, startDate y endDate en el body")
    public ResponseEntity<List<AvailabilitySlotResponseDto>> generateSlotsPayload(@Valid @RequestBody GenerateSlotsRequestDto request) {
        UUID dId = request.dentistId();
        if (dId == null) {
            throw new IllegalArgumentException("El campo dentistId es obligatorio");
        }
        List<AvailabilitySlotResponse> responses = generateSlotsUseCase.execute(dId, request.startDate(), request.endDate());
        List<AvailabilitySlotResponseDto> dtos = responses.stream().map(AvailabilitySlotResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping({"/dentists/{dentistId}/generate-slots", "/dentist/{dentistId}/generate-slots"})
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Generar slots de disponibilidad por dentista ID", description = "Genera las franjas de atención enviando dentistId en la ruta")
    public ResponseEntity<List<AvailabilitySlotResponseDto>> generateSlots(
            @PathVariable UUID dentistId,
            @Valid @RequestBody GenerateSlotsRequestDto request
    ) {
        List<AvailabilitySlotResponse> responses = generateSlotsUseCase.execute(dentistId, request.startDate(), request.endDate());
        List<AvailabilitySlotResponseDto> dtos = responses.stream().map(AvailabilitySlotResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/slots/available")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Consultar slots disponibles por query params", description = "Consulta slots por dentistId y fecha o rango de fechas")
    public ResponseEntity<List<AvailabilitySlotResponseDto>> getAvailableSlotsQuery(
            @RequestParam UUID dentistId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDate start = date != null ? date : (startDate != null ? startDate : LocalDate.now());
        LocalDate end = date != null ? date : (endDate != null ? endDate : start);

        List<AvailabilitySlotResponse> responses = getAvailableSlotsUseCase.execute(dentistId, start, end);
        List<AvailabilitySlotResponseDto> dtos = responses.stream().map(AvailabilitySlotResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping({"/dentists/{dentistId}/slots", "/dentist/{dentistId}/slots"})
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Consultar slots disponibles", description = "Consulta los slots disponibles (libres) de un dentista para un rango de fechas determinado")
    public ResponseEntity<List<AvailabilitySlotResponseDto>> getAvailableSlots(
            @PathVariable UUID dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<AvailabilitySlotResponse> responses = getAvailableSlotsUseCase.execute(dentistId, startDate, endDate);
        List<AvailabilitySlotResponseDto> dtos = responses.stream().map(AvailabilitySlotResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }
}
