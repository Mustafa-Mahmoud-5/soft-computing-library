package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class SigmoidActivationFunction implements ActivationFunction {

    @Override
    public List<Double> calculateFxForLayer(List<Double> nets) {
        List<Double> outputs = new ArrayList<>();
        for (Double netVal : nets) {
            outputs.add(this.calculateFx(netVal));
        }
        return outputs;
    }

    @Override
    public double calculateDerivative(double y) {
        return y * (1 - y);
    }

    private double calculateFx(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }
}
