package org.example.lib.geneticalgorithm.selection.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.selection.Selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements Selection {

    private final Random random = new Random();

    @Override
    public List<Chromosome> select(List<Chromosome> population, int selectCount) {
        double totalFitness = 0;
        List<Double> fitnesses = new ArrayList<>();
        for(Chromosome chromosome: population) {
            totalFitness+= chromosome.getFitness();
            fitnesses.add(chromosome.getFitness());
        }

        if(totalFitness <= 0) return selectRandom(population, selectCount);

        List<Double> fitnessesPrfx = buildPrefixSumArr(fitnesses);

        List<Chromosome> selected = new ArrayList<>();
        for(int i = 0; i < selectCount; i++) {
            double randomInt = random.nextDouble(totalFitness);
            int j = 0;
            while(randomInt >= fitnessesPrfx.get(j)) {
                j++;
            }
            selected.add(population.get(j));
        }

        return selected;
    }


    private List<Double> buildPrefixSumArr(List<Double> fitnesses) {
        List<Double> prfx = new ArrayList<>();
        prfx.add(fitnesses.get(0));
        for(int i = 1; i < fitnesses.size(); i++) {
            prfx.add(prfx.get(i-1) + fitnesses.get(i));
        }
        return prfx;
    }

    private List<Chromosome> selectRandom(List<Chromosome> population, int selectCount) {
        List<Chromosome> selected = new ArrayList<>();
        for (int i = 0; i < selectCount; i++) {
            selected.add(population.get(random.nextInt(population.size())));
        }
        return selected;
    }
}
