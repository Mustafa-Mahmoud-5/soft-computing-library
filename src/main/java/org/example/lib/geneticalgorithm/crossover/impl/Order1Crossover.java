package org.example.lib.geneticalgorithm.crossover.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.chromosome.factory.ChromosomeFactory;
import org.example.lib.geneticalgorithm.crossover.Crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order1Crossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {

        Random randomNumber= new Random();
        if(randomNumber.nextDouble()>crossoverRate){
            return List.of(p1,p2);
        }

        List<?> p1Genes= p1.getGenes();
        List<?> p2Genes= p2.getGenes();
        int size= p1.getSize();

        //2 random points
        int point1= randomNumber.nextInt(size);
        int point2= randomNumber.nextInt(size);

        int start= Math.min(point1,point2);
        int end = Math.max(point1,point2);

        List<Object> child1 = new ArrayList<>();

        // Initialize with nulls
        for (int i = 0; i < size; i++) {
            child1.add(null);
        }

        // Copy from start to end from p1
        for (int i = start; i <= end; i++) {
            child1.set(i, p1Genes.get(i));
        }


        // index so we can loop around and copy from p2
        int child1Index = (end + 1) % size;

        for (int i = 0; i < size; i++) {
            int p2Index = (end + 1 + i) % size;
            Object gene = p2Genes.get(p2Index);

            if (!child1.contains(gene)) {
                child1.set(child1Index, gene);
                child1Index = (child1Index + 1) % size;
            }
        }

        List<Object> child2 = new ArrayList<>();

        // Initialize with nulls
        for (int i = 0; i < size; i++) {
            child2.add(null);
        }

        // Copy segment from parent2
        for (int i = start; i <= end; i++) {
            child2.set(i, p2Genes.get(i));
        }

        int child2Index = (end + 1) % size;

        for (int i = 0; i < size; i++) {
            int p1Index = (end + 1 + i) % size;
            Object gene = p1Genes.get(p1Index);

            if (!child2.contains(gene)) {
                child2.set(child2Index, gene);
                child2Index = (child2Index + 1) % size;
            }
        }

        // Create child chromosomes using factory
        Chromosome c1 = factory.newInstance(size);
        Chromosome c2 = factory.newInstance(size);

        // Set the genes for children
        c1.setGenes(child1);
        c2.setGenes(child2);

        return List.of(c1, c2);
    }
}
