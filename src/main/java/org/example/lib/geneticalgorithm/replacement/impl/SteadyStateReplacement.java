package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SteadyStateReplacement implements Replacement {
    int replaceCount;

    // replace few of the worst(replaceCount) with new offspring
    SteadyStateReplacement(int replaceCount) {
        this.replaceCount = replaceCount;
    }

    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        population.sort(Comparator.comparingDouble(Chromosome::getFitness));
        offSpring.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        for(int i = 0; i < replaceCount && i < offSpring.size(); i++){
            population.set(i, offSpring.get(i));
        }

        return new ArrayList<>(population);
    }
}
