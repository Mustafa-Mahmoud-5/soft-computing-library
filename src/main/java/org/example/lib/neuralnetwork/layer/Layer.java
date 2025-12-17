package org.example.lib.neuralnetwork.layer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.lib.neuralnetwork.initialization.WeightInitializationStrategy;
import org.example.lib.neuralnetwork.neuron.Neuron;

import java.util.ArrayList;
import java.util.List;


public class Layer {
    @Setter @Getter
    int size;
    @Setter @Getter
    int prevLayerSize;
    @Getter
    List<Neuron> neurons;
    @Getter
    List<List<Double>> weightMatrix;

    public Layer(int size, int prevSize) {
        this.size = size;
        this.prevLayerSize = prevSize;
    }

    public void initialize(WeightInitializationStrategy weightStrategy) {
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(0, 0, 0));
        }

        if (prevLayerSize > 0) {
            for (int i = 0; i < size; i++) {
                List<Double> weightsForNeuron = new ArrayList<>();
                for (int j = 0; j < prevLayerSize; j++) {
                    double weight = weightStrategy.initializeWeight(prevLayerSize);
                    weightsForNeuron.add(weight);
                }
                weightMatrix.add(weightsForNeuron);
            }
        }
    }
}
