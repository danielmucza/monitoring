package com.aldisued.iot.monitoring.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aldisued.iot.monitoring.config.KafkaTopicsProperties;
import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.AlertNotFoundException;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;
  private final String alertsTopic;

  public AlertService(
      AlertRepository alertRepository,
      SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate,
      KafkaTopicsProperties kafkaTopicsProperties
  ) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
    this.alertsTopic = kafkaTopicsProperties.alerts();
  }

  @Transactional
  public Alert saveAlert(AlertDto alertDto) {
    final Sensor sensor = sensorRepository.findById(alertDto.sensorId())
        .orElseThrow(() -> new SensorNotFoundException(alertDto.sensorId()));

    final Alert alert = alertRepository.save(new Alert(alertDto.message(), alertDto.timestamp(), sensor));
    kafkaTemplate.send(alertsTopic, alertDto);  // But beware of the double write issue: the DB commit fails but the
                                                // message publishing to the topic succeeds. The outbox pattern or CDC
                                                // could be a solution to this.
    return alert;
  }

  @Transactional(readOnly = true)
  public AlertDto findLastAlertBySensorId(UUID sensorId) {
    return alertRepository.findLatestAlertBySensorId(sensorId)
        .map(alert -> new AlertDto(sensorId, alert.getMessage(), alert.getTimestamp()))
        .orElseThrow(() -> new AlertNotFoundException(sensorId));
  }
}
