package org.example.lib.FuzzyLogic.Inference.Rule;

import org.example.lib.FuzzyLogic.Utils.Validation;

public class Condition {

    private final String variableName;
    private final String fuzzySetName;
    private final LogicalOperator operator;

    public Condition(String variableName, String fuzzySetName, LogicalOperator operator) {
        Validation.validateString(variableName, "variableName");
        Validation.validateString(fuzzySetName, "fuzzySetName");
        Validation.validateNotNull(operator, "operator");

        this.variableName = variableName;
        this.fuzzySetName = fuzzySetName;
        this.operator = operator;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getFuzzySetName() {
        return fuzzySetName;
    }

    public LogicalOperator getOperator() {
        return operator;
    }
}
