package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;

import java.util.List;

// i think a Kpoint crossover code can also handle the Single point crossover case
//but you can implement separate ones if you want
public class KPointCrossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {
        return List.of();
    }
}
