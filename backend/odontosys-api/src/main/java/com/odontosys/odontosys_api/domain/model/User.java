package com.odontosys.odontosys_api.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Aggregate Root — Usuario del sistema.
 * <p>
 * POJO puro sin dependencias de framework.
 * Las invariantes se validan en los factory methods.
 */
public class User {

    private final UUID id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private String documentNumber;
    private boolean active;
    private final Set<Role> roles;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private User(UUID id, String email, String passwordHash, String firstName,
                 String lastName, String phone, String documentNumber,
                 boolean active, Set<Role> roles, Instant createdAt,
                 Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.documentNumber = documentNumber;
        this.active = active;
        this.roles = new HashSet<>(roles);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // ─── Factory Methods ──────────────────────────────────────

    /**
     * Crea un nuevo usuario validando las invariantes de negocio.
     */
    public static User create(String email, String passwordHash, String firstName,
                              String lastName, String phone, String documentNumber,
                              Set<Role> roles) {
        Objects.requireNonNull(email, "El email es obligatorio");
        Objects.requireNonNull(passwordHash, "El password hash es obligatorio");
        Objects.requireNonNull(firstName, "El nombre es obligatorio");
        Objects.requireNonNull(lastName, "El apellido es obligatorio");

        if (email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol");
        }

        Instant now = Instant.now();
        return new User(UUID.randomUUID(), email, passwordHash, firstName,
                lastName, phone, documentNumber, true, roles, now, now, null);
    }

    /**
     * Reconstituye un usuario desde la capa de persistencia.
     * No valida invariantes porque los datos ya fueron validados al crearse.
     */
    public static User reconstitute(UUID id, String email, String passwordHash,
                                    String firstName, String lastName, String phone,
                                    String documentNumber, boolean active,
                                    Set<Role> roles, Instant createdAt,
                                    Instant updatedAt, Instant deletedAt) {
        return new User(id, email, passwordHash, firstName, lastName, phone,
                documentNumber, active, roles, createdAt, updatedAt, deletedAt);
    }

    // ─── Comportamiento de dominio ────────────────────────────

    /**
     * Desactiva el usuario (soft delete).
     */
    public void deactivate() {
        this.active = false;
        this.deletedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Activa la cuenta del usuario.
     */
    public void activate() {
        this.active = true;
        this.deletedAt = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Actualiza los roles asignados al usuario.
     */
    public void setRoles(Set<Role> newRoles) {
        if (newRoles != null && !newRoles.isEmpty()) {
            this.roles.clear();
            this.roles.addAll(newRoles);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Actualiza los datos del usuario.
     */
    public void updateProfile(String firstName, String lastName, String phone) {
        updateProfile(firstName, lastName, phone, this.documentNumber);
    }

    /**
     * Actualiza los datos del usuario.
     */
    public void updateProfile(String firstName, String lastName, String phone,
                              String documentNumber) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
        this.phone = phone;
        this.documentNumber = documentNumber;
        this.updatedAt = Instant.now();
    }

    /**
     * Actualiza el password hash del usuario.
     */
    public void updatePassword(String newPasswordHash) {
        Objects.requireNonNull(newPasswordHash, "El password hash es obligatorio");
        this.passwordHash = newPasswordHash;
        this.updatedAt = Instant.now();
    }

    /**
     * Verifica si el usuario está activo y puede iniciar sesión.
     */
    public boolean canLogin() {
        return this.active && this.deletedAt == null;
    }

    // ─── Getters ──────────────────────────────────────────────

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
