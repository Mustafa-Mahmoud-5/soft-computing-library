package org.example.lib.neuralnetwork.lossfunction.impl;

import org.example.lib.neuralnetwork.lossfunction.LossFunction;

import java.util.List;

public class MeanSquaredErrorLoss implements LossFunction {
    @Override
    public double calculate(List<Double> predicted, List<Double> target) {
        double sum = 0;
        for (int i = 0; i < predicted.size(); i++) {
            sum += Math.pow(predicted.get(i) - target.get(i), 2);
        }
        return sum/2;
    }

    @Override
    public double calculateDerivative(double predicted, double target) {
        return predicted - target;
    }
}
