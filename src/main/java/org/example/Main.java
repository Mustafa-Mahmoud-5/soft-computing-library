package org.example;

import org.example.lib.geneticalgorithm.GeneticAlgorithm;
import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.enums.*;

import java.util.function.Function;
import java.util.function.Predicate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Function<Chromosome, Double> fitness = (chromosome) -> {
              return chromosome.getFitness();
        };

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.PERMUTATION);
        geneticAlgorithm.setSelectionMethod(SELECTION_METHOD.ROULETTE_WHEEL);
        geneticAlgorithm.setCrossoverMethod(CROSSOVER_METHOD.SINGLE_POINT);
        geneticAlgorithm.setMutationMethod(MUTATION_METHOD.FLIP);
        geneticAlgorithm.setReplacementMethod(REPLACEMENT_METHOD.GENERATIONAL);
        geneticAlgorithm.setFitnessFunction(fitness);
        geneticAlgorithm.setIsFeasibleChromosomePredicate(chromosome -> true);
        geneticAlgorithm.run();
    }
}