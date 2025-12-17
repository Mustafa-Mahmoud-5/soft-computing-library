package org.example.lib.neuralnetwork.layer.computationstrategy;

import java.util.List;

public interface OutputLayerComputationStrategy {
    List<Double> forward(List<Double> netInputs);
    List<Double> backward(List<Double> predicted, List<Double> target);
}
