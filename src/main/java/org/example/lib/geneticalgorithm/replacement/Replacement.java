package org.example.lib.geneticalgorithm.replacement;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.List;

public interface Replacement {
    List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring);
}
