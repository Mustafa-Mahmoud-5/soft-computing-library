package org.example.lib.neuralnetwork.initialization.impl;

import org.example.lib.neuralnetwork.initialization.WeightInitializationStrategy;

import java.util.Random;

public class HeInitialization implements WeightInitializationStrategy {
    private final Random random = new Random();
    @Override
    public double initializeWeight(int prevLayerSize) {
        double gaussian = random.nextGaussian();
        return gaussian * Math.sqrt(2.0 / prevLayerSize);
    }
}
