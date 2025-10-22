package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;

import java.util.List;

public class Order1Crossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {
        return List.of();
    }
}
