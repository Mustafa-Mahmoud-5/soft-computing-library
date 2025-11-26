package org.example.lib.FuzzyLogic.demo;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.impl.Triangular;
import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipCentroidUtils;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzySet;
import org.example.lib.FuzzyLogic.Fuzzification.Variables.FuzzyVariable;
import org.example.lib.FuzzyLogic.Inference.InferenceEngine.impl.Mamdani;
import org.example.lib.FuzzyLogic.RuleEditor.RuleBase;
import org.example.lib.FuzzyLogic.RuleEditor.RuleEditor;

import java.util.Arrays;
import java.util.Map;

/**
 * Demo: Two-Stage Hierarchical Inference with Weighted Average (Center of Sums) Defuzzification
 *
 * This demo shows:
 * 1. How to properly compute centroids of fuzzy sets
 * 2. How to use the weighted average method: z* = Σ μ_c(z_bar) * z_bar / Σ μ_c(z_bar)
 * 3. Two-stage chained inference
 *
 * RS1 (Stage 1): X1, X2 → Y
 *   R1: If X1=L AND X2=M THEN Y=L
 *   R2: If X1=M AND X2=H THEN Y=H
 *
 * RS2 (Stage 2): Y, X3 → D
 *   R3: If Y=L AND X3=M THEN D=B
 *   R4: If Y=H AND X3=H THEN D=M
 */
public class DemoWeightedAverageMethod {
    public static void main(String[] args) {
        System.out.println("=== WEIGHTED AVERAGE (CENTER OF SUMS) DEFUZZIFICATION ===\n");

        // ==========================================
        // Stage 1: Define input variables X1, X2
        // ==========================================
        FuzzyVariable x1 = new FuzzyVariable("X1", 0.0, 100.0);
        x1.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));
        x1.addFuzzySet(new FuzzySet("M", new Triangular(25.0, 50.0, 75.0)));
        x1.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));

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

        // Build stage 1 rules
        RuleBase rs1 = new RuleBase();
        RuleEditor editor1 = new RuleEditor(rs1);
        editor1.createRule().when("X1", "L").and("X2", "M").then("Y", "L").withWeight(1.0).build();
        editor1.createRule().when("X1", "M").and("X2", "H").then("Y", "H").withWeight(1.0).build();

        // ==========================================
        // Stage 2: Define input X3 and output D
        // ==========================================
        FuzzyVariable x3 = new FuzzyVariable("X3", 0.0, 100.0);
        x3.addFuzzySet(new FuzzySet("L", new Triangular(0.0, 0.0, 50.0)));
        x3.addFuzzySet(new FuzzySet("M", new Triangular(25.0, 50.0, 75.0)));
        x3.addFuzzySet(new FuzzySet("H", new Triangular(50.0, 100.0, 100.0)));

        FuzzyVariable d = new FuzzyVariable("D", 0.0, 100.0);
        d.addFuzzySet(new FuzzySet("B", new Triangular(0.0, 25.0, 50.0)));
        d.addFuzzySet(new FuzzySet("M", new Triangular(50.0, 75.0, 100.0)));

        // Build stage 2 rules
        RuleBase rs2 = new RuleBase();
        RuleEditor editor2 = new RuleEditor(rs2);
        editor2.createRule().when("Y", "L").and("X3", "M").then("D", "B").withWeight(1.0).build();
        editor2.createRule().when("Y", "H").and("X3", "H").then("D", "M").withWeight(1.0).build();

        // Precompute centroids of output fuzzy sets
        double yLCentroid = MembershipCentroidUtils.triangularCentroid(0.0, 0.0, 50.0);     // = 16.67
        double yHCentroid = MembershipCentroidUtils.triangularCentroid(50.0, 100.0, 100.0); // = 83.33
        double dBCentroid = MembershipCentroidUtils.triangularCentroid(0.0, 25.0, 50.0);    // = 25.0
        double dMCentroid = MembershipCentroidUtils.triangularCentroid(50.0, 75.0, 100.0);  // = 75.0

        System.out.println("Pre-computed centroids:");
        System.out.println("  Y=L centroid: " + String.format("%.2f", yLCentroid));
        System.out.println("  Y=H centroid: " + String.format("%.2f", yHCentroid));
        System.out.println("  D=B centroid: " + String.format("%.2f", dBCentroid));
        System.out.println("  D=M centroid: " + String.format("%.2f", dMCentroid));
        System.out.println();

        // ==========================================
        // Test Case 1: X1=20, X2=60
        // ==========================================
        System.out.println("--- Test Case 1: X1=20, X2=60 ---");
        x1.setValue(30.0);
        x2.setValue(80.0);

        // Create engine - uses Centroid (weighted average) by default
        Mamdani engine1 = new Mamdani();
        engine1.setRules(rs1.getEnabledRules());

        // Stage 1 inference using weighted average method (default with Centroid)
        Mamdani engine1Sampled = new Mamdani();
        engine1Sampled.setRules(rs1.getEnabledRules());
        engine1Sampled.setSampleCount(101);
        engine1Sampled.infer(Arrays.asList(x1, x2), y);

        Map<Integer, Double> stage1Firings = engine1Sampled.getLastRuleFirings();
        System.out.println("Stage 1 Rule firings: " + stage1Firings);

        // Manually compute weighted average for Y using centroids
        double mu_yL = stage1Firings.getOrDefault(0, 0.0);  // R1 fires to Y=L
        double mu_yH = stage1Firings.getOrDefault(1, 0.0);  // R2 fires to Y=H

        double yWeightedAverage = (mu_yL * yLCentroid + mu_yH * yHCentroid) / (mu_yL + mu_yH);
        if (Double.isNaN(yWeightedAverage)) yWeightedAverage = (yLCentroid + yHCentroid) / 2.0;

        System.out.println("Weighted average calculation for Y:");
        System.out.println("  μ(Y=L) * centroid(L) + μ(Y=H) * centroid(H) / (μ(Y=L) + μ(Y=H))");
        System.out.println("  " + String.format("%.2f * %.2f + %.2f * %.2f / (%.2f + %.2f)",
                mu_yL, yLCentroid, mu_yH, yHCentroid, mu_yL, mu_yH));
        System.out.println("  = " + String.format("%.2f", yWeightedAverage));
        y.setValue(yWeightedAverage);

        // Stage 2
        x3.setValue(90.0);
        Mamdani engine2Sampled = new Mamdani();
        engine2Sampled.setRules(rs2.getEnabledRules());
        engine2Sampled.setSampleCount(101);
        engine2Sampled.infer(Arrays.asList(y, x3), d);

        Map<Integer, Double> stage2Firings = engine2Sampled.getLastRuleFirings();
        System.out.println("\nStage 2 Rule firings: " + stage2Firings);

        double mu_dB = stage2Firings.getOrDefault(0, 0.0);  // R3 fires to D=B
        double mu_dM = stage2Firings.getOrDefault(1, 0.0);  // R4 fires to D=M

        double dWeightedAverage = (mu_dB * dBCentroid + mu_dM * dMCentroid) / (mu_dB + mu_dM);
        if (Double.isNaN(dWeightedAverage)) dWeightedAverage = (dBCentroid + dMCentroid) / 2.0;

        System.out.println("Weighted average calculation for D:");
        System.out.println("  μ(D=B) * centroid(B) + μ(D=M) * centroid(M) / (μ(D=B) + μ(D=M))");
        System.out.println("  " + String.format("%.2f * %.2f + %.2f * %.2f / (%.2f + %.2f)",
                mu_dB, dBCentroid, mu_dM, dMCentroid, mu_dB, mu_dM));
        System.out.println("  = " + String.format("%.2f", dWeightedAverage));
        d.setValue(dWeightedAverage);

        System.out.println("\nFinal result: D = " + String.format("%.2f", dWeightedAverage));

        // ==========================================
        // Test Case 2: X1=55, X2=85
        // ==========================================
        System.out.println("\n--- Test Case 2: X1=55, X2=85 ---");
        x1.setValue(55.0);
        x2.setValue(85.0);

        engine1Sampled.infer(Arrays.asList(x1, x2), y);
        stage1Firings = engine1Sampled.getLastRuleFirings();
        System.out.println("Stage 1 Rule firings: " + stage1Firings);

        mu_yL = stage1Firings.getOrDefault(0, 0.0);
        mu_yH = stage1Firings.getOrDefault(1, 0.0);

        yWeightedAverage = (mu_yL * yLCentroid + mu_yH * yHCentroid) / (mu_yL + mu_yH);
        if (Double.isNaN(yWeightedAverage)) yWeightedAverage = (yLCentroid + yHCentroid) / 2.0;

        System.out.println("Weighted average for Y: " + String.format("%.2f", yWeightedAverage));
        y.setValue(yWeightedAverage);

        x3.setValue(85.0);
        engine2Sampled.infer(Arrays.asList(y, x3), d);
        stage2Firings = engine2Sampled.getLastRuleFirings();
        System.out.println("Stage 2 Rule firings: " + stage2Firings);

        mu_dB = stage2Firings.getOrDefault(0, 0.0);
        mu_dM = stage2Firings.getOrDefault(1, 0.0);

        dWeightedAverage = (mu_dB * dBCentroid + mu_dM * dMCentroid) / (mu_dB + mu_dM);
        if (Double.isNaN(dWeightedAverage)) dWeightedAverage = (dBCentroid + dMCentroid) / 2.0;

        System.out.println("Weighted average for D: " + String.format("%.2f", dWeightedAverage));
        d.setValue(dWeightedAverage);

        System.out.println("\nFinal result: D = " + String.format("%.2f", dWeightedAverage));
        System.out.println("\n=== END OF DEMO ===");
    }
}
