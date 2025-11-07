package org.example.lib.FuzzyLogic.Inference.InferenceEngine;

import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;

import java.util.List;

public interface InferenceEngine {
    void setRules(List<Rule> rules);
    void infer(List<FuzzyVariable> inputs, FuzzyVariable output);
}
