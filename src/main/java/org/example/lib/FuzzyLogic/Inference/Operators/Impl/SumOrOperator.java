package org.example.lib.FuzzyLogic.Inference.Operators.Impl;

import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.OrOperator;

public class SumOrOperator implements OrOperator {
    @Override
    public double apply(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return 0.0;
        double sum = a + b;
        // keep result within [0,1]
        if (sum > 1.0) return 1.0;
        if (sum < 0.0) return 0.0;
        return sum;
    }
}
