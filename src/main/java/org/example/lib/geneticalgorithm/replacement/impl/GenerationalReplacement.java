package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.List;

public class GenerationalReplacement implements Replacement {
    // replace oldPopulation with offspring completely
    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        return List.of();
    }
}
