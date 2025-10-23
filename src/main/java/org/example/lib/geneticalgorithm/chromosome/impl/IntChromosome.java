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
    public List<Integer> getGenes() {
        return this.genes;
    }

    @Override
    public void setGenes(List<?> genes) {
        this.genes = new ArrayList<>();
        for (Object gene : genes) {
            if (gene instanceof Integer) {
                this.genes.add((Integer) gene);
            } else {
                throw new IllegalArgumentException("IntChromosome genes must be integers");
            }
        }
    }
}
