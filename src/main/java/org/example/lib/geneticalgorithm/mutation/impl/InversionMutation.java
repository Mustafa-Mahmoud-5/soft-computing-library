package org.example.lib.geneticalgorithm.mutation.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.mutation.Mutation;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InversionMutation implements Mutation {

    @Override
    public void mutate(Chromosome chromosome, double mutationRate) {
        Random r = new Random();
        if (r.nextDouble() > mutationRate) {
            return;
        }

        List<Integer> genes = (List<Integer>) chromosome.getGenes();

        int size = genes.size();
        int i = r.nextInt(size);
        int j = r.nextInt(size);

        // nsure i < j
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }

        List<Integer> subList = genes.subList(i, j + 1);
        Collections.reverse(subList);

        chromosome.setGenes(genes);
    }
    }

