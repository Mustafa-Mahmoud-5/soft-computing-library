package org.example.lib.FuzzyLogic.Utils;

import org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import java.util.Collection;

public class Validation {

    public static void validateString(String value, String parameterName) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(parameterName + " cannot be empty or whitespace");
        }
    }

    public static void validateWeight(double weight) {
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Weight cannot be NaN");
        }
        if (Double.isInfinite(weight)) {
            throw new IllegalArgumentException("Weight cannot be Infinity");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0, got: " + weight);
        }
    }

    public static void validateFiniteNumber(double value, String parameterName) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(parameterName + " cannot be NaN");
        }
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(parameterName + " cannot be Infinity");
        }
    }

    public static <T> void validateNotNull(T object, String parameterName) {
        if (object == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
    }

    public static <T> void validateNotEmpty(Collection<T> collection, String parameterName) {
        validateNotNull(collection, parameterName);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(parameterName + " cannot be empty");
        }
    }

    public static void validateOperatorSequence(LogicalOperator operator, boolean isFirstCondition) {
        validateNotNull(operator, "operator");

        if (isFirstCondition && operator != LogicalOperator.NONE) {
            throw new IllegalArgumentException("First condition must have operator NONE, got: " + operator);
        }

        if (!isFirstCondition && operator == LogicalOperator.NONE) {
            throw new IllegalArgumentException("Non-first conditions must have operator AND or OR, got: NONE");
        }
    }

    public static void validateRuleCompleteness(Rule rule) {
        validateNotNull(rule, "rule");

        if (rule.getConditions() == null || rule.getConditions().isEmpty()) {
            throw new IllegalStateException("Rule must have at least one condition");
        }

        if (rule.getConsequent() == null) {
            throw new IllegalStateException("Rule must have a consequent");
        }
    }
}
