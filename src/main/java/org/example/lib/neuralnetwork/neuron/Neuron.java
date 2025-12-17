package org.example.lib.neuralnetwork.neuron;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Neuron {
    int netInput; // net: sum(weight*aPrev)
    int output; // a: from activation function
    int deltaError; // δ
}
