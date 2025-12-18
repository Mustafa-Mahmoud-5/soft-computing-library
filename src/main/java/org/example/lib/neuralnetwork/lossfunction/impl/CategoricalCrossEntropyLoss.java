package org.example.lib.neuralnetwork.lossfunction.impl;

import org.example.lib.neuralnetwork.lossfunction.LossFunction;
import java.util.List;

// Categorical (multi-class) Cross Entropy, not the Binary version
public class CategoricalCrossEntropyLoss implements LossFunction {
    private static final double EPSILON = 1e-15;

    // -Summation(actual * log(predicted))
    @Override
    public double calculate(List<Double> predicted, List<Double> target) {
        double sum = 0;
        for (int i = 0; i < predicted.size(); i++) {
            if (target.get(i) > 0) {
                double p = Math.max(EPSILON, Math.min(1 - EPSILON, predicted.get(i)));
                sum += target.get(i) * Math.log(p);
            }
        }
        return -sum;
    }

    // - (target / predicted)
    @Override
    public double calculateDerivative(double predicted, double target) {
        // to avoid divide-by-zero
        double p = Math.max(EPSILON, Math.min(1 - EPSILON, predicted));
        return -(target / p);
    }
}