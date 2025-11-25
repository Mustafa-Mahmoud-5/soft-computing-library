package org.example.lib.FuzzyLogic.demo;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl.Mamdani;
import org.example.lib.FuzzyLogic.RuleEditor.RuleBase;
import org.example.lib.FuzzyLogic.RuleEditor.RuleEditor;

import java.util.Arrays;
import java.util.List;

public class DemoMalignancy {
    public static void main(String[] args) {
        // create input variables X1 (Size), X2 (Irregularity), X3 (Density)
        FuzzyVariable size = new FuzzyVariable("Size", 0.0, 100.0);
        size.addFuzzySet(new FuzzySet("Low", new Triangular(0.0, 0.0, 50.0)));
        size.addFuzzySet(new FuzzySet("Medium", new Triangular(25.0, 50.0, 75.0)));
        size.addFuzzySet(new FuzzySet("High", new Triangular(50.0, 100.0, 100.0)));

        FuzzyVariable irregularity = new FuzzyVariable("Irregularity", 0.0, 100.0);
        irregularity.addFuzzySet(new FuzzySet("Low", new Triangular(0.0, 0.0, 50.0)));
        irregularity.addFuzzySet(new FuzzySet("Medium", new Triangular(25.0, 50.0, 75.0)));
        irregularity.addFuzzySet(new FuzzySet("High", new Triangular(50.0, 100.0, 100.0)));

        FuzzyVariable density = new FuzzyVariable("Density", 0.0, 100.0);
        density.addFuzzySet(new FuzzySet("Low", new Triangular(0.0, 0.0, 50.0)));
        density.addFuzzySet(new FuzzySet("Medium", new Triangular(25.0, 50.0, 75.0)));
        density.addFuzzySet(new FuzzySet("High", new Triangular(50.0, 100.0, 100.0)));

        // output variable Malignancy
        FuzzyVariable malignancy = new FuzzyVariable("Malignancy", 0.0, 100.0);
        malignancy.addFuzzySet(new FuzzySet("Benign", new Triangular(0.0, 25.0, 50.0)));
        malignancy.addFuzzySet(new FuzzySet("Malignant", new Triangular(50.0, 75.0, 100.0)));

        // build rule base
        RuleBase rb = new RuleBase();
        RuleEditor editor = new RuleEditor(rb);

        // Rule 1: If Size is High OR Irregularity is High OR Density is High THEN Malignant
        editor.createRule()
                .when("Size", "High")
                .or("Irregularity", "High")
                .or("Density", "High")
                .then("Malignancy", "Malignant")
                .withWeight(1.0)
                .build();

        // Rule 2: If Size is Low AND Irregularity is Low AND Density is Low THEN Benign
        editor.createRule()
                .when("Size", "Low")
                .and("Irregularity", "Low")
                .and("Density", "Low")
                .then("Malignancy", "Benign")
                .withWeight(1.0)
                .build();

        // Rule 3: If Size is Medium AND Irregularity is High THEN Malignant
        editor.createRule()
                .when("Size", "Medium")
                .and("Irregularity", "High")
                .then("Malignancy", "Malignant")
                .withWeight(0.9)
                .build();

        // Rule 4: If Density is Medium AND Irregularity is Medium THEN Malignant (weaker)
        editor.createRule()
                .when("Density", "Medium")
                .and("Irregularity", "Medium")
                .then("Malignancy", "Malignant")
                .withWeight(0.6)
                .build();

        // Rule 5: If Size is Low AND Density is Medium THEN Benign
        editor.createRule()
                .when("Size", "Low")
                .and("Density", "Medium")
                .then("Malignancy", "Benign")
                .withWeight(0.8)
                .build();

        // prepare engine
        Mamdani engine = new Mamdani();
        engine.setRules(rb.getEnabledRules());
        engine.setSampleCount(101);

        // example patient 1
        size.setValue(30.0); // somewhat large
        irregularity.setValue(80.0); // irregular
        density.setValue(90.0); // normal

        List<FuzzyVariable> inputs = Arrays.asList(size, irregularity, density);
        engine.infer(inputs, malignancy);

        System.out.println("--- Demo: Malignancy Estimation (Example 1) ---");
        System.out.println("Crisp output (malignancy): " + malignancy.getValue());
        System.out.println("Rule firings: " + engine.getLastRuleFirings());

        // print a small sample of aggregated membership
        double[] xs = engine.getLastXSamples();
        double[] aggregated = engine.getLastAggregated();
        System.out.println("Sampled aggregated membership (first 10):");
        for (int i = 0; i < Math.min(10, xs.length); i++) {
            System.out.printf("x=%.2f mu=%.4f\n", xs[i], aggregated[i]);
        }

        // example patient 2 (benign)
        size.setValue(10.0);
        irregularity.setValue(5.0);
        density.setValue(20.0);
        engine.infer(inputs, malignancy);

        System.out.println("--- Demo: Malignancy Estimation (Example 2) ---");
        System.out.println("Crisp output (malignancy): " + malignancy.getValue());
        System.out.println("Rule firings: " + engine.getLastRuleFirings());
    }
}