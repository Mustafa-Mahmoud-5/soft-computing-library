package org.example.lib.geneticalgorithm.replacement.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.replacement.Replacement;

import java.util.ArrayList;
import java.util.List;

public class GenerationalReplacement implements Replacement {
    // replace oldPopulation with offspring completely
    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        if(offSpring.size() >= population.size()){
            return new ArrayList<>(offSpring.subList(0, population.size()));
        }
        else{
            List<Chromosome> nextGen = new ArrayList<>(offSpring);
            nextGen.addAll(population.subList(0, population.size() - offSpring.size()));
            return nextGen;
        }
    }
}
