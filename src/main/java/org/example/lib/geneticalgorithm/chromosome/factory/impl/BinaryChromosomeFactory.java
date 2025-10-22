package org.example.lib.geneticalgorithm.chromosome.factory.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.chromosome.impl.BinaryChromosome;

public class BinaryChromosomeFactory implements ChromosomeFactory {

    @Override
    public Chromosome newInstance(int size) {
        return new BinaryChromosome(size);
    }
}
