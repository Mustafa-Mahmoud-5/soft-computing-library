package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;

/**
 * Weighted Average (Center of Sums) Defuzzification Method.
 *
 * This method computes: z* = Σ μ_c * centroid_c / Σ μ_c
 * where μ_c is the aggregated membership value of each fuzzy set
 * and centroid_c is the geometric center of that fuzzy set.
 *
 * Note: This implementation expects xValues to represent centroids of fuzzy sets
 * and membershipValues to represent their aggregated membership degrees.
 * This is fundamentally different from sampling-based centroid:
 * - Sampling-based: approximates Σ x * μ(x) / Σ μ(x) by sampling continuous μ.
 * - Weighted average: uses Σ (centroid * aggregated_mu) / Σ aggregated_mu.
 *
 * Usage: After aggregation, compute the centroid of each output fuzzy set,
 * then pass those centroids and their membership values here.
 */
public class CenterOfSums implements Defuzzier {

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
            double centroid = xValues[i];        // centroid of fuzzy set i
            double mu = membershipValues[i];    // aggregated membership of set i
            if (Double.isFinite(centroid) && Double.isFinite(mu)) {
                numerator += centroid * mu;
                denominator += mu;
            }
        }

        if (denominator == 0.0) {
            // No activation — return midpoint of domain
            if (xValues.length == 0) return 0.0;
            double min = xValues[0], max = xValues[0];
            for (double v : xValues) {
                if (v < min) min = v;
                if (v > max) max = v;
            }
            return (min + max) / 2.0;
        }

        return numerator / denominator;
    }
}
