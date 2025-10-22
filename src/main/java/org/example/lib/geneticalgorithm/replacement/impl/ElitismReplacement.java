package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.List;

public class ElitismReplacement implements Replacement {

    private int elitisimCount;

    public ElitismReplacement(int elitismCount) {
        this.elitisimCount = elitismCount;
    }


    // keep best n from old, fill the rest from new
    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        return List.of();
    }
}
