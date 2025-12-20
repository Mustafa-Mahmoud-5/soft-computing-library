package org.example.lib.datahelper;

import java.util.List;

public class Utils {
    public static int argMax(List<Double> data) {
        int mxIdx = 0;
        double mxVal = data.get(0);
        for(int i = 0; i < data.size(); i++) {
            double val = data.get(i);
            if(val > mxVal) {
                mxVal = val;
                mxIdx = i;
            }
        }
        return mxIdx;
    }

    public static double normalize(double max, double min, double val) {
        // formula: (value - min) / (max - min)
        double divisor = max - min;
        double normalized = 0;
        if(divisor != 0) normalized = (val - min) / divisor;
        return normalized;
    }

}
