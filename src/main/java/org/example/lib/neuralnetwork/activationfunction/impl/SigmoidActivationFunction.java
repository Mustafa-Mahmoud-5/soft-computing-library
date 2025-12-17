package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

public class SigmoidActivationFunction implements ActivationFunction {

    @Override
    public double calculateFx(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

    // out * (1-out)
    @Override
    public double calculateDerivative(double y) {
        return y * (1 - y);
    }
}
