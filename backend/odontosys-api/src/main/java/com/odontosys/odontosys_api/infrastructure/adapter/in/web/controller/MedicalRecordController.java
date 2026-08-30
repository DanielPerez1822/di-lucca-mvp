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
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.medicalrecord.command.CreateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.command.MedicalRecordProcedureItemCommand;
import com.odontosys.odontosys_api.application.medicalrecord.command.UpdateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.CreateMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetMedicalRecordByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetPatientMedicalHistoryUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.UpdateMedicalRecordUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CreateMedicalRecordRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.UpdateMedicalRecordRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.MedicalRecordResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/medical-records")
@Tag(name = "Historial Médico Clínico", description = "Endpoints para registro de atención odontológica, diagnósticos, procedimientos aplicados e historial del paciente")
public class MedicalRecordController {

    private final CreateMedicalRecordUseCase createMedicalRecordUseCase;
    private final GetMedicalRecordByIdUseCase getMedicalRecordByIdUseCase;
    private final GetPatientMedicalHistoryUseCase getPatientMedicalHistoryUseCase;
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;

    public MedicalRecordController(CreateMedicalRecordUseCase createMedicalRecordUseCase,
                                   GetMedicalRecordByIdUseCase getMedicalRecordByIdUseCase,
                                   GetPatientMedicalHistoryUseCase getPatientMedicalHistoryUseCase,
                                   UpdateMedicalRecordUseCase updateMedicalRecordUseCase) {
        this.createMedicalRecordUseCase = createMedicalRecordUseCase;
        this.getMedicalRecordByIdUseCase = getMedicalRecordByIdUseCase;
        this.getPatientMedicalHistoryUseCase = getPatientMedicalHistoryUseCase;
        this.updateMedicalRecordUseCase = updateMedicalRecordUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DENTIST', 'SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Registrar atención clínica", description = "Permite al odontólogo (o empleado/admin) registrar el diagnóstico, notas clínicas y procedimientos aplicados en una consulta")
    public ResponseEntity<MedicalRecordResponseDto> createRecord(@Valid @RequestBody CreateMedicalRecordRequestDto request) {
        List<MedicalRecordProcedureItemCommand> itemCmds = request.items() != null ?
                request.items().stream()
                        .map(i -> new MedicalRecordProcedureItemCommand(i.procedureId(), i.appliedPrice(), i.toothNumber(), i.notes()))
                        .toList() : List.of();

        CreateMedicalRecordCommand command = new CreateMedicalRecordCommand(
                request.patientId(),
                request.dentistId(),
                request.appointmentId(),
                request.diagnosis(),
                request.notes(),
                itemCmds
        );

        MedicalRecordResponse response = createMedicalRecordUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(MedicalRecordResponseDto.fromApplication(response));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Consultar historial médico de un paciente", description = "Retorna la historia clínica completa con todas las consultas médicas, diagnósticos y procedimientos realizados al paciente")
    public ResponseEntity<List<MedicalRecordResponseDto>> getPatientHistory(@PathVariable UUID patientId) {
        List<MedicalRecordResponse> responses = getPatientMedicalHistoryUseCase.execute(patientId);
        List<MedicalRecordResponseDto> dtos = responses.stream().map(MedicalRecordResponseDto::fromApplication).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Obtener detalle de consulta médica por ID", description = "Retorna los datos y procedimientos aplicados en una atención clínica específica")
    public ResponseEntity<MedicalRecordResponseDto> getRecordById(@PathVariable UUID id) {
        MedicalRecordResponse response = getMedicalRecordByIdUseCase.execute(id);
        return ResponseEntity.ok(MedicalRecordResponseDto.fromApplication(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DENTIST', 'SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Actualizar consulta médica", description = "Permite modificar el diagnóstico, notas o lista de procedimientos de una atención registrada")
    public ResponseEntity<MedicalRecordResponseDto> updateRecord(@PathVariable UUID id, @Valid @RequestBody UpdateMedicalRecordRequestDto request) {
        List<MedicalRecordProcedureItemCommand> itemCmds = request.items() != null ?
                request.items().stream()
                        .map(i -> new MedicalRecordProcedureItemCommand(i.procedureId(), i.appliedPrice(), i.toothNumber(), i.notes()))
                        .toList() : null;

        UpdateMedicalRecordCommand command = new UpdateMedicalRecordCommand(
                request.diagnosis(),
                request.notes(),
                itemCmds
        );

        MedicalRecordResponse response = updateMedicalRecordUseCase.execute(id, command);
        return ResponseEntity.ok(MedicalRecordResponseDto.fromApplication(response));
    }
}
