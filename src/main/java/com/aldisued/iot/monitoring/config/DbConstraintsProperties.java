package com.aldisued.iot.monitoring.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "monitoring.db.constraints")
public record DbConstraintsProperties(
    @NotBlank String sensorNameUnique
) {
}
