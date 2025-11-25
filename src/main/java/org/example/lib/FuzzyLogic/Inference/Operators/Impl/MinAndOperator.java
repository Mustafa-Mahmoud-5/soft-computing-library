package org.example.lib.FuzzyLogic.Inference.Operators.Impl;

import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.AndOperator;

public class MinAndOperator implements AndOperator {
    @Override
    public double apply(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return 0.0;
        return Math.min(a, b);
    }
}
