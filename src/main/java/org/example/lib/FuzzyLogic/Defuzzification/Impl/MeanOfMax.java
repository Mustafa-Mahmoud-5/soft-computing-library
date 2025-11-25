package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;

public class MeanOfMax implements Defuzzier {
    @Override
    public double defuzzify(double[] xValues, double[] membershipValues) {
        if (xValues == null || membershipValues == null) {
            throw new IllegalArgumentException("xValues and membershipValues cannot be null");
        }
        if (xValues.length != membershipValues.length) {
            throw new IllegalArgumentException("xValues and membershipValues must have the same length");
        }

        double maxMu = Double.NEGATIVE_INFINITY;
        for (double mu : membershipValues) {
            if (Double.isFinite(mu) && mu > maxMu) maxMu = mu;
        }

        if (maxMu <= 0 || maxMu == Double.NEGATIVE_INFINITY) {
            // no activation — return midpoint of domain
            if (xValues.length == 0) return 0.0;
            double min = xValues[0], max = xValues[0];
            for (double v : xValues) { if (v < min) min = v; if (v > max) max = v; }
            return (min + max) / 2.0;
        }

        double sum = 0.0;
        int count = 0;
        final double EPS = 1e-12;
        for (int i = 0; i < xValues.length; i++) {
            double mu = membershipValues[i];
            if (Math.abs(mu - maxMu) <= EPS) {
                sum += xValues[i];
                count++;
            }
        }

        if (count == 0) return xValues.length > 0 ? xValues[0] : 0.0;
        return sum / count;
    }
}
