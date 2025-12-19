package org.example.lib.neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import org.example.lib.datahelper.DataRow;
import org.example.lib.datahelper.Utils;
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
import org.example.lib.neuralnetwork.lossfunction.impl.CategoricalCrossEntropyLoss;
import org.example.lib.neuralnetwork.lossfunction.impl.MeanSquaredErrorLoss;
import org.example.lib.neuralnetwork.neuron.Neuron;

import java.util.ArrayList;
import java.util.List;

public class FeedForwardNeuralNetwork {
    private List<Layer> layers = new ArrayList<>();
    private LossFunction lossFunction;
    @Getter
    @Setter
    private double learningRate = .1;
    @Getter
    @Setter
    private double epochs = 50;
    @Getter
    private List<LayerConfig> layersConfig;
    @Getter
    @Setter
    int inputSize;
    @Getter
    private LOSS_FUNCTION_STRATEGY lossFunctionStrategy;
    @Getter
    WeightInitializationStrategy weightInitializationStrategy;


    public void initializeNetwork() {
        int prevSize = inputSize;

        for(int i = 0; i < layersConfig.size(); i++) {
            LayerConfig config = layersConfig.get(i);
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
    }

    public void train(List<DataRow> dataset) {
        if(dataset.get(0).getInputs().size()  != inputSize) {
            throw new IllegalArgumentException("input size doesn't match dataset input size");
        }
        if(dataset.get(0).getTargets().size() != layers.get(layers.size() - 1).getSize()) {
            throw new IllegalArgumentException("target layer size doesn't match dataset output size");
        }

        for (int e = 0; e < epochs; e++) {
            double totalEpochLoss = 0;
            for (DataRow row : dataset) {
                List<Double> outputs = applyForwardPropagation(row.getInputs());
                totalEpochLoss += lossFunction.calculate(outputs, row.getTargets());
                applyBackwardPropagation(row.getTargets());
                updateWeights(row.getInputs());
            }
            double avgLoss = totalEpochLoss / dataset.size();
            System.out.println("Epoch " + e + " Loss: " + avgLoss);
        }
    }

    // compute nets for each layer
    // calculate activation function for each layer neurons
    private List<Double> applyForwardPropagation(List<Double> inputs) {
        List<Double> currentInputs = inputs;

        for (Layer layer : layers) {
            List<Double> layerNets = new ArrayList<>();
            for (int i = 0; i < layer.getSize(); i++) {
                double net = 0;
                List<Double> neuronWeights = layer.getWeightMatrix().get(i);

                for (int j = 0; j < currentInputs.size(); j++) {
                    net += neuronWeights.get(j) * currentInputs.get(j);
                }

                layerNets.add(net);
                layer.getNeurons().get(i).setNetInput(net);
            }

            // output values for all neurons of the layer, update for each neuron
            List<Double> layerOutputs = layer.getActivationFunction().calculateFxForLayer(layerNets);
            for (int i = 0; i < layer.getSize(); i++) {
                layer.getNeurons().get(i).setOutput(layerOutputs.get(i));
            }
            // inputs in next layer is current output of this layer
            currentInputs = layerOutputs;
        }
        return currentInputs;
    }

    private void applyBackwardPropagation(List<Double> targets) {
        // goal is to calculate delta for all nodes in all layers, then apply a weight update to all layers
        Layer outputLayer = layers.get(layers.size() - 1);
        boolean isSoftmaxCrossEntropy = lossFunction instanceof CategoricalCrossEntropyLoss && outputLayer.getActivationFunction() instanceof SoftMaxActivationFunction;
        for (int k = 0; k < outputLayer.getSize(); k++) { // for each neuron
            Neuron neuron = outputLayer.getNeurons().get(k);
            double output = neuron.getOutput();
            double target = targets.get(k);

            double delta = 0;
            if(isSoftmaxCrossEntropy) {
                delta = output - target;
            } else {
                double lossDerivative = lossFunction.calculateDerivative(output, target);
                double activationDerivative = outputLayer.getActivationFunction().calculateDerivative(output);
                delta = lossDerivative * activationDerivative;
            }

            neuron.setDeltaError(delta);
        }




        // Get deltas for hidden Layers
        for (int h = layers.size() - 2; h >= 0; h--) {
            Layer currentLayer = layers.get(h);
            Layer nextLayer = layers.get(h + 1);

            for (int j = 0; j < currentLayer.getSize(); j++) {
                double sumDeltaWeights = 0;
                // delta = summation  of weight kj * delta k
                // multiplied by derivative of activation of this layer's current neuron

                for (int k = 0; k < nextLayer.getSize(); k++) {
                    double nextDelta = nextLayer.getNeurons().get(k).getDeltaError();
                    double weightToNext = nextLayer.getWeightMatrix().get(k).get(j);
                    sumDeltaWeights += nextDelta * weightToNext;
                }

                Neuron neuron = currentLayer.getNeurons().get(j);
                double output = neuron.getOutput();
                double activationDerivative = currentLayer.getActivationFunction()
                        .calculateDerivative(output);

                neuron.setDeltaError(sumDeltaWeights * activationDerivative);
            }
        }
    }

    private void updateWeights(List<Double> originalInputs) {
        List<Double> inputsToLayer = originalInputs;

        // loop from left to right and update all weights
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);

            // loop over nodes
            for (int j = 0; j < layer.getSize(); j++) {
                Neuron neuron = layer.getNeurons().get(j);
                List<Double> weights = layer.getWeightMatrix().get(j);
                // loop over node connected to it from prev layer
                for (int w = 0; w < weights.size(); w++) {
                    double currentWeight = weights.get(w);
                    double inputVal = inputsToLayer.get(w);

                    double newWeight = currentWeight - (learningRate * neuron.getDeltaError() * inputVal);

                    weights.set(w, newWeight);
                }
            }

            // inputs for the next layer are the outputs of the current layer
            List<Double> nextInputs = new ArrayList<>();
            for(Neuron n : layer.getNeurons()) {
                nextInputs.add(n.getOutput());
            }
            inputsToLayer = nextInputs;
        }
    }


    public int predict(DataRow row) {
        if(row.getInputs().size() != inputSize) {
            throw new IllegalArgumentException("input size doesn't match dataset input size");
        }
        List<Double> predicted = applyForwardPropagation(row.getInputs());
        System.out.println("predicted" + predicted);
        return Utils.argMax(predicted);
    }

    public double test(List<DataRow> dataset) {
        int correctTotal = 0;
        for(DataRow row: dataset) {
            List<Double> predicted = applyForwardPropagation(row.getInputs());
            if(Utils.argMax(predicted) == Utils.argMax(row.getTargets())) {
                correctTotal++;
            }
        }
        return (double) correctTotal / dataset.size();
    }

    private ActivationFunction initializeActivationType(ACTIVATION_FUNCTION_TYPE type) {
        switch (type) {
            case LINEAR -> { return new LinearActivationFunction(); }
            case RELU -> { return new ReLUActivationFunction(); }
            case SIGMOID -> { return new SigmoidActivationFunction(); }
            case SOFT_MAX -> { return new SoftMaxActivationFunction(); }
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

    public void setLossFunctionStrategy(LOSS_FUNCTION_STRATEGY type) {
        switch (type) {
            case MEAN_SQUARED_ERROR -> this.lossFunction = new MeanSquaredErrorLoss();
            case CROSS_ENTROPY -> this.lossFunction = new CategoricalCrossEntropyLoss();
            default -> throw new IllegalArgumentException("unsupported loss function strategy");
        }
    }

    public void setLayers(List<LayerConfig> layersConfig) {
        for (int i = 0; i < layersConfig.size() - 1; i++) {
            if(layersConfig.get(i).getNeuronsCount() <= 0) {
                throw new IllegalArgumentException("Layer Neurons must be > 0");
            }
            if(layersConfig.get(i).getActivationFunctionType() ==  ACTIVATION_FUNCTION_TYPE.SOFT_MAX) {
                throw new IllegalArgumentException("Softmax is not supported for middle/hidden layers");
            }
        }
        this.layersConfig = layersConfig;
    }
}