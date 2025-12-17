package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

public class ReLUActivationFunction implements ActivationFunction {
    @Override
    public double calculateFx(double x) {
        if(x > 0) return x;
        return 0.0;
    }

    @Override
    public double calculateDerivative(double y) {
        if(y > 0) return 1;
        return 0.0;
    }
}
