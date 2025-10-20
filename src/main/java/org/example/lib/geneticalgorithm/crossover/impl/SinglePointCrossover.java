package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.crossover.Crossover;

import java.util.List;

public class SinglePointCrossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate) {
        return List.of();
    }
}
