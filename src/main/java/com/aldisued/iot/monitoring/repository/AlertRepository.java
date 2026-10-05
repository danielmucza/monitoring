package com.aldisued.iot.monitoring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aldisued.iot.monitoring.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {

  @Query("""
      SELECT alert
      FROM Alert alert
      WHERE alert.sensor.id = :sensorId
      ORDER BY alert.timestamp DESC
      LIMIT 1
      """)
  Optional<Alert> findLatestAlertBySensorId(@Param("sensorId") UUID sensorId);

}
