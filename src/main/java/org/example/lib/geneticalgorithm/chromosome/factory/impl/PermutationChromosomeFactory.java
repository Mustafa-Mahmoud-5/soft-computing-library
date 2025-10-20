package org.example.lib.geneticalgorithm.chromosome.factory.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.impl.PermutationChromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;

public class PermutationChromosomeFactory implements ChromosomeFactory {
    @Override
    public Chromosome newInstance(int size) {
        return new PermutationChromosome(size);
    }
}
