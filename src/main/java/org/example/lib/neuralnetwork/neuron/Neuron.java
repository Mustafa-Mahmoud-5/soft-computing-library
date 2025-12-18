package org.example.lib.neuralnetwork.neuron;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Neuron {
    double netInput; // net: sum(weight*aPrev)
    double output; // a: from activation function
    double deltaError; // δ
}
