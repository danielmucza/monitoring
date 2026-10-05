package com.aldisued.iot.monitoring.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

  @Query("""
      SELECT AVG(sensorReading.value)
      FROM SensorReading sensorReading
      WHERE sensorReading.sensor.type = :sensorType
        AND sensorReading.timestamp >= :from AND sensorReading.timestamp <= :to
      """)
  Optional<Double> findAverageBySensorTypeInWindow(
      @Param("sensorType") SensorType sensorType,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to);

  @Query("""
      SELECT sensorReading.value
      FROM SensorReading sensorReading
      WHERE sensorReading.sensor.type = :sensorType
        AND sensorReading.timestamp >= :from AND sensorReading.timestamp <= :to
      ORDER BY sensorReading.timestamp
      """)
  List<Double> findSensorReadingValuesBySensorTypeInWindow(
      @Param("sensorType") SensorType sensorType,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to
  );

}
