package org.example.lib.geneticalgorithm.chromosome.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;

public class BinaryChromosome extends Chromosome {
    List<Integer> genes; // 0/1

    public BinaryChromosome(int size) {
        super(size);
        this.genes = new ArrayList<>();
    }

    @Override
    protected void initializeGenes() {
    }

    @Override
    public List<Integer> getGenes() {
        return List.of();
    }
}
