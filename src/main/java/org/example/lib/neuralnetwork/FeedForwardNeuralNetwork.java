package org.example.lib.neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import org.example.lib.datahelper.DataRow;
import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;
import org.example.lib.neuralnetwork.activationfunction.impl.LinearActivationFunction;
import org.example.lib.neuralnetwork.activationfunction.impl.ReLUActivationFunction;
import org.example.lib.neuralnetwork.activationfunction.impl.SigmoidActivationFunction;
import org.example.lib.neuralnetwork.activationfunction.impl.SoftMaxActivationFunction;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;
import org.example.lib.neuralnetwork.enums.LOSS_FUNCTION_STRATEGY;
import org.example.lib.neuralnetwork.enums.WEIGHT_INITIALIZATION_TYPE;
import org.example.lib.neuralnetwork.initialization.WeightInitializationStrategy;
import org.example.lib.neuralnetwork.initialization.impl.HeInitialization;
import org.example.lib.neuralnetwork.initialization.impl.RandomUniformInitialization;
import org.example.lib.neuralnetwork.layer.Layer;
import org.example.lib.neuralnetwork.layer.LayerConfig;
import org.example.lib.neuralnetwork.lossfunction.LossFunction;

import java.util.ArrayList;
import java.util.List;

public class FeedForwardNeuralNetwork {
    private List<Double> epochsTrainingLoss;
    private List<Layer> layers = new ArrayList<>();
    private LossFunction lossFunction;
    @Getter
    @Setter
    private double learningRate = .1;
    @Getter
    @Setter
    private double epochs = 50;
    @Getter
    @Setter
    private List<LayerConfig> hiddenLayersConfig; //[#nodes in layer 1, #nodes in layer2]
    @Getter
    @Setter
    int inputSize;
    @Getter
    private LOSS_FUNCTION_STRATEGY lossFunctionStrategy;
    @Getter
    WeightInitializationStrategy weightInitializationStrategy;


    public void initializeNetwork() {
        int prevSize = inputSize; // the size of the firstt layer depends on the input given by user

        for(int i = 0; i < hiddenLayersConfig.size(); i++) {
            LayerConfig config = hiddenLayersConfig.get(i);
            int layerSize = config.getNeuronsCount();
            ActivationFunction layerActivationFunc = initializeActivationType(config.getActivationFunctionType());

            Layer layer = new Layer(layerSize, prevSize, layerActivationFunc);
            layer.initialize(weightInitializationStrategy);
            layers.add(layer);

            prevSize = layerSize;
        }
    }

    public void printLayers() {
        for(Layer layer: layers) {
            System.out.println(layer.toString());
        }
//        for(Layer layer: layers) {
//            for(int i = 0; i < layer.getWeightMatrix().size(); i++) {
//                System.out.println("NeuronWeights #" + i);
//                System.out.println(layer.getWeightMatrix().get(i));
//                for(int j = 0; j < layer.getWeightMatrix().get(i).size(); j++) {
//                    System.out.println(layer.getWeightMatrix().get(i).get(j));
//                }
//            }
//        }
    }

    private List<Double> applyForwardPropagation() {
        return List.of();
    }

    private List<Double> applyBackwardPropagation() {
        return List.of();
    }

//    private double calculateDeltaInOutputLayer(double predicted, double target, double neuronOut) {
//        // if activation function is softMax and lossFunction is CrossEntropy
//        if (outputLayerActivationFunction instanceof SoftMaxActivationFunction
//                && lossFunction instanceof CategoricalCrossEntropyLoss) {
//            return predicted - target;
//        } else {
//            return outputLayerActivationFunction.calculateDerivative(neuronOut)
//                    * lossFunction.calculateDerivative(predicted, target);
//        }
//    }



    private ActivationFunction initializeActivationType(ACTIVATION_FUNCTION_TYPE type) {
        switch (type) {
            case LINEAR -> {
                return new LinearActivationFunction();
            }
            case RELU -> {
                return new ReLUActivationFunction();
            }
            case SIGMOID -> {
                return new SigmoidActivationFunction();
            }
            case SOFT_MAX -> {
                return new SoftMaxActivationFunction();
            }
            default -> throw new IllegalArgumentException("Unsupported ActivationFunction type: " + type);
        }
    }


    public void setWeightInitializationStrategy (WEIGHT_INITIALIZATION_TYPE type) {
        switch (type) {
            case RANDOM -> this.weightInitializationStrategy = new RandomUniformInitialization();
            case HE -> this.weightInitializationStrategy = new HeInitialization();
            default -> throw new IllegalArgumentException("Unsupported Weight initialization strategy: " + type);
        }
    }

    public double train(List<DataRow> dataset) {
        return 0;
    }

    public double predict(List<DataRow> dataset) {
        return 0;
    }
}
