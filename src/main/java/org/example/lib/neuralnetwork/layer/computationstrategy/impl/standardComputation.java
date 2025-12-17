package org.example.lib.neuralnetwork.layer.computationstrategy.impl;

import org.example.lib.neuralnetwork.layer.computationstrategy.OutputLayerComputationStrategy;

import java.util.List;

public class standardComputation implements OutputLayerComputationStrategy {
    @Override
    public List<Double> forward(List<Double> netInputs) {
        return List.of();
    }

    @Override
    public List<Double> backward(List<Double> predicted, List<Double> target) {
        return List.of();
    }
}
