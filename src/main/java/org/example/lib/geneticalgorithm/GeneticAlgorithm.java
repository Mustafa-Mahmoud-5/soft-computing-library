package org.example.lib.geneticalgorithm;


import org.example.lib.Algorithm;
import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.BinaryChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.IntChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.factory.impl.PermutationChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;
import org.example.lib.geneticalgorithm.crossover.impl.KPointCrossover;
import org.example.lib.geneticalgorithm.crossover.impl.Order1Crossover;
import org.example.lib.geneticalgorithm.crossover.impl.SinglePointCrossover;
import org.example.lib.geneticalgorithm.enums.CHROMOSOME_TYPE;
import org.example.lib.geneticalgorithm.enums.CROSSOVER_METHOD;
import org.example.lib.geneticalgorithm.enums.MUTATION_METHOD;
import org.example.lib.geneticalgorithm.enums.SELECTION_METHOD;
import org.example.lib.geneticalgorithm.mutation.Mutation;
import org.example.lib.geneticalgorithm.mutation.impl.FlipMutation;
import org.example.lib.geneticalgorithm.mutation.impl.SwapMutation;
import org.example.lib.geneticalgorithm.replacement.Replacement;
import org.example.lib.geneticalgorithm.selection.Selection;
import org.example.lib.geneticalgorithm.selection.impl.RouletteWheelSelection;
import org.example.lib.geneticalgorithm.selection.impl.TournamentSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class GeneticAlgorithm implements Algorithm {
    private List<Chromosome> population = new ArrayList<>();
    private List<Chromosome> matingPool = new ArrayList<>();
    private ChromosomeFactory chromosomeFactory;
    private Selection selectionOperator;
    private Crossover crossoverOperator;
    private Mutation mutationOperator;
    private Replacement replacementOperator;

    // statistical data
    private Chromosome bestChromosome;
    private double avgFitness = 0;
    private double bestFitness = 0;
    private int currentGeneration = 0;

    // config props, maybe should be moved to a config class
    private int populationSize = 10;
    private int chromosomeSize = 5;
    private int maxGenerations = 50;
    private double crossoverRate = 0.6;
    private double mutationRate = 0.1;
    private int elitismCount = 0;

    private Predicate<Chromosome> isFeasible;
    private Function<Chromosome, Double> fitnessFunction;

    @Override
    public void run() {
        initializePopulation();
        calculatePopulationFitness();
        for(int i = 1; i <= maxGenerations; i++) { // later make it while(!isFinished())
            applySelection();
            generate();
            applyReplacement();
            calculatePopulationFitness();
        }
    }

    public void setChromosomeType(CHROMOSOME_TYPE type) {
        switch (type) {
            case BINARY -> chromosomeFactory = new BinaryChromosomeFactory();
            case PERMUTATION -> chromosomeFactory = new PermutationChromosomeFactory();
            case INTEGER -> chromosomeFactory = new IntChromosomeFactory();
            default -> throw new IllegalArgumentException("Unsupported chromosome type: " + type);
        }
    }

    public void setSelectionMethod(SELECTION_METHOD method) {
        switch (method) {
            case ROULETTE_WHEEL -> selectionOperator = new RouletteWheelSelection();
            case TOURNAMENT -> selectionOperator = new TournamentSelection();
            default -> throw new IllegalArgumentException("Unsupported selection type: " + method);
        }
    }

    public void setCrossoverMethod(CROSSOVER_METHOD method) {
        switch (method) {
            case SINGLE_POINT -> this.crossoverOperator = new SinglePointCrossover();
            case K_POINT -> this.crossoverOperator = new KPointCrossover();
            case ORDER1 -> this.crossoverOperator = new Order1Crossover();
            default -> throw new IllegalArgumentException("Unsupported crossover type: " + method);
        }
    }

    public void setMutationMethod(MUTATION_METHOD method) {
        switch (method) {
            case FLIP -> mutationOperator = new FlipMutation();
            case SWAP -> mutationOperator = new SwapMutation();
            default -> throw new IllegalArgumentException("Unsupported crossover type: " + method);
        }
    }


    private void validateConfig() {}

    private void initializePopulation() {
        if(!population.isEmpty()) population.clear();
        for(int i = 0; i < populationSize; i++) {
            Chromosome chromosome = chromosomeFactory.newInstance(chromosomeSize);
            population.add(chromosome);
        }
    }

    private void calculatePopulationFitness() {
        for(int i = 0; i < populationSize; i++) {
            double fitness = fitnessFunction.apply(population.get(i));
            population.get(i).setFitness(fitness);
        }
    }

    private void applySelection() {
        matingPool.clear();
        matingPool = selectionOperator.select(population);
    }

    // produces new generation in the matingPool
    // applies crossover then mutation
    private void generate() {
        for(int i = 0;i < matingPool.size()/2; i++) {
            List<Chromosome> children = crossoverOperator.crossover(matingPool.get(i), matingPool.get(i+1), crossoverRate, chromosomeFactory);
            Chromosome c1 = children.get(0);
            Chromosome c2 = children.get(1);

            mutationOperator.mutate(c1, mutationRate);
            mutationOperator.mutate(c2, mutationRate);

            matingPool.set(i, c1);
            matingPool.set(i+1, c2);
        }
    }

    private void applyReplacement() {
        replacementOperator.replace(population, matingPool);
    }


    // TODO: add getters/setters for required attributes only
}
