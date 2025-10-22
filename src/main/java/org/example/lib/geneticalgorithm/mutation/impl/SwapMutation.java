package org.example.lib.geneticalgorithm.mutation.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.impl.PermutationChromosome;
import org.example.lib.geneticalgorithm.mutation.Mutation;

import java.util.Collections;
import java.util.Random;

public class SwapMutation implements Mutation {
    @Override
    public void mutate(Chromosome chromosome, double mutationRate) {
        PermutationChromosome p = (PermutationChromosome) chromosome;
        Random r = new Random();
        if (r.nextDouble() < mutationRate) {
            int i = r.nextInt(p.getSize());
            int j = r.nextInt(p.getSize());
            Collections.swap(p.getGenes(), i, j);
        }

    }
}
