package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

public class LinearActivationFunction implements ActivationFunction {
    @Override
    public double calculateFx(double x) {
        return x;
    }

    @Override
    public double calculateDerivative(double y) {
        return 1;
    }
}
