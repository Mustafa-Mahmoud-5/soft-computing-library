package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class LinearActivationFunction implements ActivationFunction {
    @Override
    public List<Double> calculateFxForLayer(List<Double> nets) {
        List<Double> outputs = new ArrayList<>();
        for(Double netVal: nets) {
            outputs.add(this.calculateFx(netVal));
        }
        return outputs;
    }

    @Override
    public double calculateDerivative(double y) {
        return 1;
    }

    private double calculateFx(double x) {
        return x;
    }
}
