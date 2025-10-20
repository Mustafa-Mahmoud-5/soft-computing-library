package org.example.lib.geneticalgorithm.chromosome.factory.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.impl.IntChromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;

public class IntChromosomeFactory implements ChromosomeFactory {
    @Override
    public Chromosome newInstance(int size) {
        return new IntChromosome(size);
    }
}
