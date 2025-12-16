package org.example.lib.neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import org.example.lib.datahelper.DataRow;
import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.LOSS_FUNCTION_STRATEGY;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_STRATEGY;
import org.example.lib.neuralnetwork.layer.Layer;

import java.util.List;

public class FeedForwardNeuralNetwork{
    private List<Double> epochsTrainingLoss;
    private List<Layer> layers;
    private ActivationFunction activationFunction;

    // config props
    @Getter @Setter
    private double learningRate = .1;
    @Getter @Setter
    private double epochs = 50;
    @Getter @Setter
    private double patchSize;
    @Getter @Setter
    private double[] hiddenLayers;
    @Getter @Setter
    ACTIVATION_FUNCTION_TYPE activationFunctionType;
    @Getter @Setter
    LOSS_FUNCTION_STRATEGY lossFunctionStrategy;
    @Getter @Setter
    WEIGHT_INITIALIZATION_STRATEGY weightInitializationStrategy;

    private void applyForwardPropagation() {

    }

    private void applyBackwardPropagation() {

    }

    public double train(List<DataRow> dataset) {
        return 0;
    }


    public double predict(List<DataRow> dataset) {
        return 0;
    }
}
