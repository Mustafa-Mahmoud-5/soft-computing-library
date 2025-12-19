package org.example.lib.nncasestudy;

import org.example.lib.datahelper.DataLoader;
import org.example.lib.datahelper.DataRow;
import org.example.lib.datahelper.Utils;
import org.example.lib.neuralnetwork.FeedForwardNeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Example {
    // NOTE: I searched get some games that actually happened so we can test our model on.
    public void predict(FeedForwardNeuralNetwork nn) {
        List<DataRow> predictionDataset = new ArrayList<>();

        // Match 1: Man City(3) vs Liverpool(0)
        predictionDataset.add(createMatchRow(
                12.0,   // Form5Home
                4.0,    // Form5Away
                1994.0, // HomeElo
                1940.0, // AwayElo
                54.0,   // EloDiff
                8.0,    // FormDiff (12 - 4)
                0.54,   // ImpliedHome (1/1.85)
                0.25,   // ImpliedDraw
                0.26    // ImpliedAway
        ));

        // Match 2: Tottenham(0) vs Chelsea(1)
        predictionDataset.add(createMatchRow(
                8.0,    // Form5Home
                9.0,    // Form5Away
                1813.0, // HomeElo
                1903.0, // AwayElo
                -90.0,  // EloDiff
                -1.0,   // FormDiff (8 - 9)
                0.38,   // ImpliedHome
                0.27,   // ImpliedDraw
                0.40    // ImpliedAway
        ));

        // Match 3: Chelsea(Home) (1) vs Arsenal(Away) (1) - Nov 30, 2025
        predictionDataset.add(createMatchRow(
                9.0,    // Form5Home: Chelsea solid at home (3 wins in 5)
                13.0,   // Form5Away: Arsenal in title-winning form (4 wins, 1 draw)
                1903.0, // HomeElo: Chelsea
                2043.0, // AwayElo: Arsenal (Highest in Europe)
                -140.0, // EloDiff: (1903 - 2043) Big advantage to Away
                -4.0,   // FormDiff: (9 - 13)
                0.31,   // ImpliedHome: ~3.20 odds (Underdogs at home)
                0.26,   // ImpliedDraw: ~3.80 odds
                0.43    // ImpliedAway: ~2.30 odds (Favorite)
        ));

        String[] matchNames = {
                "Man City vs Liverpool 3-0 (Nov 2025)",
                "Tottenham vs Chelsea 0-1(Nov 2025)",
                "Chelsea vs Arsenal 1-1(Nov 2025)"
        };

        for (int i = 0; i < predictionDataset.size(); i++) {
            System.out.println("\nPredicting: " + matchNames[i]);

            int classIdx = nn.predict(predictionDataset.get(i));

            System.out.print("Result: ");
            switch (classIdx) {
                case 0 -> System.out.println("Home Wins");
                case 1 -> System.out.println("Draw");
                case 2 -> System.out.println("Away Wins");
            }
        }
    }

    private DataRow createMatchRow(
            double form5Home, double form5Away,
            double homeElo, double awayElo, double eloDiff, double formDiff,
            double impHome, double impDraw, double impAway) {

        List<Double> features = new ArrayList<>(List.of(
                form5Home, form5Away,
                homeElo, awayElo,
                eloDiff, formDiff,
                impHome, impDraw, impAway
        ));

        for(int i = 0; i < features.size(); i++) {
            double feature = features.get(i);
            double normalized = Utils.normalize(DataLoader.getMax()[i], DataLoader.getMin()[i] ,feature);
            features.set(i, normalized);
        }

        return new DataRow(features, null);
    }
}
