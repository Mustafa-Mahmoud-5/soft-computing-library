package org.example.lib.geneticalgorithm.selection.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.selection.Selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RouletteWheelSelection implements Selection {
    private final int numberOfParents;
    private final Random random;


    public RouletteWheelSelection(int numberOfParents) {
        this.numberOfParents = numberOfParents;
        this.random = new Random();
    }

    @Override
    public List<Chromosome> select(List<Chromosome> population) {

        if (population == null || population.isEmpty()) {
            return new ArrayList<>();
        }

        double totalFitness = calculateTotalFitness(population);

        // Handle negative case
        if (totalFitness <= 0) {
            return selectRandomly(population);
        }

        // Select number of parents
        List<Chromosome> selectedParents = new ArrayList<>();
        for (int i = 0; i < numberOfParents; i++) {
            Chromosome selected = spinWheel(population, totalFitness);
            selectedParents.add(selected);
        }

        return selectedParents;
    }


    private double calculateTotalFitness(List<Chromosome> population) {
        double total = 0.0;
        for (int i = 0; i < population.size(); i++) {
            total += population.get(i).getFitness();
        }
        return total;
    }


    private Chromosome spinWheel(List<Chromosome> population, double totalFitness) {
        double randomValue = random.nextDouble() * totalFitness;

        // Find which chromosome this value lands on
        double cumulativeFitness = 0.0;
        for (int i = 0; i < population.size(); i++) {
            cumulativeFitness += population.get(i).getFitness();
            if (cumulativeFitness >= randomValue) {
                return population.get(i);
            }
        }

        return population.get(population.size() - 1);
    }


    private List<Chromosome> selectRandomly(List<Chromosome> population) {
        List<Chromosome> selected = new ArrayList<>();
        for (int i = 0; i < numberOfParents; i++) {
            int randomIndex = random.nextInt(population.size());
            selected.add(population.get(randomIndex));
        }
        return selected;
    }
}
