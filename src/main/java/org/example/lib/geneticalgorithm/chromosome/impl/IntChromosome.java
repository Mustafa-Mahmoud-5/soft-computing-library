package org.example.lib.geneticalgorithm.chromosome.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;

public class IntChromosome extends Chromosome {
    List<Integer> genes;

    public IntChromosome(int size) {
        super(size);
        this.genes = new ArrayList<>();
    }


    @Override
    protected void initializeGenes() {

    }

    @Override
    public List<?> getGenes() {
        return List.of();
    }
}
