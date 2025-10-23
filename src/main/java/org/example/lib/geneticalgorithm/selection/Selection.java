package org.example.lib.geneticalgorithm.selection;

import org.example.lib.geneticalgorithm.chromosome.Chromosome;

import java.util.List;

public interface Selection {
    List<Chromosome> select(List<Chromosome> population, int selectCount);

}
