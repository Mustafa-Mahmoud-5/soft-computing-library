package org.example.lib.neuralnetwork.layer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.lib.neuralnetwork.activationfunction.ActivationFunction;
import org.example.lib.neuralnetwork.enums.ACTIVATION_FUNCTION_TYPE;

@AllArgsConstructor
@Setter
@Getter
public class LayerConfig {
    private int neuronsCount; // how many neurons in the layer
    private ACTIVATION_FUNCTION_TYPE activationFunctionType;
}
