package org.example.lib.FuzzyLogic.Fuzzification.Membership;

/**
 * Utility for computing centroids of standard membership function shapes.
 * 
 * These centroids are used by the weighted average (center of sums) defuzzification method.
 */
public final class MembershipCentroidUtils {
    private MembershipCentroidUtils() {}

    /**
     * Compute the centroid of a membership function using sampling-based integration.
     * This numerically integrates: centroid = Σ(x * μ(x)) / Σ(μ(x))
     *
     * @param mf the membership function
     * @param min domain minimum
     * @param max domain maximum
     * @param samples number of sample points
     * @return centroid (center of mass)
     */
    public static double computeCentroid(MembershipFunction mf, double min, double max, int samples) {
        if (mf == null || samples < 2) return (min + max) / 2.0;
        
        double numerator = 0.0;
        double denominator = 0.0;
        double step = (max - min) / (samples - 1);
        
        for (int i = 0; i < samples; i++) {
            double x = min + i * step;
            double mu = mf.compute(x);
            numerator += x * mu;
            denominator += mu;
        }
        
        if (denominator < 1e-12) {
            return (min + max) / 2.0;
        }
        return numerator / denominator;
    }

    /**
     * Compute the centroid (center of mass) of a triangular MF analytically.
     * For triangle (a, b, c), centroid = (a + b + c) / 3
     *
     * @param a left end
     * @param b peak
     * @param c right end
     * @return centroid (geometric center)
     */
    public static double triangularCentroid(double a, double b, double c) {
        return (a + b + c) / 3.0;
    }

    /**
     * Compute the centroid of a trapezoidal MF analytically.
     * For trapezoid (a, b, c, d), centroid = (a + b + c + d) / 4
     *
     * @param a left end
     * @param b left plateau end
     * @param c right plateau end
     * @param d right end
     * @return centroid (geometric center)
     */
    public static double trapezoidalCentroid(double a, double b, double c, double d) {
        return (a + b + c + d) / 4.0;
    }

    /**
     * Compute the centroid of a Gaussian MF analytically.
     * For Gaussian(c, sigma), the centroid is at the center c.
     *
     * @param c center
     * @param sigma standard deviation
     * @return centroid (which is c itself)
     */
    public static double gaussianCentroid(double c, double sigma) {
        return c;  // center of Gaussian is its centroid
    }
}
