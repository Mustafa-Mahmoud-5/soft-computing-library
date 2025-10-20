package org.example;

import org.example.lib.geneticalgorithm.GeneticAlgorithm;
import org.example.lib.geneticalgorithm.enums.CHROMOSOME_TYPE;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.BINARY);
        geneticAlgorithm.run();
    }
}