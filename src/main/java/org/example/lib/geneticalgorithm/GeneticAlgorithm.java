package org.example.lib.geneticalgorithm;

import lombok.Setter;
import org.example.lib.Algorithm;
import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.BinaryChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.IntChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.PermutationChromosomeFactory;
import org.example.lib.geneticalgorithm.enums.CHROMOSOME_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Setter
public class GeneticAlgorithm implements Algorithm {
    private List<Chromosome> population = new ArrayList<>();
    private List<Chromosome> matingPool = new ArrayList<>();
    private Chromosome bestChromosome = null;
    private ChromosomeFactory chromosomeFactory;
    private double avgFitness = 0;
    private double bestFitness = 0;

    // config props, maybe should be moved to a config class
    private int populationSize = 10;
    private int chromosomeSize = 5;
    private int maxGenerations = 50;
    private int currentGeneration = 0;
    private double crossoverRate = 0.6;
    private double mutationRate = 0.1;
    private int elitismCount = 0;

    private Predicate<Chromosome> isFeasible;
    private Function<Chromosome, Double> fitnessFunction;

    @Override
    public void run() {
        initializePopulation();



        // Selection
        // Crossover
        // Mutation
        // Replacement
        // Fitness
    }

    public void setChromosomeType(CHROMOSOME_TYPE type) {
        switch (type) {
            case BINARY -> this.chromosomeFactory = new BinaryChromosomeFactory();
            case PERMUTATION -> this.chromosomeFactory = new PermutationChromosomeFactory();
            case INTEGER -> this.chromosomeFactory = new IntChromosomeFactory();
            default -> throw new IllegalArgumentException("Unsupported chromosome type: " + type);
        }
    }


    private void validateConfig() {}

    private void initializePopulation() {
        if(this.population.isEmpty()) this.population.clear();
        for(int i = 0; i < populationSize; i++) {
            Chromosome chromosome = chromosomeFactory.newInstance(chromosomeSize);
            this.population.add(chromosome);
        }
    }
}
