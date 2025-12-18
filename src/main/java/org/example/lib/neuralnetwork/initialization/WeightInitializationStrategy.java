package org.example.lib.neuralnetwork.initialization;

public interface WeightInitializationStrategy {
    double initializeWeight(int prevLayerSize);
}