package org.example.lib.datahelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<DataRow> loadData(String filePath) throws IOException {
        List<DataRow> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");

                double homeElo  = Double.parseDouble(values[0]) / 3000;
                double awayElo  = Double.parseDouble(values[1]) / 3000;
                double homeForm = Double.parseDouble(values[2]) / 25;
                double awayForm = Double.parseDouble(values[3]) / 25;

                List<Double> inputs = List.of(homeElo, awayElo, homeForm, awayForm);

                // one-hot encoded target for the output column
                List<Double> targets = switch (values[4]) {
                    case "H" -> List.of(1.0, 0.0, 0.0);
                    case "D" -> List.of(0.0, 1.0, 0.0);
                    case "A" -> List.of(0.0, 0.0, 1.0);
                    default  -> throw new IllegalArgumentException("Unknown result: " + values[4]);
                };

                dataset.add(new DataRow(inputs, targets));
            }
        }

        return dataset;
    }
}
