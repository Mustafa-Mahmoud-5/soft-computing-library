package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import java.util.Map;

public class MeanOfMax implements Defuzzier {


    @Override
    public double defuzzify(Map<String, Double> membershipValues, FuzzyVariable output) {
        if (membershipValues == null || output == null) {
            throw new IllegalArgumentException("membershipValues and output cannot be null");
        }
        if (membershipValues.isEmpty() || output.getFuzzySets().isEmpty()) {
            return 0.0;
        }

        // Step 1: Find max membership value
        double maxMu = Double.NEGATIVE_INFINITY;
        for (double mu : membershipValues.values()) {
            if (Double.isFinite(mu) && mu > maxMu) maxMu = mu;
        }

        if (maxMu <= 0 || maxMu == Double.NEGATIVE_INFINITY) {
            // No activation — return midpoint of output domain
            return (output.getMin() + output.getMax()) / 2.0;
        }

        // Step 2: Collect all fuzzy set values corresponding to max membership
        double sum = 0.0;
        int count = 0;
        final double EPS = 1e-12;
        for (Map.Entry<String, Double> entry : membershipValues.entrySet()) {
            String setName = entry.getKey();
            double mu = entry.getValue();
            if (Math.abs(mu - maxMu) <= EPS) {
                // Get the representative value of the fuzzy set
                FuzzySet set = output.getFuzzySet(setName);
                if (set != null) {
                     double centroid = output.getFuzzySet(setName).getMembershipFunction().CalcCentroid(); 
                     sum += centroid;
                     count++;
                }
            }
        }

        if (count == 0) return (output.getMin() + output.getMax()) / 2.0;
        return sum / count;
    }
    }

