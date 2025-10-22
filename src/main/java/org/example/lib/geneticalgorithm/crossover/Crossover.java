package org.example.lib.geneticalgorithm.crossover;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;

import java.util.List;

// return type is a list of size 2 that contains 2 offsprings
public interface Crossover {
    List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory);
}