package org.example.lib.FuzzyLogic.Defuzzification;

import org.example.lib.FuzzyLogic.Fuzzification.Membership.MembershipFunction;

public final class DefuzzificationUtils {
    private DefuzzificationUtils() {}

    /**
     * Sample a membership function uniformly across [min, max] with the given number of samples
     * and call the provided defuzzier.
     *
     * @param mf membership function to sample
     * @param min domain minimum
     * @param max domain maximum
     * @param samples number of samples (must be >= 2)
     * @param defuzzier defuzzier to compute crisp value
     * @return defuzzified crisp value
     */
    public static double sampleAndDefuzzify(MembershipFunction mf, double min, double max, int samples, Defuzzier defuzzier) {
        if (mf == null) throw new IllegalArgumentException("membership function cannot be null");
        if (defuzzier == null) throw new IllegalArgumentException("defuzzier cannot be null");
        if (!Double.isFinite(min) || !Double.isFinite(max) || Double.isNaN(min) || Double.isNaN(max))
            throw new IllegalArgumentException("min and max must be finite numbers");
        if (samples < 2) throw new IllegalArgumentException("samples must be >= 2");
        if (max <= min) throw new IllegalArgumentException("max must be greater than min");

        double[] xs = new double[samples];
        double[] mus = new double[samples];
        double step = (max - min) / (samples - 1);
        for (int i = 0; i < samples; i++) {
            double x = min + i * step;
            xs[i] = x;
            mus[i] = mf.compute(x);
        }

        return defuzzier.defuzzify(xs, mus);
    }
}
