package org.example.lib.geneticalgorithm;


import lombok.Getter;
import lombok.Setter;
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
import org.example.lib.geneticalgorithm.enums.*;
import org.example.lib.geneticalgorithm.mutation.Mutation;
import org.example.lib.geneticalgorithm.mutation.impl.FlipMutation;
import org.example.lib.geneticalgorithm.mutation.impl.SwapMutation;
import org.example.lib.geneticalgorithm.replacement.Replacement;
import org.example.lib.geneticalgorithm.replacement.impl.ElitismReplacement;
import org.example.lib.geneticalgorithm.replacement.impl.GenerationalReplacement;
import org.example.lib.geneticalgorithm.replacement.impl.SteadyStateReplacement;
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

    
    @Setter
    private CHROMOSOME_TYPE chromosomeType;
    @Setter
    private SELECTION_METHOD selectionMethod;
    @Setter
    private CROSSOVER_METHOD crossoverMethod;
    @Setter
    private MUTATION_METHOD mutationMethod;
    @Setter
    private REPLACEMENT_METHOD replacementMethod;


    // statistical data
    @Getter
    private Chromosome bestChromosome;
    @Getter
    private double avgFitness = 0;
    @Getter
    private double bestFitness = 0;
    @Getter
    private int currentGeneration = 0;

    // config props, maybe should be moved to a config class
    @Setter @Getter
    private int populationSize = 10;
    @Setter @Getter
    private int chromosomeSize = 5;
    @Setter @Getter
    private int maxGenerations = 200;
    @Setter @Getter
    private double crossoverRate = 0.6;
    @Setter @Getter
    private double mutationRate = 0.1;
    @Setter @Getter
    private int elitismCount = 0;
    @Setter @Getter
    private int steadyStateReplacementCount = 2;
    @Setter @Getter
    private int crossoverPoints = 1;
    @Setter @Getter
    private int selectionCount = 10;
    @Setter @Getter
    private int geneValueBoundary = 10;
    @Setter @Getter
    private double fitnessTarget;

    @Setter
    private Predicate<Chromosome> isFeasibleChromosomePredicate;
    @Setter
    private Function<Chromosome, Double> fitnessFunction;

    @Setter
    private Predicate<GeneticAlgorithm> terminationCondition;

    @Override
    public void run() {


        initializeOperators();

        initializePopulation();
        calculatePopulationFitness();
        updateBestChromosome();

        while (!shouldTerminate()) {
            applySelection();
            generate(); // crossover+mutation
            applyReplacement();
            calculatePopulationFitness();
            updateBestChromosome();
        }
    }


    private void initializePopulation() {
        if(!population.isEmpty()) population.clear();
        while (population.size() < populationSize) {
            Chromosome chromosome = chromosomeFactory.newInstance(chromosomeSize);
            chromosome.initializeGenes();
            System.out.println(chromosome.getGenes());
            if (isFeasibleChromosomePredicate == null || isFeasibleChromosomePredicate.test(chromosome)) {
                population.add(chromosome);
            }
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
        matingPool = selectionOperator.select(population, selectionCount);
    }

    // produces new generation in the matingPool
    // applies crossover then mutation
    private void generate() {
        for(int i = 0; i < matingPool.size(); i+=2) {
            List<Chromosome> children = crossoverOperator.crossover(matingPool.get(i), matingPool.get(i+1), crossoverRate, chromosomeFactory);
            Chromosome c1 = children.get(0);
            Chromosome c2 = children.get(1);

            mutationOperator.mutate(c1, mutationRate);
            mutationOperator.mutate(c2, mutationRate);

            if (isFeasibleChromosomePredicate == null || isFeasibleChromosomePredicate.test(c1)) {
                matingPool.set(i, c1);
            }
            if (isFeasibleChromosomePredicate == null || isFeasibleChromosomePredicate.test(c2)) {
                matingPool.set(i+1, c2);
            }
        }

        if(matingPool.size() %2 == 1) {
            Chromosome lastChromosome = matingPool.get(matingPool.size() - 1);
            mutationOperator.mutate(lastChromosome, mutationRate); // updated by ref
        }
    }


    private void applyReplacement() {
        population = replacementOperator.replace(population, matingPool);
    }

    private void initializeChromosomeType(CHROMOSOME_TYPE type, Object ... params) {
        switch (type) {
            case BINARY -> chromosomeFactory = new BinaryChromosomeFactory();
            case PERMUTATION -> chromosomeFactory = new PermutationChromosomeFactory();
            case INTEGER -> {
                int randomIntBoundary = 100;
                if(params.length > 0 && params[0] instanceof Integer) {
                    randomIntBoundary = (Integer) params[0];
                }
                chromosomeFactory = new IntChromosomeFactory(randomIntBoundary);
            }
            default -> throw new IllegalArgumentException("Unsupported chromosome type: " + type);
        }
    }

    private void initializeSelectionMethod(SELECTION_METHOD method) {
        switch (method) {
            case ROULETTE_WHEEL -> selectionOperator = new RouletteWheelSelection();
            case TOURNAMENT -> selectionOperator = new TournamentSelection(2,3);
            default -> throw new IllegalArgumentException("Unsupported selection type: " + method);
        }
    }

    private void initializeCrossoverMethod(CROSSOVER_METHOD method) {
        switch (method) {
            case SINGLE_POINT -> crossoverOperator = new SinglePointCrossover();
            case K_POINT -> {
                if(crossoverPoints < 1) throw new IllegalArgumentException("Kpoint crossover requires crossoverPoints param set");
                crossoverOperator = new KPointCrossover(crossoverPoints);
            }
            case ORDER1 -> crossoverOperator = new Order1Crossover();
            default -> throw new IllegalArgumentException("Unsupported crossover method: " + method);
        }
    }

    private void initializeMutationMethod(MUTATION_METHOD method) {
        switch (method) {
            case FLIP -> mutationOperator = new FlipMutation();
            case SWAP -> mutationOperator = new SwapMutation();
            default -> throw new IllegalArgumentException("Unsupported mutation method: " + method);
        }
    }

    private void initializeReplacementMethod(REPLACEMENT_METHOD method) {
        switch (method) {
            case GENERATIONAL -> replacementOperator = new GenerationalReplacement();
            case STEADY_STATE -> replacementOperator = new SteadyStateReplacement(steadyStateReplacementCount);
            case ELITISM -> replacementOperator = new ElitismReplacement(elitismCount);
            default -> throw new IllegalArgumentException("Unsupported mutation method: " + method);
        }
    }


    private void initializeOperators() {
        if(chromosomeType != null) {
            initializeChromosomeType(chromosomeType, geneValueBoundary);
        }

        if (selectionMethod != null) {
            initializeSelectionMethod(selectionMethod);
        }

        if (crossoverMethod != null) {
            initializeCrossoverMethod(crossoverMethod);
        }

        if (mutationMethod != null) {
            initializeMutationMethod(mutationMethod);
        }

        if (replacementMethod != null) {
            initializeReplacementMethod(replacementMethod);
            if(replacementMethod == REPLACEMENT_METHOD.GENERATIONAL) {
                selectionCount = populationSize;
            }
        }


    }

    private boolean shouldTerminate() {
        if (terminationCondition != null && terminationCondition.test(this)) {
            return true;
        }

        if (maxGenerations != 0 && currentGeneration++ >= maxGenerations) {
            return true;
        }

        if (fitnessTarget != 0 && bestFitness >= fitnessTarget) {
            return true;
        }

        return false;
    }

    private void updateBestChromosome() {
        Chromosome newBest = population.get(0);

        for (Chromosome c : population) {
            if (c.getFitness() > newBest.getFitness()) {
                newBest = c;
            }
        }

        if (bestChromosome == null || newBest.getFitness() > bestChromosome.getFitness()) {
            bestChromosome = newBest;
            bestFitness = newBest.getFitness();
        }
    }




    public void displayResults() {
        System.out.println("Results Summary:");
        System.out.println("Generations: " + getCurrentGeneration());
        System.out.println("Best Fitness: " + getBestFitness());
        System.out.println("Best Chromosome: " + getBestChromosome().getGenes());
    }
}
