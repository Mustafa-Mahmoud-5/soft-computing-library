package org.example.lib.geneticalgorithm.mutation.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.impl.BinaryChromosome;
import org.example.lib.geneticalgorithm.mutation.Mutation;

import java.util.Random;

public class FlipMutation implements Mutation {

    @Override
    public void mutate(Chromosome chromosome, double mutationRate) {
        BinaryChromosome b = (BinaryChromosome) chromosome;
        Random r = new Random();
        for (int i = 0; i < b.getGenes().size(); i++) {
            if (r.nextDouble() < mutationRate) {
                int old =  b.getGenes().get(i);
                if(old==1){
                    b.getGenes().set(i,0);
                }
                else {
                    b.getGenes().set(i, 1);
                }
                }
            }
        }

    }

