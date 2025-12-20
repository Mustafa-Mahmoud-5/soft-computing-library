package org.example.lib.datahelper;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    // each feature i, has its min and max in min[i], max[i] for normalizing feature i values
    @Getter
    static double[] min;
    @Getter
    static double[] max;

    public static List<DataRow> loadData(String filePath) throws IOException {
        List<DataRow> dataset = new ArrayList<>();

        List<List<Double>> rawFeaturesList = new ArrayList<>();
        List<List<Double>> targetsList = new ArrayList<>();

        int numFeatures = 9;
        min = new double[numFeatures];
        max = new double[numFeatures];

        for (int i = 0; i < numFeatures; i++) {
            min[i] = Double.MAX_VALUE;
            max[i] = -Double.MAX_VALUE;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");

                List<Double> rawRow = new ArrayList<>();
                for (int i = 0; i < numFeatures; i++) {
                    double val = Double.parseDouble(values[i]);
                    rawRow.add(val);

                    if (val < min[i]) min[i] = val;
                    if (val > max[i]) max[i] = val;
                }
                rawFeaturesList.add(rawRow);
                double isAway = parseTarget(values[9]);
                double isDraw = parseTarget(values[10]);
                double isHome = parseTarget(values[11]);
                targetsList.add(List.of(isHome, isDraw, isAway));
            }
        }

        // normalize the inputs and push
        for (int i = 0; i < rawFeaturesList.size(); i++) {
            List<Double> rawFeatures = rawFeaturesList.get(i);
            List<Double> normalizedFeatures = new ArrayList<>();

            for (int j = 0; j < numFeatures; j++) {
                double val = rawFeatures.get(j);
                normalizedFeatures.add(Utils.normalize(max[j], min[j], val));
            }

            dataset.add(new DataRow(normalizedFeatures, targetsList.get(i)));
        }

        return dataset;
    }

    private static double parseTarget(String val) {
        if (val.equalsIgnoreCase("True") || val.equals("1")) {
            return 1;
        }
        return 0;
    }
}