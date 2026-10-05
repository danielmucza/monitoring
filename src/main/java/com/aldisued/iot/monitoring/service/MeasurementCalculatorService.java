package com.aldisued.iot.monitoring.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviationPercentage) {
    if (deviationPercentage == null || deviationPercentage < 0.0 || deviationPercentage > 1.0) {
      throw new IllegalArgumentException("Deviation must be between 0.0 and 1.0");
    }

    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }

    final double average = values.stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElseThrow();
    final double deviation = Math.abs(average) * deviationPercentage;
    final double lowerBound = average - deviation;
    final double upperBound = average + deviation;

    return values.stream()
        .filter(value -> value >= lowerBound && value <= upperBound)
        .toList();
  }

  public List<Double> getMovingAverage(List<Double> data, int windowSize) {
    // TODO: Task 10
    return List.of();
  }

}
