package org.example.lib.geneticalgorithm.mutation;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

public interface Mutation {
    void mutate(Chromosome chromosome, double mutationRate);
}
