package com.aldisued.iot.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.aldisued.iot.monitoring.entity.SensorType;

public record SensorDto(
    @NotBlank String name,
    @NotNull SensorType type
) {
}
