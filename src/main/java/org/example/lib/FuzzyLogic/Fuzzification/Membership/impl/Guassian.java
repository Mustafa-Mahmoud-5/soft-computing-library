package org.example.lib.FuzzyLogic.Fuzzification.Membership.impl;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction;

public class Guassian implements MembershipFunction {
  private final double c; // de el Center 
    private final double sigma; //

    public Guassian(double c, double sigma) {
        this.c = c;
        this.sigma = sigma;
    }

    @Override
    public double compute(double x) {
        double diff = x - c;
        return Math.exp(- (diff * diff) / (2 * sigma * sigma));
    }
    public double getC() { return c; }
    public double getSigma() { return sigma; }
    @Override
    public double CalcCentroid() {
        return c;
    }

}
