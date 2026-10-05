package com.aldisued.iot.monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "monitoring.kafka.topics")
public record KafkaTopicsProperties(
    @NotBlank String alerts
) {
}
