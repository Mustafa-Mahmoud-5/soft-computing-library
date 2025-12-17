package org.example.lib.neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import org.example.lib.datahelper.DataRow;
import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;
import org.example.lib.neuralnetwork.activationfunction.impl.SoftMaxActivationFunction;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.LOSS_FUNCTION_STRATEGY;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_TYPE;
import org.example.lib.neuralnetwork.initialization.WeightInitializationStrategy;
import org.example.lib.neuralnetwork.layer.Layer;
import org.example.lib.neuralnetwork.layer.computationstrategy.OutputLayerComputationStrategy;
import org.example.lib.neuralnetwork.lossfunction.LossFunction;
import org.example.lib.neuralnetwork.lossfunction.impl.CategoricalCrossEntropyLoss;

import java.util.List;

public class FeedForwardNeuralNetwork {
    private List<Double> epochsTrainingLoss;
    private List<Layer> layers;
    private ActivationFunction activationFunction;
    private LossFunction lossFunction;
    private ActivationFunction outputLayerActivationFunction;
    @Getter
    @Setter
    private double learningRate = .1;
    @Getter
    @Setter
    private double epochs = 50;
    @Getter
    @Setter
    private List<Integer> hiddenLayersConfig; //[#nodes in layer 1, #nodes in layer2]
    @Getter
    @Setter
    int inputSize;
    @Getter
    @Setter
    private ACTIVATION_FUNCTION_TYPE activationFunctionType;
    @Getter
    @Setter
    private ACTIVATION_FUNCTION_TYPE outputLayerActivationFunctionType;
    @Getter
    @Setter
    private OutputLayerComputationStrategy outputLayerComputationStrategy;
    @Getter
    @Setter
    private LOSS_FUNCTION_STRATEGY lossFunctionStrategy;
    @Getter
    @Setter
    private WEIGHT_INITIALIZATION_TYPE weightInitializationType;
    @Getter
    @Setter
    WeightInitializationStrategy weightInitializationStrategy;


    public void initializeNetwork() {
        int prevSize = inputSize;
        for(int i = 0; i < hiddenLayersConfig.size(); i++) {
            Layer layer = new Layer(i, prevSize);
            layer.initialize(weightInitializationStrategy);
            layers.add(layer);
            prevSize = i;
        }
    }

    private List<Double> applyForwardPropagation() {
        return List.of();
    }

    private List<Double> applyBackwardPropagation() {
        return List.of();
    }

    private double calculateDeltaInOutputLayer(double predicted, double target, double neuronOut) {
        // if activation function is softMax and lossFunction is CrossEntropy
        if (outputLayerActivationFunction instanceof SoftMaxActivationFunction
                && lossFunction instanceof CategoricalCrossEntropyLoss) {
            return predicted - target;
        } else {
            return outputLayerActivationFunction.calculateDerivative(neuronOut)
                    * lossFunction.calculateDerivative(predicted, target);
        }
    }

    public double train(List<DataRow> dataset) {
        return 0;
    }

    public double predict(List<DataRow> dataset) {
        return 0;
    }
}
