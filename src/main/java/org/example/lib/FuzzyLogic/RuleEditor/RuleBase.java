package org.example.lib.FuzzyLogic.RuleEditor;

import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.Utils.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuleBase {
    private List<Rule> rules;

    public RuleBase() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        Validation.validateNotNull(rule, "rule");
        Validation.validateRuleCompleteness(rule);
        rules.add(rule);
    }

    public void removeRule(Rule rule) {
        Validation.validateNotNull(rule, "rule");
        rules.remove(rule);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule findRuleById(int id) {
        return rules.stream()
                .filter(rule -> rule.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Rule> getEnabledRules() {
        return rules.stream()
                .filter(Rule::isEnabled)
                .collect(Collectors.toList());
    }

    public void clearAll() {
        rules.clear();
    }

    public int getRuleCount() {
        return rules.size();
    }
}
