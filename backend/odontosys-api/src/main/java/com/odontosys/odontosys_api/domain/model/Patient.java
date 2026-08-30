package com.odontosys.odontosys_api.domain.model;

import java.time.LocalDate;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Paciente del consultorio odontológico.
 * POJO puro sin dependencias de frameworks.
 */
public class Patient {

    private final UUID id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private Patient(UUID id, String firstName, String lastName, String documentType,
                    String documentNumber, String phone, String email,
                    LocalDate dateOfBirth, String address, boolean active,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Patient create(String firstName, String lastName, String documentType,
                                 String documentNumber, String phone, String email,
                                 LocalDate dateOfBirth, String address) {
        Objects.requireNonNull(firstName, "El nombre es obligatorio");
        Objects.requireNonNull(lastName, "El apellido es obligatorio");
        Objects.requireNonNull(documentType, "El tipo de documento es obligatorio");
        Objects.requireNonNull(documentNumber, "El número de documento es obligatorio");

        if (firstName.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (documentNumber.isBlank()) {
            throw new IllegalArgumentException("El número de documento no puede estar vacío");
        }

        Instant now = Instant.now();
        return new Patient(UUID.randomUUID(), firstName, lastName, documentType,
                documentNumber, phone, email, dateOfBirth, address, true, now, now);
    }

    public static Patient reconstitute(UUID id, String firstName, String lastName, String documentType,
                                       String documentNumber, String phone, String email,
                                       LocalDate dateOfBirth, String address, boolean active,
                                       Instant createdAt, Instant updatedAt) {
        return new Patient(id, firstName, lastName, documentType, documentNumber,
                phone, email, dateOfBirth, address, active, createdAt, updatedAt);
    }

    public void updateDetails(String firstName, String lastName, String documentType,
                              String documentNumber, String phone, String email,
                              LocalDate dateOfBirth, String address) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
        if (documentType != null && !documentType.isBlank()) {
            this.documentType = documentType;
        }
        if (documentNumber != null && !documentNumber.isBlank()) {
            this.documentNumber = documentNumber;
        }
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
