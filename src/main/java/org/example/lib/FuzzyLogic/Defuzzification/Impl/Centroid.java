package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;

/**
 * Centroid (Weighted Average) Defuzzifier using the Center of Sums method.
 * 
 * This implements the weighted average defuzzification technique from fuzzy logic theory:
 * z* = Σ(μ_c * z̄_c) / Σ(μ_c)
 * 
 * where:
 *   - μ_c is the aggregated membership degree for fuzzy set c
 *   - z̄_c is the centroid (center point) of fuzzy set c
 *   - The numerator sums the product of each membership and its corresponding centroid
 *   - The denominator sums all membership degrees
 * 
 * This method is more efficient than sampling-based approaches and works well with
 * symmetrical and non-symmetrical membership functions.
 */
public class Centroid implements Defuzzier {
    // B  x value 
    // M 
    
    /**
     * Defuzzify using weighted average of centroids.
     * 
     * @param xValues    the centroids of the output fuzzy sets (pre-computed analytical centroids)
     * @param membershipValues  the aggregated membership degrees (from rule implication and aggregation)
     * @return the crisp output value as the weighted average of centroids
     */
    @Override
    public double defuzzify(double[] xValues, double[] membershipValues) {
        if (xValues == null || membershipValues == null || xValues.length == 0) {
            return 0.0;
        }
        
        if (xValues.length != membershipValues.length) {
            throw new IllegalArgumentException(
                "xValues (centroids) and membershipValues must have the same length. " +
                "Got " + xValues.length + " centroids and " + membershipValues.length + " membership values."
            );
        }
        
        double numerator = 0.0;
        double denominator = 0.0;
        
        // Compute weighted sum: Σ(centroid * membership)
        for (int i = 0; i < xValues.length; i++) {
            double centroid = xValues[i];
            double membership = membershipValues[i];
            
            // Skip NaN values
            if (Double.isNaN(centroid) || Double.isNaN(membership)) {
                continue;
            }
            
            numerator += centroid * membership;
            denominator += membership;
        }
        
        // Handle zero activation case (no fuzzy sets fired)
        if (denominator == 0.0 || Double.isNaN(denominator)) {
            return 0.0;
        }
        
        return numerator / denominator;
    }
}
