package org.example.lib.FuzzyLogic.Defuzzification;

import java.util.Map;

import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;

public interface Defuzzier {
    
    double defuzzify(Map<String, Double> membershipValues, FuzzyVariable output);
}
