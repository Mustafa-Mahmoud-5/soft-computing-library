package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;
// this needs to receive all nodes.nets at once in order to calculate their output a
public class SoftMaxActivationFunction implements ActivationFunction {
    @Override
    public double calculateFx(double x) {
        return 0;
    }

    @Override
    public double calculateDerivative(double y) {
        // derivative of softmax requires calculating the Jacobin Matrix
        // instead, if we want to calculate δ in case the activation is softmax with categoricalCrossEntropy
        // the formula becomes δk = predicted - actual
        return 0;
    }
}
