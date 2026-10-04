package com.aldisued.iot.monitoring.service;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;

@Service
@Validated
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(
      SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository
  ) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public SensorReading saveSensorReading(@Valid SensorReadingDto sensorReadingDto) {
    final Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
        .orElseThrow(() -> new SensorNotFoundException(sensorReadingDto.sensorId()));

    final SensorReading sensorReading = new SensorReading(sensorReadingDto.value(), sensorReadingDto.timestamp(), sensor);
    return sensorReadingRepository.save(sensorReading);
  }

}
