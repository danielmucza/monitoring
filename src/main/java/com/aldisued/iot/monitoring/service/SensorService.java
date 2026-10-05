package com.aldisued.iot.monitoring.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.aldisued.iot.monitoring.config.DbConstraintsProperties;
import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.SensorAlreadyExistsException;
import com.aldisued.iot.monitoring.repository.SensorRepository;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;
  private final String sensorNameUniqueConstraint;

  public SensorService(SensorRepository sensorRepository, DbConstraintsProperties dbConstraintsProperties) {
    this.sensorRepository = sensorRepository;
    this.sensorNameUniqueConstraint = dbConstraintsProperties.sensorNameUnique();
  }

  public Sensor saveSensor(SensorDto sensor) {
    try {
      return sensorRepository.save(new Sensor(sensor.name(), sensor.type()));
    } catch (DataIntegrityViolationException exception) {
      if (exception.getCause() instanceof ConstraintViolationException cve && sensorNameUniqueConstraint.equalsIgnoreCase(cve.getConstraintName())) {
        throw new SensorAlreadyExistsException(sensor.name());
      }
      throw exception;
    }
  }
}
