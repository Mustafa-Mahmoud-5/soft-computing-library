package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;

public class WeightedAverage implements Defuzzier {
    @Override
    public double defuzzify(double[] xValues, double[] membershipValues) {
        if (xValues == null || membershipValues == null) {
            throw new IllegalArgumentException("xValues and membershipValues cannot be null");
        }
        if (xValues.length != membershipValues.length) {
            throw new IllegalArgumentException("xValues and membershipValues must have the same length");
        }

        double numerator = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double mu = membershipValues[i];
            if (Double.isFinite(x) && Double.isFinite(mu)) {
                numerator += x * mu;
                denominator += mu;
            }
        }

        if (denominator == 0.0) {
            if (xValues.length == 0) return 0.0;
            double min = xValues[0], max = xValues[0];
            for (double v : xValues) { if (v < min) min = v; if (v > max) max = v; }
            return (min + max) / 2.0;
        }

        return numerator / denominator;
    }
}
