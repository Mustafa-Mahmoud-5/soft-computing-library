package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class ReLUActivationFunction implements ActivationFunction {

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
        if (y > 0) return 1.0;
        return 0.0;
    }

    private double calculateFx(double x) {
        if (x > 0) return x;
        return 0.0;
    }
}
