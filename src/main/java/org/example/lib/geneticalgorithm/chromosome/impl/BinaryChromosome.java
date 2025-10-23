package org.example.lib.geneticalgorithm.chromosome.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinaryChromosome extends Chromosome {
    private List<Integer> genes; // 0/1
    private final Random random = new Random();

    public BinaryChromosome(int size) {
        super(size);
        this.genes = new ArrayList<>();
    }

    @Override
    public void initializeGenes() {
        for(int i = 0; i < size; i++) {
            genes.add(random.nextInt(2));
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
                int value = (Integer) gene;
                if (value != 0 && value != 1) {
                    throw new IllegalArgumentException("Binary chromosome genes must be 0 or 1");
                }
                this.genes.add(value);
            } else {
                throw new IllegalArgumentException("Binary chromosome genes must be integers");
            }
        }
    }


}
