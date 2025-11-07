package org.example.lib.FuzzyLogic.Defuzzification;

public interface Defuzzier {
    double defuzzify(double[] xValues, double[] membershipValues);
}
