package org.example.lib.neuralnetwork.initialization.impl;

import org.example.lib.neuralnetwork.initialization.WeightInitializationStrategy;

import java.util.Random;

public class RandomUniformInitialization implements WeightInitializationStrategy {
    private final Random random = new Random();
    private final double min = -0.5;
    private final double max = 0.5;

    @Override
    public double initializeWeight(int prevLayerSize) {
        return random.nextDouble(min, max);
    }
}