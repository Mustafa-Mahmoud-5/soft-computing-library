package org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl;

import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.InferenceEngine;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MaxOrOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MinAndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.AndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.OrOperator;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.Inference.Rule.Condition;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mamdani implements InferenceEngine {

    private List<Rule> rules = new ArrayList<>();
    private AndOperator andOperator = new MinAndOperator();
    private OrOperator orOperator = new MaxOrOperator();

    private Map<Integer, Double> lastRuleFirings = new HashMap<>();
    private Map<String, Double> lastAggregated = new HashMap<>();

    @Override
    public void setRules(List<Rule> rules) {
        if (rules == null) throw new IllegalArgumentException("rules cannot be null");
        this.rules = new ArrayList<>(rules);
    }


  @Override
    public Map<String, Double> infer(List<FuzzyVariable> inputs, FuzzyVariable output) {
        if (output == null) throw new IllegalArgumentException("output variable cannot be null");
        if (inputs == null) throw new IllegalArgumentException("inputs cannot be null");
        if (output.getFuzzySets().isEmpty()) {
            throw new IllegalArgumentException("output variable must have at least one fuzzy set");
        }

        // Step 1: Initialize aggregation storage keyed by fuzzy set name
        lastAggregated.clear();
        for (String setName : output.getFuzzySets().keySet()) {
            lastAggregated.put(setName, 0.0);
        }

        lastRuleFirings.clear();

        // Step 2: Evaluate each rule
        for (Rule rule : rules) {
            if (!rule.isEnabled()) continue;

            // Fuzzification: Compute antecedent firing strength
            double firing = computeAntecedentFiring(rule, inputs);
            
            // Apply rule weight
            firing = firing * rule.getWeight();
            lastRuleFirings.put(rule.getId(), firing);

            // Step 3: Aggregation - accumulate firing degrees per output fuzzy set (by name)
            String outSetName = rule.getConsequent().getFuzzySetName();
            if (outSetName != null && lastAggregated.containsKey(outSetName)) {
                double currentAgg = lastAggregated.getOrDefault(outSetName, 0.0);
                lastAggregated.put(outSetName, Math.max(currentAgg, firing));
            }
        }

        return new HashMap<>(lastAggregated);
    }
    
    /**
     * Compute the firing strength of a rule's antecedent (fuzzification + logic)
     */
    private double computeAntecedentFiring(Rule rule, List<FuzzyVariable> inputs) {
        List<Condition> conditions = rule.getAntecedent();
        if (conditions == null || conditions.isEmpty()) return 0.0;

        double firing = 0.0;
        for (int i = 0; i < conditions.size(); i++) {
            Condition cond = conditions.get(i);
            
            // Find input variable
            FuzzyVariable var = inputs.stream()
                    .filter(v -> v.getName().equals(cond.getVariableName()))
                    .findFirst()
                    .orElse(null);
            
            // Get crisp value (fuzzification input)
            double crisp = getCrispValue(var);

            // Fuzzification: Compute membership degree
            double mu = computeMembership(var, cond.getFuzzySetName(), crisp);

            // Combine with previous conditions using logical operators
            if (i == 0) {
                firing = mu;
            } else {
                if (cond.getOperator() == org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator.AND) {
                    firing = andOperator.apply(firing, mu);
                } else if (cond.getOperator() == org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator.OR) {
                    firing = orOperator.apply(firing, mu);
                } else {
                    firing = mu;
                }
            }
        }
        
        return firing;
    }
    
    /**
     * Get crisp value from a fuzzy variable
     */
    private double getCrispValue(FuzzyVariable var) {
        if (var == null) return 0.0;
        if (var.getValue() == null) return (var.getMin() + var.getMax()) / 2.0;
        return var.getValue();
    }
    
    /**
     * Compute membership degree for fuzzification
     */
    private double computeMembership(FuzzyVariable var, String fuzzySetName, double crispValue) {
        if (var == null) return 0.0;
        
        org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet set = var.getFuzzySet(fuzzySetName);
        if (set == null || set.getMembershipFunction() == null) return 0.0;
        
        return set.getMembershipFunction().compute(crispValue);
    }

    // Configuration setters
    public void setAndOperator(AndOperator andOperator) {
        this.andOperator = andOperator;
    }
    
    public void setOrOperator(OrOperator orOperator) {
        this.orOperator = orOperator;
    }
    
 

    // Introspection accessors
    public Map<Integer, Double> getLastRuleFirings() {
        return new HashMap<>(lastRuleFirings);
    }
    
    public Map<String, Double> getLastAggregated() {
        return new HashMap<>(lastAggregated);
    }
}