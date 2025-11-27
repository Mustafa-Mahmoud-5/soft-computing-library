package org.example.lib.FuzzyLogic.Inference.InferenceEngine;

import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;

import java.util.List;
import java.util.Map;

public interface InferenceEngine {
    void setRules(List<Rule> rules);
    Map<String, Double> infer(List<FuzzyVariable> inputs, FuzzyVariable output);
}
