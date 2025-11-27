package org.example.lib.demo;

import org.example.lib.FuzzyLogic.FuzzyLogic;
import org.example.lib.FuzzyLogic.Defuzzification.Impl.WeightedAverage;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Trapezoidal;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl.Mamdani;
import org.example.lib.FuzzyLogic.Inference.Rule.Rule;
import org.example.lib.FuzzyLogic.RuleEditor.RuleBase;
import org.example.lib.FuzzyLogic.RuleEditor.RuleEditor;
import org.example.lib.FuzzyLogic.Inference.Rule.LogicalOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo for case study X1: Size — demonstrates Mamdani inference and WeightedAverage defuzzifier.
 */
public class DemoX1Size {

    public static void main(String[] args) {
        // Create input variable X1: Size (0..100)
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


        


        RuleBase rb = new RuleBase();
        RuleEditor editor = new RuleEditor(rb);

        editor.createRule()
                .when("Size", "High")
                .or("Irregularity", "High")
                .or("Density", "High")
                .then("Malignancy", "Malignant")
                .withWeight(1.0)
                .build();

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

        List<Rule> rules = rb.getRules();

        size.setValue(30.0);
        irregularity.setValue(80.0);
        density.setValue(90.0);            // Run pipeline

        // Setup FuzzyLogic engine with Mamdani and WeightedAverage defuzzifier
        FuzzyLogic engine = new FuzzyLogic();
        engine.setInferenceEngine(new Mamdani());
        engine.setDefuzzifier(new WeightedAverage());
        engine.setOutputVariable(malignancy);
        engine.setInputVariables(List.of(size, irregularity, density));
        engine.setRules(rules);



        engine.run();

          
            System.out.println("Aggregated membership degrees: " + engine.getLastAggregated());
            System.out.printf("Defuzzified (crisp) Malignant: %.4f\n", malignancy.getValue());
        
    }
}
