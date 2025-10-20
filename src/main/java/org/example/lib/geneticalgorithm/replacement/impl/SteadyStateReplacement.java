package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.List;

public class SteadyStateReplacement implements Replacement {
    int replaceCount;

    // replace few of the worst(replaceCount) with new offspring
    SteadyStateReplacement(int replaceCount) {
        this.replaceCount = replaceCount;
    }

    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        return List.of();
    }
}
