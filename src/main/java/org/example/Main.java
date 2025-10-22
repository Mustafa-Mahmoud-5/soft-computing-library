package org.example;

import org.example.lib.geneticalgorithm.GeneticAlgorithm;
import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.enums.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static double tspFitness(Chromosome chromosome,double[][] dist) {
        List<Integer> order = (List<Integer>) chromosome.getGenes();
        double totalDistance = 0;
        int l = order.size();
        for (int i = 0; i < l - 1; i++) {
            totalDistance += dist[order.get(i)][order.get(i + 1)];
        }
        totalDistance += dist[order.get(l - 1)][order.get(0)];
        return 1.0 / totalDistance;
    }
    public static void main(String[] args) {
        double[][] dist = {
                {0, 2, 9, 10},
                {1, 0, 6, 4},
                {15, 7, 0, 8},
                {6, 3, 12, 0}
        };
       Function<Chromosome, Double> fitness = (chromosome) -> tspFitness(chromosome, dist);


        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.BINARY);
        geneticAlgorithm.setSelectionMethod(SELECTION_METHOD.ROULETTE_WHEEL);
        geneticAlgorithm.setCrossoverMethod(CROSSOVER_METHOD.SINGLE_POINT);
        geneticAlgorithm.setReplacementMethod(REPLACEMENT_METHOD.GENERATIONAL);
        geneticAlgorithm.setFitnessFunction(fitness);
        geneticAlgorithm.setIsFeasibleChromosomePredicate(chromosome -> true);
        geneticAlgorithm.run();
    }
}