package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover implements Crossover {
    
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {

        //if random is less than rate, return parents
        Random randomNumber = new Random();
        if (randomNumber.nextDouble() > crossoverRate) {
            return List.of(p1, p2);
        }
        List<?> p1Genes = p1.getGenes();
        List<?> p2Genes = p2.getGenes();
        int size = p1.getSize();

        int crossOverPoint = randomNumber.nextInt(size-1) +1 ;
        List<Object> c1Genes = new ArrayList<>();
        List<Object> c2Genes = new ArrayList<>();

        //copy p1,p2 into c1
        for (int i=0; i<crossOverPoint;i++){
            c1Genes.add(p1Genes.get(i));
        }
        for (int i=crossOverPoint; i<size;i++){
            c1Genes.add(p2Genes.get(i));
        }

        //copy p2,p1 into c2
        for (int i=0; i<crossOverPoint;i++){
            c2Genes.add(p2Genes.get(i));
        }
        for (int i=crossOverPoint; i<size;i++){
            c2Genes.add(p1Genes.get(i));
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
