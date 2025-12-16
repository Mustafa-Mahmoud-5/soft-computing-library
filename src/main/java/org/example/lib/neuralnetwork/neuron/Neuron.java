package org.example.lib.neuralnetwork.neuron;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Neuron {
    int input;
    int output;
    int error;
}
