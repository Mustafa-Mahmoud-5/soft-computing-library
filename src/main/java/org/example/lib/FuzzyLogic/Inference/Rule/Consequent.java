package org.example.lib.FuzzyLogic.Inference.Rule;

import org.example.lib.FuzzyLogic.Utils.Validation;

public class Consequent {

    private final String variableName;
    private final String fuzzySetName;

    public Consequent(String variableName, String fuzzySetName) {
        Validation.validateString(variableName, "variableName");
        Validation.validateString(fuzzySetName, "fuzzySetName");

        this.variableName = variableName;
        this.fuzzySetName = fuzzySetName;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getFuzzySetName() {
        return fuzzySetName;
    }
}
