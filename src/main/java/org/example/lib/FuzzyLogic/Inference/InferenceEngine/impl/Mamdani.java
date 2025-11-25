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
import org.example.lib.FuzzyLogic.Defuzzification.Impl.WeightedAverage;

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

        // prepare samples across output domain
        double min = output.getMin();
        double max = output.getMax();
        if (!Double.isFinite(min) || !Double.isFinite(max) || max <= min) {
            throw new IllegalArgumentException("output variable must have a valid min < max domain");
        }

        int n = Math.max(2, sampleCount);
        double[] xs = new double[n];
        double[] aggregated = new double[n];
        double step = (max - min) / (n - 1);
        for (int i = 0; i < n; i++) {
            xs[i] = min + i * step;
            aggregated[i] = 0.0;
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

            // implication + aggregation across xs
            String outSetName = rule.getConsequent().getFuzzySetName();
            org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet outSet = output.getFuzzySet(outSetName);
            if (outSet == null || outSet.getMembershipFunction() == null) continue;

            for (int i = 0; i < n; i++) {
                double muOut = outSet.getMembershipFunction().compute(xs[i]);
                double implied = productImplication ? (firing * muOut) : Math.min(firing, muOut);
                if (implied > aggregated[i]) aggregated[i] = implied; // max aggregation
            }
        }

        // save last arrays
        lastXSamples = Arrays.copyOf(xs, xs.length);
        lastAggregated = Arrays.copyOf(aggregated, aggregated.length);

        // defuzzify
        double crisp = defuzzier.defuzzify(xs, aggregated);
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
