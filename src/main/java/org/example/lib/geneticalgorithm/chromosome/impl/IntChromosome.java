package org.example.lib.geneticalgorithm.chromosome.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntChromosome extends Chromosome {
    private List<Integer> genes;
    private final Random random = new Random();
    private final int boundary;

    public IntChromosome(int size, int boundary) {
        super(size);
        this.genes = new ArrayList<>();
        this.boundary = boundary;
    }

    @Override
    public void initializeGenes() {
        for(int i = 0; i < size; i++) {
            genes.add(random.nextInt(boundary));
        }
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
