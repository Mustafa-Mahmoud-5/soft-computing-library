package org.example.lib.FuzzyLogic.RuleEditor;

import org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.Utils.Validation;

public class RuleEditor {
    private RuleBase ruleBase;

    public RuleEditor(RuleBase ruleBase) {
        Validation.validateNotNull(ruleBase, "ruleBase");
        this.ruleBase = ruleBase;
    }

    public RuleBuilder createRule() {
        return new RuleBuilder(ruleBase);
    }

    public void enableRule(int ruleId) {
        Rule rule = ruleBase.findRuleById(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Rule with ID " + ruleId + " not found");
        }
        rule.setEnabled(true);
    }

    public void disableRule(int ruleId) {
        Rule rule = ruleBase.findRuleById(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Rule with ID " + ruleId + " not found");
        }
        rule.setEnabled(false);
    }

    public void setRuleWeight(int ruleId, double weight) {
        Validation.validateWeight(weight);
        Rule rule = ruleBase.findRuleById(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Rule with ID " + ruleId + " not found");
        }
        rule.setWeight(weight);
    }

    public void deleteRule(int ruleId) {
        Rule rule = ruleBase.findRuleById(ruleId);
        if (rule == null) {
            throw new IllegalArgumentException("Rule with ID " + ruleId + " not found");
        }
        ruleBase.removeRule(rule);
    }

    public Rule getRule(int ruleId) {
        return ruleBase.findRuleById(ruleId);
    }

    public static class RuleBuilder {
        private Rule rule;
        private RuleBase ruleBase;

        public RuleBuilder(RuleBase ruleBase) {
            Validation.validateNotNull(ruleBase, "ruleBase");
            this.ruleBase = ruleBase;
            this.rule = new Rule();
        }

        public RuleBuilder when(String variableName, String fuzzySetName) {
            rule.addCondition(variableName, fuzzySetName, LogicalOperator.NONE);
            return this;
        }

        public RuleBuilder and(String variableName, String fuzzySetName) {
            rule.addCondition(variableName, fuzzySetName, LogicalOperator.AND);
            return this;
        }

        public RuleBuilder or(String variableName, String fuzzySetName) {
            rule.addCondition(variableName, fuzzySetName, LogicalOperator.OR);
            return this;
        }

        public RuleBuilder then(String variableName, String fuzzySetName) {
            rule.setConsequent(variableName, fuzzySetName);
            return this;
        }

        public RuleBuilder withWeight(double weight) {
            rule.setWeight(weight);
            return this;
        }

        public Rule build() {
            ruleBase.addRule(rule);
            return rule;
        }
    }
}
