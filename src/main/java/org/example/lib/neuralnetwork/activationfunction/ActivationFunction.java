package org.example.lib.neuralnetwork.activationfunction;

public interface ActivationFunction {
    public double calculateFx(int x);
    public double calculateDerivative(int x);
}
