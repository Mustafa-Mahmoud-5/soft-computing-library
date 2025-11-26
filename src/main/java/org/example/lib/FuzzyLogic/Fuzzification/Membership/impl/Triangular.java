package org.example.lib.FuzzyLogic.Fuzzification.Membership.impl;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction;

public class Triangular implements MembershipFunction {

    
    private final double a, b, c;
    private final double mLeft, qLeft, mRight, qRight;
    private final boolean leftVertical, rightVertical;
    private static final double EPS = 1e-12;

    public Triangular(double a, double b, double c) {
        
        
        this.a = a; this.b = b; this.c = c;

        if (Math.abs(b - a) < EPS) {
            leftVertical = true; mLeft = qLeft = 0.0;
        } else {
            leftVertical = false;
            mLeft = 1.0 / (b - a); qLeft = -mLeft * a;
        }

        if (Math.abs(c - b) < EPS) {
            rightVertical = true; mRight = qRight = 0.0;
        } else {
            rightVertical = false;
            mRight = -1.0 / (c - b); qRight = 1.0 - mRight * b;
        }
    }

    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }

    @Override
    public double compute(double x) {
        if (x <= a || x >= c) return 0.0;
        if (Math.abs(x - b) < EPS) return 1.0;

        if (x < b) return leftVertical ? 0.0 : mLeft * x + qLeft;
        else return rightVertical ? 0.0 : mRight * x + qRight;
    }
}
