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
import com.odontosys.odontosys_api.application.auth.command.CreateUserCommand;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.CreateUserUseCase;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.CreateUserRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Gestión de Usuarios", description = "Endpoints de administración para creación y consulta de usuarios (Solo ADMIN)")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserRepositoryPort userRepository;

    public UserController(CreateUserUseCase createUserUseCase, UserRepositoryPort userRepository) {
        this.createUserUseCase = createUserUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Permite al Administrador registrar nuevos empleados (asistentes) o dentistas")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        if (request.roles() != null && request.roles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("No está permitido crear nuevos usuarios con el rol ADMINISTRADOR. Solo puede existir 1 administrador.");
        }

        CreateUserCommand command = new CreateUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.documentNumber(),
                request.roles()
        );

        UserResponse response = createUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.fromApplication(response));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna la lista de todos los usuarios registrados")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(UserResponse::fromDomain)
                .map(UserResponseDto::fromApplication)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna los detalles de un usuario específico")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::fromDomain)
                .map(UserResponseDto::fromApplication)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar información de usuario", description = "Permite editar datos personales y rol del usuario")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.UpdateUserRequestDto request
    ) {
        return userRepository.findById(id).map(user -> {
            boolean wasAdmin = user.getRoles().contains(Role.ADMIN);
            if (!wasAdmin && request.roles() != null && request.roles().contains(Role.ADMIN)) {
                throw new IllegalArgumentException("No está permitido asignar el rol ADMINISTRADOR a otros usuarios.");
            }

            user.updateProfile(request.firstName(), request.lastName(), request.phone());
            if (request.roles() != null && !request.roles().isEmpty()) {
                user.setRoles(request.roles());
            }
            if (request.active() != null) {
                if (request.active()) {
                    user.activate();
                } else {
                    user.deactivate();
                }
            }

            User saved = userRepository.save(user);
            return ResponseEntity.ok(UserResponseDto.fromApplication(UserResponse.fromDomain(saved)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario", description = "Desactiva la cuenta de un usuario (soft delete)")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        return userRepository.findById(id).map(user -> {
            user.deactivate();
            userRepository.save(user);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
