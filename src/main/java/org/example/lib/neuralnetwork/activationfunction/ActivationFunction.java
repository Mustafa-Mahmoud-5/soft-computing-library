package org.example.lib.neuralnetwork.activationfunction;

import java.util.List;

public interface ActivationFunction {
    public List<Double> calculateFxForLayer(List<Double> nets);
    public double calculateDerivative(double y);
}
