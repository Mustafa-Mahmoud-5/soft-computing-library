package org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl;

import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.InferenceEngine;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.Inference.Rule.Condition;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.AndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.OrOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MinAndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MaxOrOperator;
import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;
import org.example.lib.FuzzyLogic.Defuzzification.Impl.Centroid;
import org.example.lib.FuzzyLogic.Defuzzification.Impl.WeightedAverage;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipCentroidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mamdani implements InferenceEngine {
    private List<Rule> rules = new ArrayList<>();
    private AndOperator andOperator = new MinAndOperator();
    private OrOperator orOperator = new MaxOrOperator();
    private boolean productImplication = false; // default: min-implication
    private Defuzzier defuzzier = new WeightedAverage();
    private int sampleCount = 101;

    // debug / introspection
    private double[] lastXSamples = new double[0];
    private double[] lastAggregated = new double[0];
    private Map<Integer, Double> lastRuleFirings = new HashMap<>();

    @Override
    public void setRules(List<Rule> rules) {
        this.rules = rules == null ? new ArrayList<>() : new ArrayList<>(rules);
    }

    @Override
    public void infer(List<FuzzyVariable> inputs, FuzzyVariable output) {
        if (output == null) throw new IllegalArgumentException("output variable cannot be null");
        if (inputs == null) throw new IllegalArgumentException("inputs cannot be null");

        if (output.getFuzzySets().isEmpty()) {
            throw new IllegalArgumentException("output variable must have at least one fuzzy set");
        }

        // Prepare sampling grid for centroid computation
        double min = output.getMin();
        double max = output.getMax();
        int n = Math.max(2, sampleCount);//101
        double[] xs = new double[n];// 0=> 101
        double step = (max - min) / (n - 1);
        for (int i = 0; i < n; i++) {
            xs[i] = min + i * step;
        }

        // Initialize aggregated membership for each output fuzzy set
        int numFuzzySets = output.getFuzzySets().size();
        double[] aggregated = new double[numFuzzySets];
        double[] centroids = new double[numFuzzySets];
        
        // Precompute centroids for each output fuzzy set using analytic formulas when available
        int idx = 0;
        for (org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet fuzzySet : output.getFuzzySets().values()) {
            org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction mf = fuzzySet.getMembershipFunction();

            double centroid;
            if (mf == null) {
                centroid = (min + max) / 2.0;
            } else if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular) {
                org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular t =
                        (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular) mf;
                centroid = MembershipCentroidUtils.triangularCentroid(t.getA(), t.getB(), t.getC());
            } else if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal) {
                org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal tr =
                        (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal) mf;
                centroid = MembershipCentroidUtils.trapezoidalCentroid(tr.getA(), tr.getB(), tr.getC(), tr.getD());
            } else if (mf instanceof org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian) {
                org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian g =
                        (org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Guassian) mf;
                centroid = MembershipCentroidUtils.gaussianCentroid(g.getC(), g.getSigma());
            } else {
                // fallback to numeric sampling-based centroid  count sample n
                centroid = MembershipCentroidUtils.computeCentroid(mf, min, max, n);
            }

            centroids[idx] = centroid;
            aggregated[idx] = 0.0;
            idx++;
        }

        lastRuleFirings.clear();

        // evaluate each rule
        for (Rule rule : rules) {
            if (!rule.isEnabled()) continue;

            // compute antecedent value
            List<Condition> conditions = rule.getAntecedent();
            if (conditions == null || conditions.isEmpty()) continue;

            double firing = 0.0;
            for (int i = 0; i < conditions.size(); i++) {
                Condition cond = conditions.get(i);
                // find corresponding input variable
                FuzzyVariable var = inputs.stream()
                        .filter(v -> v.getName().equals(cond.getVariableName()))
                        .findFirst()
                        .orElse(null);
                double crisp = (var == null || var.getValue() == null) ?
                        ((var == null) ? 0.0 : (var.getMin() + var.getMax()) / 2.0) : var.getValue();

                double mu = 0.0;
                if (var != null) {
                    org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet set = var.getFuzzySet(cond.getFuzzySetName());
                    if (set != null && set.getMembershipFunction() != null) {
                        mu = set.getMembershipFunction().compute(crisp);
                    }
                }

                if (i == 0) firing = mu;
                else {
                    if (cond.getOperator() == org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator.AND) {
                        firing = andOperator.apply(firing, mu);
                    } else if (cond.getOperator() == org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator.OR) {
                        firing = orOperator.apply(firing, mu);
                    } else {
                        // should not happen due to validation
                        firing = mu;
                    }
                }
            }

            // apply rule weight
            firing = firing * rule.getWeight();
            lastRuleFirings.put(rule.getId(), firing);

            // implication + aggregation for the corresponding output fuzzy set
            String outSetName = rule.getConsequent().getFuzzySetName();
            int outSetIdx = 0;
            boolean found = false;
            for (String setName : output.getFuzzySets().keySet()) {
                if (setName.equals(outSetName)) {
                    found = true;
                    break;
                }
                outSetIdx++;
            }
            
            if (found && outSetIdx < numFuzzySets) {
                // Aggregate firing degree (max aggregation per fuzzy set)
                if (firing > aggregated[outSetIdx]) {
                    aggregated[outSetIdx] = firing;
                }
            }
        }

        // save last arrays for debug display (sample-based)
        double[] lastAgg = new double[n];
        for (int i = 0; i < Math.min(n, numFuzzySets); i++) {
            lastAgg[i] = aggregated[i];
        }
        lastXSamples = Arrays.copyOf(xs, xs.length);
        lastAggregated = Arrays.copyOf(lastAgg, lastAgg.length);

        // defuzzify using center of sums with analytical centroids
        double crisp = defuzzier.defuzzify(centroids, aggregated);
        output.setValue(crisp);
    }

    // setters for customization
    public void setAndOperator(AndOperator andOperator) { this.andOperator = andOperator; }
    public void setOrOperator(OrOperator orOperator) { this.orOperator = orOperator; }
    public void setProductImplication(boolean productImplication) { this.productImplication = productImplication; }
    public void setDefuzzier(Defuzzier defuzzier) { if (defuzzier != null) this.defuzzier = defuzzier; }
    public void setSampleCount(int sampleCount) { if (sampleCount >= 2) this.sampleCount = sampleCount; }

    // debug accessors
    public double[] getLastXSamples() { return Arrays.copyOf(lastXSamples, lastXSamples.length); }
    public double[] getLastAggregated() { return Arrays.copyOf(lastAggregated, lastAggregated.length); }
    public Map<Integer, Double> getLastRuleFirings() { return new HashMap<>(lastRuleFirings); }
}
