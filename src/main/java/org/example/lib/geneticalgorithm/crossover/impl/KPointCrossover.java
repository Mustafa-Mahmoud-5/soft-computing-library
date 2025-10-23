package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// i think a Kpoint crossover code can also handle the Single point crossover case
//but you can implement separate ones if you want
public class KPointCrossover implements Crossover {

    private int k;
    
    public KPointCrossover(int k){
        this.k=k;
    }

    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {

        Random randomNumber = new Random();
        // If random number is greater than crossover rate, return parents unchanged
        if(randomNumber.nextDouble() > crossoverRate){
            return List.of(p1,p2);
        }

        List<?> p1Genes = p1.getGenes();
        List<?> p2Genes = p2.getGenes();
        int size = p1.getSize();

        // Generate k random points
        List<Integer> crossoverPoints = new ArrayList<>();
        for (int i=0; i<k; i++) crossoverPoints.add(randomNumber.nextInt((size - 1) + 1));

        List<Object> c1Genes = new ArrayList<>();
        List<Object> c2Genes = new ArrayList<>();

        // Track which parent we're currently copying from
        boolean useP1ForC1 = true;
        int currentIndex = 0;

        // Iterate through each segment defined by crossover points
        for (int i = 0; i <= crossoverPoints.size(); i++) {
            int endIndex = (i < crossoverPoints.size()) ? crossoverPoints.get(i) : size;

            // Copy genes from currentIndex to endIndex
            for (int j = currentIndex; j < endIndex; j++) {
                if (useP1ForC1) {
                    c1Genes.add(p1Genes.get(j));
                    c2Genes.add(p2Genes.get(j));
                } else {
                    c1Genes.add(p2Genes.get(j));
                    c2Genes.add(p1Genes.get(j));
                }
            }

            // Switch parents for next segment
            useP1ForC1 = !useP1ForC1;
            currentIndex = endIndex;
        }

        // Create offspring chromosomes using factory
        Chromosome c1 = factory.newInstance(size);
        Chromosome c2 = factory.newInstance(size);

        // Set the genes for offspring
        c1.setGenes(c1Genes);
        c2.setGenes(c2Genes);

        return List.of(c1, c2);
    }
}
