package org.example.lib.FuzzyLogic.RuleEditor;

import org.example.lib.FuzzyLogic.Inference.Rule.Rule;

import java.util.List;

public class RuleBase {
    private List<Rule> rules;

    public void addRule(Rule rule){
        rules.add(rule);
    }

    public void removeRule(Rule rule){
        rules.remove(rule);
    }

    public List<Rule> getRules(){
        return rules;
    }
}
