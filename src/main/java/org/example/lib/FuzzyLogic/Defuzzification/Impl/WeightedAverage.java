package org.example.lib.FuzzyLogic.Defuzzification.Impl;

import java.util.HashMap;
import java.util.Map;

import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipCentroidUtils;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;

public class WeightedAverage implements Defuzzier {
   /**
     * Defuzzify using weighted average of centroids.
     * 
     * @param xValues    the centroids of the output fuzzy sets (pre-computed analytical centroids)
     * @param membershipValues  the aggregated membership degrees (from rule implication and aggregation)
     * @return the crisp output value as the weighted average of centroids
     */
    @Override
    public double defuzzify(Map<String, Double> membershipValues, FuzzyVariable output) {
        if (membershipValues == null || membershipValues.isEmpty()) return 0.0;

        // Compute centroids for all fuzzy sets from the output variable
        Map<String, Double> centroids = new HashMap<>();
        for (Map.Entry<String, FuzzySet> entry : output.getFuzzySets().entrySet()) {
            String setName = entry.getKey();
            FuzzySet set = entry.getValue();
            centroids.put(setName, computeCentroidForSet(set, output.getMin(), output.getMax(), 101));
        }

        double numerator = 0.0;
        double denominator = 0.0;

        for (Map.Entry<String, Double> e : membershipValues.entrySet()) {
            String setName = e.getKey();
            double membership = e.getValue() == null ? 0.0 : e.getValue();
            Double centroid = centroids.get(setName);
            if (centroid == null || Double.isNaN(centroid) || Double.isNaN(membership)) continue;
            numerator += centroid * membership;
            denominator += membership;
        }

        if (denominator == 0.0 || Double.isNaN(denominator)) return 0.0;
        return numerator / denominator;
    }

    private double computeCentroidForSet(FuzzySet fuzzySet, double min, double max, int sampleCount) {
        if (fuzzySet.getMembershipFunction() == null) {
            return (min + max) / 2.0;
        }
        var mf = fuzzySet.getMembershipFunction();
        if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular) {
            org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular t =
                    (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular) mf;
            return MembershipCentroidUtils.triangularCentroid(t.getA(), t.getB(), t.getC());
        } else if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal) {
            org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal tr =
                    (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal) mf;
            return MembershipCentroidUtils.trapezoidalCentroid(tr.getA(), tr.getB(), tr.getC(), tr.getD());
        } else if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian) {
            org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian g =
                    (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian) mf;
            return MembershipCentroidUtils.gaussianCentroid(g.getC(), g.getSigma());
        }
        return MembershipCentroidUtils.computeCentroid(mf, min, max, sampleCount);
    }
}
