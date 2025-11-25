package org.example.lib.FuzzyLogic.Inference.Operators.Impl;

import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.OrOperator;

public class MaxOrOperator implements OrOperator {
    @Override
    public double apply(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return 0.0;
        return Math.max(a, b);
    }
}
