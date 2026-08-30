package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.patient.command.CreatePatientCommand;
import com.odontosys.odontosys_api.application.patient.command.UpdatePatientCommand;
import com.odontosys.odontosys_api.application.patient.response.PatientResponse;
import com.odontosys.odontosys_api.domain.port.in.patient.CreatePatientUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.DeletePatientUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.GetPatientByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.ListPatientsUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.UpdatePatientUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CreatePatientRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.UpdatePatientRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.PatientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/patients")
@PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
@Tag(name = "Gestión de Pacientes", description = "Endpoints para registro, consulta, edición y desactivación de pacientes del consultorio")
public class PatientController {

    private final CreatePatientUseCase createPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final GetPatientByIdUseCase getPatientByIdUseCase;
    private final ListPatientsUseCase listPatientsUseCase;
    private final DeletePatientUseCase deletePatientUseCase;

    public PatientController(CreatePatientUseCase createPatientUseCase,
                             UpdatePatientUseCase updatePatientUseCase,
                             GetPatientByIdUseCase getPatientByIdUseCase,
                             ListPatientsUseCase listPatientsUseCase,
                             DeletePatientUseCase deletePatientUseCase) {
        this.createPatientUseCase = createPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.getPatientByIdUseCase = getPatientByIdUseCase;
        this.listPatientsUseCase = listPatientsUseCase;
        this.deletePatientUseCase = deletePatientUseCase;
    }

    @PostMapping
    @Operation(summary = "Registrar paciente", description = "Permite al Empleado o Administrador registrar los datos de un nuevo paciente")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody CreatePatientRequestDto request) {
        CreatePatientCommand command = new CreatePatientCommand(
                request.firstName(),
                request.lastName(),
                request.documentType(),
                request.documentNumber(),
                request.phone(),
                request.email(),
                request.dateOfBirth(),
                request.address()
        );

        PatientResponse response = createPatientUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(PatientResponseDto.fromApplication(response));
    }

    @GetMapping
    @Operation(summary = "Listar pacientes", description = "Retorna la lista de todos los pacientes registrados en el consultorio")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> patients = listPatientsUseCase.execute().stream()
                .map(PatientResponseDto::fromApplication)
                .toList();

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener paciente por ID", description = "Retorna la información detallada de un paciente específico")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable UUID id) {
        PatientResponse response = getPatientByIdUseCase.execute(id);
        return ResponseEntity.ok(PatientResponseDto.fromApplication(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Modifica los datos personales y de contacto de un paciente existente")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id, @Valid @RequestBody UpdatePatientRequestDto request) {
        UpdatePatientCommand command = new UpdatePatientCommand(
                request.firstName(),
                request.lastName(),
                request.documentType(),
                request.documentNumber(),
                request.phone(),
                request.email(),
                request.dateOfBirth(),
                request.address()
        );

        PatientResponse response = updatePatientUseCase.execute(id, command);
        return ResponseEntity.ok(PatientResponseDto.fromApplication(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar paciente", description = "Desactiva la ficha de un paciente del consultorio")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        deletePatientUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
