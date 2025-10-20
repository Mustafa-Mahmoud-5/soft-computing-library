package org.example.lib.geneticalgorithm.chromosome.factory;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

public interface ChromosomeFactory {
    public Chromosome newInstance(int size);
}
