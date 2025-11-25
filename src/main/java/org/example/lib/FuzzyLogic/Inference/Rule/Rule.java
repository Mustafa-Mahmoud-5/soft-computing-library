package org.example.lib.FuzzyLogic.Inference.Rule;

import org.example.lib.FuzzyLogic.Utils.Validation;
import java.util.ArrayList;
import java.util.List;

public class Rule {

    private static int idCounter = 0;

    private final int id;
    private final List<Condition> conditions;
    private Consequent consequent;
    private double weight;
    private boolean enabled;

    public Rule() {
        this.id = idCounter++;
        this.conditions = new ArrayList<>();
        this.weight = 1.0;
        this.enabled = true;
    }


    public void addCondition(String variableName, String fuzzySetName, LogicalOperator operator) {
        Validation.validateOperatorSequence(operator, conditions.isEmpty());
        Condition condition = new Condition(variableName, fuzzySetName, operator);
        conditions.add(condition);
    }

    public void setConsequent(String variableName, String fuzzySetName) {
        if (this.consequent != null) {
            throw new IllegalStateException("Consequent already set for this rule");
        }
        this.consequent = new Consequent(variableName, fuzzySetName);
    }

    public void setWeight(double weight) {
        Validation.validateWeight(weight);
        this.weight = weight;
    }

    // Alias method used in your code logic
    public List<Condition> getAntecedent() {
        return conditions;
    }


    public static int getIdCounter() {
        return idCounter;
    }

    public int getId() {
        return id;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public Consequent getConsequent() {
        return consequent;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}