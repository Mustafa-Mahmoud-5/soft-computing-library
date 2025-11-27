package org.example.lib.FuzzyLogic.Fuzzification.Membership.impl;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction;

public class Trapezoidal implements MembershipFunction {
   private final double a, b, c, d;
    private final double mLeft, qLeft, mRight, qRight;
    private final boolean leftVertical, rightVertical;
    private static final double EPS = 1e-12;

    public Trapezoidal(double a, double b, double c, double d) {

        this.a = a; this.b = b; this.c = c; this.d = d;

        if (Math.abs(b - a) < EPS) { leftVertical = true; mLeft = qLeft = 0.0; }
        else { leftVertical = false; mLeft = 1.0 / (b - a); qLeft = -mLeft * a; }

        if (Math.abs(d - c) < EPS) { rightVertical = true; mRight = qRight = 0.0; }
        else { rightVertical = false; mRight = -1.0 / (d - c); qRight = 1.0 - mRight * c; }
    }

    @Override
    public double compute(double x) {
        if (x <= a || x >= d) return 0.0;
        if (x >= b && x <= c) return 1.0;
        if (x < b) return leftVertical ? 0.0 : mLeft * x + qLeft;
        else return rightVertical ? 0.0 : mRight * x + qRight;
    }
    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }
    public double getD() { return d; }
    @Override
    public double CalcCentroid() {
        return (a + b + c + d) / 4.0;
    }

}
