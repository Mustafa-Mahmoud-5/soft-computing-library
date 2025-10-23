package org.example;

import org.example.lib.geneticalgorithm.GeneticAlgorithm;
import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.enums.*;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class Main {

    static double tspFitness(Chromosome chromosome,double[][] dist) {
        List<Integer> order = (List<Integer>) chromosome.getGenes();
        double totalDistance = 0;
        int l = order.size();
        for (int i = 0; i < l - 1; i++) {
            totalDistance += dist[order.get(i)][order.get(i + 1)];
        }
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


        Predicate<Chromosome> isFeasible = chromosome -> {
            List<Integer> genes = (List<Integer>) chromosome.getGenes();
            return genes.size() == new HashSet<>(genes).size(); // no duplicates
        };


        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.PERMUTATION);
        geneticAlgorithm.setSelectionMethod(SELECTION_METHOD.TOURNAMENT);
        geneticAlgorithm.setCrossoverMethod(CROSSOVER_METHOD.ORDER1);
        geneticAlgorithm.setMutationMethod(MUTATION_METHOD.SWAP);
        geneticAlgorithm.setReplacementMethod(REPLACEMENT_METHOD.GENERATIONAL);

        geneticAlgorithm.setChromosomeSize(dist.length);
        geneticAlgorithm.setPopulationSize(50);
        geneticAlgorithm.setMaxGenerations(500);
        geneticAlgorithm.setCrossoverRate(0.8);
        geneticAlgorithm.setMutationRate(0.2);

        geneticAlgorithm.setFitnessFunction(fitness);
        geneticAlgorithm.setIsFeasibleChromosomePredicate(isFeasible);

        geneticAlgorithm.run();
        geneticAlgorithm.displayResults();
    }
}