package com.odontosys.odontosys_api.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Modelo de Dominio — Token / Código de restablecimiento de contraseña.
 */
public class PasswordResetToken {

    private final UUID id;
    private final String email;
    private final String code;
    private final Instant expiresAt;
    private boolean used;
    private final Instant createdAt;

    private PasswordResetToken(UUID id, String email, String code, Instant expiresAt, boolean used, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.used = used;
        this.createdAt = createdAt;
    }

    public static PasswordResetToken create(String email, int expirationMinutes) {
        Objects.requireNonNull(email, "El email es obligatorio");
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(expirationMinutes * 60L);
        String generatedCode = generate6DigitCode();
        return new PasswordResetToken(UUID.randomUUID(), email, generatedCode, expires, false, now);
    }

    public static PasswordResetToken reconstitute(UUID id, String email, String code, Instant expiresAt, boolean used, Instant createdAt) {
        return new PasswordResetToken(id, email, code, expiresAt, used, createdAt);
    }

    public boolean isValid(String inputCode) {
        return !used && code.equalsIgnoreCase(inputCode) && Instant.now().isBefore(expiresAt);
    }

    public void markAsUsed() {
        this.used = true;
    }

    private static String generate6DigitCode() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
