package com.odontosys.odontosys_api.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root — Procedimiento Dental del catálogo.
 * POJO puro desacoplado de frameworks.
 */
public class Procedure {

    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration; // Duración estimada en minutos
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private Procedure(UUID id, String name, String description, BigDecimal price,
                      Integer duration, boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Procedure create(String name, String description, BigDecimal price, Integer duration) {
        Objects.requireNonNull(name, "El nombre del procedimiento es obligatorio");
        Objects.requireNonNull(price, "El precio del procedimiento es obligatorio");

        if (name.isBlank()) {
            throw new IllegalArgumentException("El nombre del procedimiento no puede estar vacío");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (duration != null && duration <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }

        Instant now = Instant.now();
        return new Procedure(UUID.randomUUID(), name, description, price, duration, true, now, now);
    }

    public static Procedure reconstitute(UUID id, String name, String description, BigDecimal price,
                                         Integer duration, boolean active, Instant createdAt, Instant updatedAt) {
        return new Procedure(id, name, description, price, duration, active, createdAt, updatedAt);
    }

    public void update(String name, String description, BigDecimal price, Integer duration) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            this.price = price;
        }
        if (duration != null) {
            if (duration <= 0) {
                throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
            }
            this.duration = duration;
        }
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
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
