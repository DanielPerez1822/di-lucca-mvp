package com.odontosys.odontosys_api.infrastructure.adapter.in.web.controller;

import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.odontosys.odontosys_api.application.auth.command.LoginCommand;
import com.odontosys.odontosys_api.application.auth.command.RefreshTokenCommand;
import com.odontosys.odontosys_api.application.auth.command.RequestPasswordResetCommand;
import com.odontosys.odontosys_api.application.auth.command.ResetPasswordCommand;
import com.odontosys.odontosys_api.application.auth.response.AuthResponse;
import com.odontosys.odontosys_api.application.auth.response.TokenResponse;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.port.in.auth.GetCurrentUserUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.LoginUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.LogoutUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.RefreshTokenUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.RequestPasswordResetUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.ResetPasswordUseCase;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.ForgotPasswordRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.LoginRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.RefreshTokenRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request.ResetPasswordRequestDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.AuthResponseDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.TokenResponseDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.UserResponseDto;
import com.odontosys.odontosys_api.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión, renovación de tokens, perfil y recuperación de contraseña")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          LogoutUseCase logoutUseCase,
                          GetCurrentUserUseCase getCurrentUserUseCase,
                          RequestPasswordResetUseCase requestPasswordResetUseCase,
                          ResetPasswordUseCase resetPasswordUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.requestPasswordResetUseCase = requestPasswordResetUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña, retornando tokens JWT")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        AuthResponse response = loginUseCase.execute(command);

        AuthResponseDto dto = new AuthResponseDto(
                UserResponseDto.fromApplication(response.user()),
                response.accessToken(),
                response.refreshToken()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens", description = "Renueva el access token usando un refresh token válido (con rotación de token)")
    public ResponseEntity<TokenResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        RefreshTokenCommand command = new RefreshTokenCommand(request.refreshToken());
        TokenResponse response = refreshTokenUseCase.execute(command);

        return ResponseEntity.ok(new TokenResponseDto(response.accessToken(), response.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Revoca todos los refresh tokens activos del usuario autenticado")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        logoutUseCase.execute(userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil", description = "Retorna los datos del usuario autenticado actualmente")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = getCurrentUserUseCase.execute(userDetails.getId());
        return ResponseEntity.ok(UserResponseDto.fromApplication(response));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar código de recuperación", description = "Envía un código de verificación de 6 dígitos al correo electrónico registrado")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        RequestPasswordResetCommand command = new RequestPasswordResetCommand(request.email());
        requestPasswordResetUseCase.execute(command);
        return ResponseEntity.ok(Map.of("message", "Si el correo está registrado, recibirás un código de verificación."));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contraseña", description = "Restablece la contraseña ingresando el correo, código de verificación recibido y la nueva contraseña")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        ResetPasswordCommand command = new ResetPasswordCommand(request.email(), request.code(), request.newPassword());
        resetPasswordUseCase.execute(command);
        return ResponseEntity.ok(Map.of("message", "Contraseña restablecida exitosamente. Puedes iniciar sesión con tu nueva contraseña."));
    }
}
