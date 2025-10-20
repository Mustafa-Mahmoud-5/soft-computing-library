package org.example.lib.geneticalgorithm.chromosome;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@Setter
public abstract class Chromosome {
    protected int size;
    protected double fitness;

    public Chromosome(int size) {
        this.size = size;
        initializeGenes();
    }
    protected abstract void initializeGenes();
    public abstract List<?> getGenes();
}
