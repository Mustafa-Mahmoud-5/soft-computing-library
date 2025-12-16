package org.example.lib.neuralnetwork.lossfunction;

import java.util.List;

public interface LossFunction {
    // after finishing the forward propagation, and calculating the predicted outputs for all
    // neurons in the output layer, we pass them along with target values to calculate loss
    public double calculate(List<Double> predicted, List<Double> target);

    // we use it to calculate the derivative for each neuron in the backpropagation phase
    public double calculateDerivative(double predicted, double target);
}
