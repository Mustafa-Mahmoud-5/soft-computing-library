package org.example.lib.nncasestudy;

import org.example.lib.neuralnetwork.FeedForwardNeuralNetwork;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_TYPE;
import org.example.lib.neuralnetwork.layer.LayerConfig;

import java.util.List;

public class main {
    public static void main(String[] args) {
        FeedForwardNeuralNetwork nn = new FeedForwardNeuralNetwork();

        nn.setInputSize(3);
        nn.setEpochs(5);
        nn.setWeightInitializationStrategy(WEIGHT_INITIALIZATION_TYPE.RANDOM);
        List<LayerConfig> layers = List.of(
                new LayerConfig(5, ACTIVATION_FUNCTION_TYPE.SIGMOID)
        );
        nn.setHiddenLayersConfig(layers);
        nn.initializeNetwork();
        nn.printLayers();
    }
}
