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
import com.odontosys.odontosys_api.application.procedure.command.CreateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.command.UpdateProcedureCommand;
import com.odontosys.odontosys_api.application.procedure.response.ProcedureResponse;
import com.odontosys.odontosys_api.domain.port.in.procedure.CreateProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.DeleteProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.GetProcedureByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.ListProceduresUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.UpdateProcedureUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CreateProcedureRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.UpdateProcedureRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.ProcedureResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/procedures")
@Tag(name = "Catálogo de Procedimientos", description = "Endpoints para la gestión del catálogo de procedimientos y tratamientos odontológicos")
public class ProcedureController {

    private final CreateProcedureUseCase createProcedureUseCase;
    private final UpdateProcedureUseCase updateProcedureUseCase;
    private final GetProcedureByIdUseCase getProcedureByIdUseCase;
    private final ListProceduresUseCase listProceduresUseCase;
    private final DeleteProcedureUseCase deleteProcedureUseCase;

    public ProcedureController(CreateProcedureUseCase createProcedureUseCase,
                               UpdateProcedureUseCase updateProcedureUseCase,
                               GetProcedureByIdUseCase getProcedureByIdUseCase,
                               ListProceduresUseCase listProceduresUseCase,
                               DeleteProcedureUseCase deleteProcedureUseCase) {
        this.createProcedureUseCase = createProcedureUseCase;
        this.updateProcedureUseCase = updateProcedureUseCase;
        this.getProcedureByIdUseCase = getProcedureByIdUseCase;
        this.listProceduresUseCase = listProceduresUseCase;
        this.deleteProcedureUseCase = deleteProcedureUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Crear procedimiento", description = "Permite al Empleado o Administrador agregar un nuevo procedimiento al catálogo")
    public ResponseEntity<ProcedureResponseDto> createProcedure(@Valid @RequestBody CreateProcedureRequestDto request) {
        CreateProcedureCommand command = new CreateProcedureCommand(
                request.name(),
                request.description(),
                request.price(),
                request.duration()
        );

        ProcedureResponse response = createProcedureUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProcedureResponseDto.fromApplication(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Listar procedimientos", description = "Retorna el catálogo completo de procedimientos dentales")
    public ResponseEntity<List<ProcedureResponseDto>> getAllProcedures() {
        List<ProcedureResponseDto> procedures = listProceduresUseCase.execute().stream()
                .map(ProcedureResponseDto::fromApplication)
                .toList();

        return ResponseEntity.ok(procedures);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'DENTIST', 'ADMIN')")
    @Operation(summary = "Obtener procedimiento por ID", description = "Retorna los detalles de un procedimiento específico por su ID")
    public ResponseEntity<ProcedureResponseDto> getProcedureById(@PathVariable UUID id) {
        ProcedureResponse response = getProcedureByIdUseCase.execute(id);
        return ResponseEntity.ok(ProcedureResponseDto.fromApplication(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Actualizar procedimiento", description = "Modifica los datos y precio base de un procedimiento existente")
    public ResponseEntity<ProcedureResponseDto> updateProcedure(@PathVariable UUID id, @Valid @RequestBody UpdateProcedureRequestDto request) {
        UpdateProcedureCommand command = new UpdateProcedureCommand(
                request.name(),
                request.description(),
                request.price(),
                request.duration()
        );

        ProcedureResponse response = updateProcedureUseCase.execute(id, command);
        return ResponseEntity.ok(ProcedureResponseDto.fromApplication(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARY_ASSISTANT', 'ADMIN')")
    @Operation(summary = "Desactivar procedimiento", description = "Desactiva un procedimiento del catálogo")
    public ResponseEntity<Void> deleteProcedure(@PathVariable UUID id) {
        deleteProcedureUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
