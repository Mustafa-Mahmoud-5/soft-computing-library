package org.example.lib.FuzzyLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Arrays;

import org.example.lib.Algorithm;
import org.example.lib.FuzzyLogic.Defuzzification.Defuzzier;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.InferenceEngine;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.Inference.Rule.Condition;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MaxOrOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Impl.MinAndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.AndOperator;
import org.example.lib.FuzzyLogic.Inference.Operators.Interfaces.OrOperator;


public class FuzzyLogic implements Algorithm {

    private InferenceEngine inferenceEngine;
    private List<FuzzyVariable> inputVariables = new ArrayList<>();
    private FuzzyVariable outputVariable;
    private List<Rule> rules = new ArrayList<>();
    private Defuzzier defuzzifier;
    //private int sampleCount = 101;
    
    // Debug / Introspection
    private Map<String, Double> lastAggregated = new HashMap<>();
    private Map<Integer, Double> lastRuleFirings = new HashMap<>();
    
    /**
     * Execute the complete fuzzy logic pipeline
     */
    @Override
    public void run() {
        if (inferenceEngine == null) {
            throw new IllegalStateException("InferenceEngine must be set");
        }
        if (outputVariable == null) {
            throw new IllegalStateException("Output variable must be set");
        }
        if (inputVariables.isEmpty()) {
            throw new IllegalStateException("At least one input variable must be set");
        }
        if (defuzzifier == null) {
            throw new IllegalStateException("Defuzzifier must be set");
        }
        
        // Step 1: (optional) compute centroids map for introspection

        // Step 2: Run inference engine to get aggregated membership degrees keyed by fuzzy set name
        inferenceEngine.setRules(rules);
        Map<String, Double> aggregated = inferenceEngine.infer(inputVariables, outputVariable);

        // Step 3: Defuzzify using maps. Defuzzifier now computes centroids from `outputVariable`.
        double crispOutput = defuzzifier.defuzzify(aggregated, outputVariable);
        
        // Step 5: Set crisp output value
        outputVariable.setValue(crispOutput);
        
        // Store for introspection
        lastAggregated = new HashMap<>(aggregated);
    }
 

    
    /**
     * Run inference engine and aggregate firing degrees
     */
    // private Map<String, Double> runInferenceAndAggregation(
    //         List<FuzzyVariable> inputs, FuzzyVariable output) {
    //     inferenceEngine.setRules(rules);
    //     return inferenceEngine.infer(inputs, output);
    // }
    
    // Configuration methods
    public void setInferenceEngine(InferenceEngine engine) {
        this.inferenceEngine = engine;
    }
    
    public void setDefuzzifier(Defuzzier defuzzifier) {
        this.defuzzifier = defuzzifier;
    }
    
    public void setInputVariables(List<FuzzyVariable> inputs) {
        this.inputVariables = inputs != null ? new ArrayList<>(inputs) : new ArrayList<>();
    }
    
    public void addInputVariable(FuzzyVariable variable) {
        if (variable != null) {
            this.inputVariables.add(variable);
        }
    }
    
    public void setOutputVariable(FuzzyVariable output) {
        this.outputVariable = output;
    }
    
    public void setRules(List<Rule> rules) {
        this.rules = rules != null ? new ArrayList<>(rules) : new ArrayList<>();
    }
    
    // public void setSampleCount(int sampleCount) {
    //     if (sampleCount >= 2) {
    //         this.sampleCount = sampleCount;
    //     }
    // }
    
    // // Introspection methods
    // public Map<String, Double> getLastCentroids() {
    //     return new HashMap<>(lastCentroids);
    // }
    
    public Map<String, Double> getLastAggregated() {
        return new HashMap<>(lastAggregated);
    }
    
    public Map<Integer, Double> getLastRuleFirings() {
        return new HashMap<>(lastRuleFirings);
    }
    
    public FuzzyVariable getOutputVariable() {
        return outputVariable;
    }
}
