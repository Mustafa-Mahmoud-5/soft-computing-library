package org.example.lib.neuralnetwork.activationfunction.impl;

import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class SoftMaxActivationFunction implements ActivationFunction {

    // Formula: softmax(net[k]) = e^(net[k] - max(nets)) / summation(e^ (net[i] - max(nets))
    @Override
    public List<Double> calculateFxForLayer(List<Double> nets) {
        List<Double> outputs = new ArrayList<>();


        double max = nets.get(0);
        for (Double v : nets) {
            if (v > max) max = v;
        }

        double sum = 0;
        for (Double v : nets) {
            sum += Math.exp(v - max); // math.exp is euler
        }

        for (Double v : nets) {
            outputs.add(Math.exp(v - max) / sum);
        }

        return outputs;
    }

    @Override
    public double calculateDerivative(double y) {
        throw new UnsupportedOperationException("derivative of softmax is Unsupported operation");
    }
}
