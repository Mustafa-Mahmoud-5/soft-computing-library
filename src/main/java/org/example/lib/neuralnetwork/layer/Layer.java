package org.example.lib.neuralnetwork.layer;

import lombok.Getter;
import lombok.Setter;
import org.example.lib.neuralnetwork.neuron.Neuron;

import java.util.List;

public class Layer {
    @Setter @Getter
    int size;
    @Setter @Getter
    int prevLayerSize;
    @Getter
    List<Neuron> nodes;
    @Getter
    List<List<Double>> weightMatrix;
}
