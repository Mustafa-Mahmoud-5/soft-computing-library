package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ElitismReplacement implements Replacement {

    private final int elitisimCount;

    public ElitismReplacement(int elitismCount) {
        this.elitisimCount = elitismCount;
    }


    // keep best n from old, fill the rest from new
    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {

        population.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        offSpring.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        List<Chromosome> nextGeneration = new ArrayList<>(population.subList(0, elitisimCount));

        int remaining = population.size()- elitisimCount;
        nextGeneration.addAll(offSpring.subList(0, Math.min(remaining, offSpring.size())));

        return nextGeneration;

    }
}
