package org.example.lib.FuzzyLogic.demo;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl.Mamdani;
import org.example.lib.FuzzyLogic.RuleEditor.RuleBase;
import org.example.lib.FuzzyLogic.RuleEditor.RuleEditor;

import java.util.Arrays;
import java.util.List;

/**
 * Hierarchical/Chained Fuzzy Inference Demo
 *
 * Two-stage inference system:
 * RS1 (Stage 1): X1, X2 → Y
 *   R1: If X1=L AND X2=M THEN Y=L
 *   R2: If X1=M AND X2=H THEN Y=H
 *
 * RS2 (Stage 2): Y, X3 → D (uses output Y from Stage 1 as input)
 *   R3: If Y=L AND X3=M THEN D=B
 *   R4: If Y=H AND X3=H THEN D=M
 */
public class DemoHierarchicalRulebase {
    public static void main(String[] args) {
        System.out.println("=== TWO-STAGE HIERARCHICAL FUZZY INFERENCE ===\n");

        // ==========================================
        // Stage 1: Define input variables X1, X2
        // ==========================================
        FuzzyVariable x1 = new FuzzyVariable("X1", 0.0, 100.0);
        x1.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));       // Low
        x1.addFuzzySet(new FuzzySet("M", new Triangular(25.0, 50.0, 75.0)));     // Medium
        x1.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));   // High

        FuzzyVariable x2 = new FuzzyVariable("X2", 0.0, 100.0);
        x2.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));
        x2.addFuzzySet(new FuzzySet("M", new Triangular(25.0, 50.0, 75.0)));
        x2.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));

        // ==========================================
        // Stage 1: Define intermediate output Y
        // ==========================================
        FuzzyVariable y = new FuzzyVariable("Y", 0.0, 100.0);
        y.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));
        y.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));

        // ==========================================
        // Stage 1: Build RS1 (X1, X2 → Y)
        // ==========================================
        RuleBase rs1 = new RuleBase();
        RuleEditor editor1 = new RuleEditor(rs1);

        // R1: If X1=L AND X2=M THEN Y=L
        editor1.createRule()
                .when("X1", "L")
                .and("X2", "M")
                .then("Y", "L")
                .withWeight(1.0)
                .build();

        // R2: If X1=M AND X2=H THEN Y=H
        editor1.createRule()
                .when("X1", "M")
                .and("X2", "H")
                .then("Y", "H")
                .withWeight(1.0)
                .build();

        // ==========================================
        // Stage 1: Run RS1 to compute Y
        // ==========================================
        Mamdani engine1 = new Mamdani();
        engine1.setRules(rs1.getEnabledRules());
        engine1.setSampleCount(101);

        // Test case 1: X1=20 (Low), X2=60 (Medium)
        System.out.println("--- Test Case 1: X1=20, X2=60 ---");
        x1.setValue(30.0);
        x2.setValue(80.0);
        List<FuzzyVariable> stageInputs1 = Arrays.asList(x1, x2);

        engine1.infer(stageInputs1, y);  // Compute Y from X1, X2
        double yValue = y.getValue();  // Get intermediate Y value

        System.out.println("Stage 1 (RS1) Results:");
        System.out.println("  Inputs: X1=" + x1.getValue() + ", X2=" + x2.getValue());
        System.out.println("  Rule firings: " + engine1.getLastRuleFirings());
        System.out.println("  Intermediate output Y: " + yValue);

        // ==========================================
        // Stage 2: Define input X3 and output D
        // ==========================================
        FuzzyVariable x3 = new FuzzyVariable("X3", 0.0, 100.0);
        x3.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));
        x3.addFuzzySet(new FuzzySet("M", new Triangular(25.0, 50.0, 75.0)));
        x3.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));

        FuzzyVariable d = new FuzzyVariable("D", 0.0, 100.0);
        d.addFuzzySet(new FuzzySet("B", new Triangular(0.0, 25.0, 50.0)));   // Benign
        d.addFuzzySet(new FuzzySet("M", new Triangular(50.0, 75.0, 100.0))); // Malignant

        // ==========================================
        // Stage 2: Build RS2 (Y, X3 → D)
        // ==========================================
        RuleBase rs2 = new RuleBase();
        RuleEditor editor2 = new RuleEditor(rs2);

        // R3: If Y=L AND X3=M THEN D=B
        editor2.createRule()
                .when("Y", "L")
                .and("X3", "M")
                .then("D", "B")
                .withWeight(1.0)
                .build();

        // R4: If Y=H AND X3=H THEN D=M
        editor2.createRule()
                .when("Y", "H")
                .and("X3", "H")
                .then("D", "M")
                .withWeight(1.0)
                .build();

        // ==========================================
        // Stage 2: Run RS2 using Y and X3
        // ==========================================
        Mamdani engine2 = new Mamdani();
        engine2.setRules(rs2.getEnabledRules());
        engine2.setSampleCount(101);

        // Use Y from Stage 1 and set X3 input
        x3.setValue(90.0);  // X3 = Medium-High
        // Y already has crisp value from Stage 1 (yValue)

        List<FuzzyVariable> stageInputs2 = Arrays.asList(y, x3);
        engine2.infer(stageInputs2, d);  // Compute D from Y (output of Stage 1) and X3

        System.out.println("\nStage 2 (RS2) Results:");
        System.out.println("  Inputs: Y=" + yValue + " (from Stage 1), X3=" + x3.getValue());
        System.out.println("  Rule firings: " + engine2.getLastRuleFirings());
        System.out.println("  Final output D: " + d.getValue());

        // ==========================================
        // Test case 2: Different input values
        // ==========================================
        System.out.println("\n--- Test Case 2: X1=55, X2=85 ---");
        x1.setValue(55.0);  // Medium-High
        x2.setValue(85.0);  // High

        engine1.infer(stageInputs1, y);  // Stage 1: compute Y
        yValue = y.getValue();

        System.out.println("Stage 1 (RS1) Results:");
        System.out.println("  Inputs: X1=" + x1.getValue() + ", X2=" + x2.getValue());
        System.out.println("  Rule firings: " + engine1.getLastRuleFirings());
        System.out.println("  Intermediate output Y: " + yValue);

        x3.setValue(85.0);  // High
        engine2.infer(stageInputs2, d);  // Stage 2: compute D using Y and X3

        System.out.println("\nStage 2 (RS2) Results:");
        System.out.println("  Inputs: Y=" + yValue + " (from Stage 1), X3=" + x3.getValue());
        System.out.println("  Rule firings: " + engine2.getLastRuleFirings());
        System.out.println("  Final output D: " + d.getValue());

        System.out.println("\n=== END OF HIERARCHICAL DEMO ===");
    }
}
