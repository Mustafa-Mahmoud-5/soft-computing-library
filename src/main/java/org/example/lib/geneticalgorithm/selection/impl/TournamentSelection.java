package org.example.lib.geneticalgorithm.selection.impl;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;
import org.example.lib.geneticalgorithm.selection.Selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TournamentSelection implements Selection {
    private final int numberOfParents;
    private final int tournamentSize;
    private final Random random;


    public TournamentSelection(int numberOfParents, int tournamentSize) {
        this.numberOfParents = numberOfParents;
        this.tournamentSize = tournamentSize;
        this.random = new Random();
    }

    @Override
    public List<Chromosome> select(List<Chromosome> population) {
        if (population == null || population.isEmpty()) {
            return new ArrayList<>();
        }

        // Ensure tournament size
        int actualTournamentSize = Math.min(tournamentSize, population.size());

        // Run tournaments to select the specified number of parents
        List<Chromosome> selectedParents = new ArrayList<>();
        for (int i = 0; i < numberOfParents; i++) {
            Chromosome winner = runTournament(population, actualTournamentSize);
            selectedParents.add(winner);
        }

        return selectedParents;
    }

    private Chromosome runTournament(List<Chromosome> population, int actualTournamentSize) {
        // Randomly select chromosomes for the tournament
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < actualTournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }

        // Find the chromosome with the highest fitness
        Chromosome best = tournament.get(0);
        for (int i = 1; i < tournament.size(); i++) {
            Chromosome current = tournament.get(i);
            if (current.getFitness() > best.getFitness()) {
                best = current;
            }
        }

        return best;
    }
}
