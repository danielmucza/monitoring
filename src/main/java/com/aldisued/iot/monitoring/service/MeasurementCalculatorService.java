package com.aldisued.iot.monitoring.service;

import java.util.ArrayList;
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
    if (data == null || data.isEmpty()) {
      throw new IllegalArgumentException("Data must not be null or empty");
    }

    if (windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException("Window size must be greater than 0 and less than or equal to the data size");
    }

    final List<Double> movingAverages = new ArrayList<>();
    double currentSum = 0;
    for (int i = 0; i < windowSize; i++) {
      currentSum += data.get(i);
    }
    movingAverages.add(currentSum / windowSize);

    for (int i = windowSize; i < data.size(); i++) {
      currentSum += data.get(i) - data.get(i - windowSize);
      movingAverages.add(currentSum / windowSize);
    }

    return movingAverages;
  }

}
