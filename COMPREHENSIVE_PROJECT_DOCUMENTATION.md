# Comprehensive Project Documentation
## Soft Computing Library - Complete Code Reference

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture and Design Patterns](#architecture-and-design-patterns)
3. [Core Framework](#core-framework)
4. [Chromosome System](#chromosome-system)
5. [Selection Operators](#selection-operators)
6. [Crossover Operators](#crossover-operators)
7. [Mutation Operators](#mutation-operators)
8. [Replacement Strategies](#replacement-strategies)
9. [Configuration Enums](#configuration-enums)
10. [Usage Example](#usage-example)
11. [Complete Code Walkthrough](#complete-code-walkthrough)

---

## Project Overview

### What is This Project?

The **Soft Computing Library** is a Java-based framework for implementing **Genetic Algorithms (GA)**, which are optimization algorithms inspired by natural evolution. The library provides a flexible, modular system where you can:

- Solve optimization problems like the Traveling Salesman Problem (TSP)
- Configure different genetic operators (selection, crossover, mutation)
- Work with different chromosome encodings (binary, integer, permutation)
- Use various replacement strategies to evolve populations

### Key Technologies
- **Language**: Java 17
- **Build Tool**: Maven
- **Dependencies**: Lombok 1.18.42 (for reducing boilerplate code)
- **Design Approach**: Strategy Pattern, Factory Pattern, Interface-based design

### How Genetic Algorithms Work

A genetic algorithm mimics natural evolution:

1. **Initialization**: Create a random population of potential solutions (chromosomes)
2. **Evaluation**: Calculate fitness (how good each solution is)
3. **Selection**: Choose parents based on fitness
4. **Crossover**: Combine parents to create offspring
5. **Mutation**: Randomly modify offspring to add diversity
6. **Replacement**: Form new population from old population and offspring
7. **Repeat**: Continue until termination condition is met

---

## Architecture and Design Patterns

### Overall Architecture

The library uses a **layered architecture**:

```
┌─────────────────────────────────────────┐
│         Application Layer               │
│         (Main.java)                     │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│      Genetic Algorithm Orchestrator     │
│      (GeneticAlgorithm.java)           │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│        Genetic Operators Layer          │
│  Selection | Crossover | Mutation       │
│        Replacement                      │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│       Chromosome & Factory Layer        │
│  Chromosome Types | Factories           │
└─────────────────────────────────────────┘
```

### Design Patterns Used

#### 1. Strategy Pattern
Each genetic operator (Selection, Crossover, Mutation, Replacement) is defined as an **interface** with multiple implementations. This allows you to swap algorithms at runtime.

**Example**:
- `Selection` interface → `TournamentSelection`, `RouletteWheelSelection`
- `Crossover` interface → `SinglePointCrossover`, `KPointCrossover`, `Order1Crossover`

#### 2. Factory Pattern
The `ChromosomeFactory` interface abstracts the creation of different chromosome types.

**Example**:
- `ChromosomeFactory` interface → `BinaryChromosomeFactory`, `IntChromosomeFactory`, `PermutationChromosomeFactory`

#### 3. Template Method Pattern
The `GeneticAlgorithm.run()` method defines the skeleton of the algorithm, calling specific operators at each step.

---

## Core Framework

### 1. Algorithm Interface
**Location**: `src/main/java/org/example/lib/Algorithm.java`

```java
public interface Algorithm {
    public void run();
}
```

**Purpose**: Base interface for all algorithms in the library. Currently, only genetic algorithms are implemented, but this allows for future expansion (e.g., Particle Swarm Optimization, Simulated Annealing).

**Key Concept**: Defines a contract that all algorithm implementations must provide a `run()` method.

---

### 2. GeneticAlgorithm Class
**Location**: `src/main/java/org/example/lib/geneticalgorithm/GeneticAlgorithm.java`

**Purpose**: The main orchestrator that manages the entire genetic algorithm lifecycle.

#### Key Components

##### Fields

```java
// Population and mating pool
private List<Chromosome> population = new ArrayList<>();
private List<Chromosome> matingPool = new ArrayList<>();

// Operators (Strategy Pattern)
private ChromosomeFactory chromosomeFactory;
private Selection selectionOperator;
private Crossover crossoverOperator;
private Mutation mutationOperator;
private Replacement replacementOperator;

// Configuration enums
private CHROMOSOME_TYPE chromosomeType;
private SELECTION_METHOD selectionMethod;
private CROSSOVER_METHOD crossoverMethod;
private MUTATION_METHOD mutationMethod;
private REPLACEMENT_METHOD replacementMethod;

// Statistical data
private Chromosome bestChromosome;
private double avgFitness = 0;
private double bestFitness = 0;
private int currentGeneration = 0;

// Configuration parameters
private int populationSize = 10;
private int chromosomeSize = 5;
private int maxGenerations = 200;
private double crossoverRate = 0.6;
private double mutationRate = 0.1;
private int elitismCount = 0;
private int steadyStateReplacementCount = 2;
private int crossoverPoints = 1;
private int selectionCount = 10;
private int geneValueBoundary = 10;
private double fitnessTarget;

// User-defined functions
private Predicate<Chromosome> isFeasibleChromosomePredicate;
private Function<Chromosome, Double> fitnessFunction;
private Predicate<GeneticAlgorithm> terminationCondition;
```

**Explanation**:
- **population**: The current generation of chromosomes
- **matingPool**: Selected chromosomes that will produce offspring
- **Operator fields**: Hold instances of strategy implementations
- **Configuration parameters**: Control the behavior of the algorithm
- **Statistical data**: Track progress and best solutions
- **User-defined functions**: Allow custom fitness evaluation and feasibility checking

##### Main Algorithm Flow

```java
@Override
public void run() {
    initializeOperators();

    initializePopulation();
    calculatePopulationFitness();
    updateBestChromosome();

    while (!shouldTerminate()) {
        applySelection();
        generate(); // crossover + mutation
        applyReplacement();
        calculatePopulationFitness();
        updateBestChromosome();
    }
}
```

**Step-by-step explanation**:

1. **initializeOperators()**: Creates instances of all operator classes based on configuration enums
2. **initializePopulation()**: Creates `populationSize` random chromosomes using the factory
3. **calculatePopulationFitness()**: Evaluates each chromosome using the fitness function
4. **updateBestChromosome()**: Finds and stores the best chromosome seen so far
5. **Main loop**:
   - **applySelection()**: Selects parents for reproduction
   - **generate()**: Applies crossover and mutation to create offspring
   - **applyReplacement()**: Forms the new generation
   - **calculatePopulationFitness()**: Evaluates new chromosomes
   - **updateBestChromosome()**: Updates best solution if improved
6. **shouldTerminate()**: Checks if max generations reached or fitness target met

##### Key Methods Explained

**initializePopulation()**
```java
private void initializePopulation() {
    if(!population.isEmpty()) population.clear();
    for(int i = 0; i < populationSize; i++) {
        Chromosome chromosome = chromosomeFactory.newInstance(chromosomeSize);
        chromosome.initializeGenes();
        System.out.println(chromosome.getGenes());
        population.add(chromosome);
    }
}
```
- Clears any existing population
- Creates `populationSize` new chromosomes using the factory
- Initializes genes randomly for each chromosome
- Adds all chromosomes to the population list

**calculatePopulationFitness()**
```java
private void calculatePopulationFitness() {
    for(int i = 0; i < populationSize; i++) {
        double fitness = fitnessFunction.apply(population.get(i));
        population.get(i).setFitness(fitness);
    }
}
```
- Iterates through all chromosomes
- Applies the user-defined fitness function to each
- Stores the fitness value in the chromosome

**applySelection()**
```java
private void applySelection() {
    matingPool.clear();
    matingPool = selectionOperator.select(population, selectionCount);
}
```
- Clears the previous mating pool
- Uses the selection operator to choose parents
- The number of selected chromosomes is `selectionCount`

**generate()**
```java
private void generate() {
    for(int i = 0; i < matingPool.size(); i+=2) {
        List<Chromosome> children = crossoverOperator.crossover(
            matingPool.get(i),
            matingPool.get(i+1),
            crossoverRate,
            chromosomeFactory
        );
        Chromosome c1 = children.get(0);
        Chromosome c2 = children.get(1);

        mutationOperator.mutate(c1, mutationRate);
        mutationOperator.mutate(c2, mutationRate);

        matingPool.set(i, c1);
        matingPool.set(i+1, c2);
    }

    if(matingPool.size() %2 == 1) {
        Chromosome lastChromosome = matingPool.get(matingPool.size() - 1);
        mutationOperator.mutate(lastChromosome, mutationRate);
    }
}
```
**Explanation**:
- Pairs up chromosomes in the mating pool (i and i+1)
- Applies crossover to each pair to create two children
- Applies mutation to both children
- Replaces parents in the mating pool with mutated children
- If there's an odd number of chromosomes, mutates the last one

**applyReplacement()**
```java
private void applyReplacement() {
    population = replacementOperator.replace(population, matingPool);
}
```
- Uses the replacement strategy to form the next generation
- The old population and offspring (mating pool) are combined according to the strategy

**shouldTerminate()**
```java
private boolean shouldTerminate() {
    if (terminationCondition != null && terminationCondition.test(this)) {
        return true;
    }

    if (maxGenerations != 0 && currentGeneration++ >= maxGenerations) {
        return true;
    }

    if (fitnessTarget != 0 && bestFitness >= fitnessTarget) {
        return true;
    }

    return false;
}
```
**Termination conditions**:
1. Custom termination condition (if provided by user)
2. Maximum generations reached
3. Fitness target achieved

**updateBestChromosome()**
```java
private void updateBestChromosome() {
    Chromosome newBest = population.get(0);

    for (Chromosome c : population) {
        if (c.getFitness() > newBest.getFitness()) {
            newBest = c;
        }
    }

    if (bestChromosome == null || newBest.getFitness() > bestChromosome.getFitness()) {
        bestChromosome = newBest;
        bestFitness = newBest.getFitness();
    }
}
```
- Finds the best chromosome in the current population
- Compares it with the all-time best
- Updates the all-time best if the current best is better

##### Operator Initialization Methods

**initializeChromosomeType()**
```java
private void initializeChromosomeType(CHROMOSOME_TYPE type, Object ... params) {
    switch (type) {
        case BINARY -> chromosomeFactory = new BinaryChromosomeFactory();
        case PERMUTATION -> chromosomeFactory = new PermutationChromosomeFactory();
        case INTEGER -> {
            int randomIntBoundary = 100;
            if(params.length > 0 && params[0] instanceof Integer) {
                randomIntBoundary = (Integer) params[0];
            }
            chromosomeFactory = new IntChromosomeFactory(randomIntBoundary);
        }
        default -> throw new IllegalArgumentException("Unsupported chromosome type: " + type);
    }
}
```
- Creates the appropriate factory based on the chromosome type
- For INTEGER type, accepts an optional boundary parameter

**initializeSelectionMethod()**
```java
private void initializeSelectionMethod(SELECTION_METHOD method) {
    switch (method) {
        case ROULETTE_WHEEL -> selectionOperator = new RouletteWheelSelection();
        case TOURNAMENT -> selectionOperator = new TournamentSelection(2, 3);
        default -> throw new IllegalArgumentException("Unsupported selection type: " + method);
    }
}
```
- Creates the appropriate selection operator
- Tournament selection uses hardcoded values (2 parents, tournament size 3)

**initializeCrossoverMethod()**
```java
private void initializeCrossoverMethod(CROSSOVER_METHOD method) {
    switch (method) {
        case SINGLE_POINT -> crossoverOperator = new SinglePointCrossover();
        case K_POINT -> {
            if(crossoverPoints < 1)
                throw new IllegalArgumentException("Kpoint crossover requires crossoverPoints param set");
            crossoverOperator = new KPointCrossover(crossoverPoints);
        }
        case ORDER1 -> crossoverOperator = new Order1Crossover();
        default -> throw new IllegalArgumentException("Unsupported crossover method: " + method);
    }
}
```
- Creates the appropriate crossover operator
- K_POINT requires `crossoverPoints` to be set

**initializeMutationMethod()**
```java
private void initializeMutationMethod(MUTATION_METHOD method) {
    switch (method) {
        case FLIP -> mutationOperator = new FlipMutation();
        case SWAP -> mutationOperator = new SwapMutation();
        default -> throw new IllegalArgumentException("Unsupported mutation method: " + method);
    }
}
```
- Creates the appropriate mutation operator

**initializeReplacementMethod()**
```java
private void initializeReplacementMethod(REPLACEMENT_METHOD method) {
    switch (method) {
        case GENERATIONAL -> replacementOperator = new GenerationalReplacement();
        case STEADY_STATE -> replacementOperator = new SteadyStateReplacement(steadyStateReplacementCount);
        case ELITISM -> replacementOperator = new ElitismReplacement(elitismCount);
        default -> throw new IllegalArgumentException("Unsupported mutation method: " + method);
    }
}
```
- Creates the appropriate replacement strategy
- STEADY_STATE and ELITISM require count parameters

---

## Chromosome System

### 1. Chromosome Abstract Class
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/Chromosome.java`

```java
@Getter
@Setter
public abstract class Chromosome {
    protected int size;
    protected double fitness;

    public Chromosome(int size) {
        this.size = size;
    }

    public abstract void initializeGenes();
    public abstract List<?> getGenes();
    public abstract void setGenes(List<?> genes);
}
```

**Purpose**: Base class for all chromosome types. A chromosome represents a potential solution in the genetic algorithm.

**Key Concepts**:
- **size**: Number of genes in the chromosome
- **fitness**: Quality of the solution (higher is better)
- **initializeGenes()**: Creates random genes
- **getGenes()**: Returns the list of genes
- **setGenes()**: Sets genes to specific values (used in crossover)

**Why abstract?**: Different problems require different encodings. For example:
- Binary problems (0/1 values)
- Integer problems (integer ranges)
- Permutation problems (unique ordering, like TSP)

---

### 2. BinaryChromosome
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/BinaryChromosome.java`

```java
public class BinaryChromosome extends Chromosome {
    private List<Integer> genes; // 0/1
    private final Random random = new Random();

    public BinaryChromosome(int size) {
        super(size);
        this.genes = new ArrayList<>();
    }

    @Override
    public void initializeGenes() {
        for(int i = 0; i < size; i++) {
            genes.add(random.nextInt(2)); // 0 or 1
        }
    }

    @Override
    public List<Integer> getGenes() {
        return this.genes;
    }

    @Override
    public void setGenes(List<?> genes) {
        this.genes = new ArrayList<>();
        for (Object gene : genes) {
            if (gene instanceof Integer) {
                int value = (Integer) gene;
                if (value != 0 && value != 1) {
                    throw new IllegalArgumentException("Binary chromosome genes must be 0 or 1");
                }
                this.genes.add(value);
            } else {
                throw new IllegalArgumentException("Binary chromosome genes must be integers");
            }
        }
    }
}
```

**Use Cases**:
- Knapsack problem (0 = don't include item, 1 = include item)
- Feature selection (0 = don't use feature, 1 = use feature)
- Any problem with binary decisions

**Key Points**:
- Genes are restricted to 0 or 1
- `setGenes()` validates that all genes are binary
- Random initialization uses `random.nextInt(2)` to generate 0 or 1

---

### 3. IntChromosome
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/IntChromosome.java`

```java
public class IntChromosome extends Chromosome {
    private List<Integer> genes;
    private final Random random = new Random();
    private final int boundary;

    public IntChromosome(int size, int boundary) {
        super(size);
        this.genes = new ArrayList<>();
        this.boundary = boundary;
    }

    @Override
    public void initializeGenes() {
        for(int i = 0; i < size; i++) {
            genes.add(random.nextInt(boundary));
        }
    }

    @Override
    public List<Integer> getGenes() {
        return this.genes;
    }

    @Override
    public void setGenes(List<?> genes) {
        this.genes = new ArrayList<>();
        for (Object gene : genes) {
            if (gene instanceof Integer) {
                this.genes.add((Integer) gene);
            } else {
                throw new IllegalArgumentException("IntChromosome genes must be integers");
            }
        }
    }
}
```

**Use Cases**:
- Scheduling problems (time slots)
- Resource allocation (quantities)
- Parameter optimization with discrete values

**Key Points**:
- **boundary**: Maximum value for genes (genes are in range [0, boundary))
- Random initialization generates integers from 0 to boundary-1
- More flexible than binary chromosomes

---

### 4. PermutationChromosome
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/PermutationChromosome.java`

```java
public class PermutationChromosome extends Chromosome {
    List<Integer> genes;

    public PermutationChromosome(int size) {
        super(size);
        this.genes = new ArrayList<>();
    }

    @Override
    public void initializeGenes() {
        for(int i = 0; i < size; i++) {
            genes.add(i);
        }
        Collections.shuffle(genes);
    }

    @Override
    public List<Integer> getGenes() {
        return this.genes;
    }

    @Override
    public void setGenes(List<?> genes) {
        this.genes = new ArrayList<>();
        for (Object gene : genes) {
            if (gene instanceof Integer) {
                this.genes.add((Integer) gene);
            } else {
                throw new IllegalArgumentException("PermutationChromosome genes must be integers");
            }
        }
    }
}
```

**Use Cases**:
- Traveling Salesman Problem (TSP) - order of cities to visit
- Job scheduling - order of tasks
- Any problem requiring a unique ordering

**Key Points**:
- Genes are a permutation of [0, 1, 2, ..., size-1]
- All values are unique (no duplicates)
- Random initialization creates the sequence [0...size-1] and shuffles it
- Requires special crossover operators (like Order1) to maintain permutation property

---

### 5. ChromosomeFactory Interface
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/ChromosomeFactory.java`

```java
public interface ChromosomeFactory {
    public Chromosome newInstance(int size);
}
```

**Purpose**: Factory pattern to create chromosome instances without knowing the concrete type.

**Why Factory Pattern?**:
- Decouples chromosome creation from the genetic algorithm
- Makes it easy to add new chromosome types
- Allows operators (crossover, mutation) to create new chromosomes without knowing their type

---

### 6. BinaryChromosomeFactory
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/BinaryChromosomeFactory.java`

```java
public class BinaryChromosomeFactory implements ChromosomeFactory {
    @Override
    public Chromosome newInstance(int size) {
        return new BinaryChromosome(size);
    }
}
```

**Purpose**: Creates instances of `BinaryChromosome`.

---

### 7. IntChromosomeFactory
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/IntChromosomeFactory.java`

```java
public class IntChromosomeFactory implements ChromosomeFactory {
    private int boundary;

    public IntChromosomeFactory() {}

    public IntChromosomeFactory(int boundary) {
        this.boundary = boundary;
    }

    @Override
    public Chromosome newInstance(int size) {
        return new IntChromosome(size, boundary);
    }
}
```

**Purpose**: Creates instances of `IntChromosome` with a specified boundary.

**Key Point**: Stores the boundary value so all created chromosomes use the same range.

---

### 8. PermutationChromosomeFactory
**Location**: `src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/PermutationChromosomeFactory.java`

```java
public class PermutationChromosomeFactory implements ChromosomeFactory {
    @Override
    public Chromosome newInstance(int size) {
        return new PermutationChromosome(size);
    }
}
```

**Purpose**: Creates instances of `PermutationChromosome`.

---

## Selection Operators

Selection determines which chromosomes become parents for the next generation.

### 1. Selection Interface
**Location**: `src/main/java/org/example/lib/geneticalgorithm/selection/Selection.java`

```java
public interface Selection {
    List<Chromosome> select(List<Chromosome> population, int selectCount);
}
```

**Purpose**: Defines the contract for all selection methods.

**Parameters**:
- **population**: The current generation
- **selectCount**: Number of chromosomes to select

**Returns**: A mating pool (list of selected chromosomes)

---

### 2. RouletteWheelSelection
**Location**: `src/main/java/org/example/lib/geneticalgorithm/selection/impl/RouletteWheelSelection.java`

```java
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

        if(totalFitness == 0) return selectRandom(population, selectCount);

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
```

**Concept**: Fitness-proportionate selection. Think of a roulette wheel where each chromosome occupies a slice proportional to its fitness. Higher fitness = larger slice = higher probability of selection.

**How it works**:
1. Calculate total fitness of all chromosomes
2. Build a prefix sum array for efficient selection
3. For each selection:
   - Generate a random number between 0 and total fitness
   - Find which chromosome's slice contains this number
   - Add that chromosome to the mating pool

**Example**:
```
Chromosome A: fitness = 10
Chromosome B: fitness = 20
Chromosome C: fitness = 30
Total fitness = 60

Prefix sum array: [10, 30, 60]

Random number 15 → falls in B's range (10-30)
Random number 45 → falls in C's range (30-60)
Random number 5 → falls in A's range (0-10)
```

**Edge Case**: If total fitness is 0, falls back to random selection.

---

### 3. TournamentSelection
**Location**: `src/main/java/org/example/lib/geneticalgorithm/selection/impl/TournamentSelection.java`

```java
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
    public List<Chromosome> select(List<Chromosome> population, int selectCount) {
        if (population == null || population.isEmpty()) {
            return new ArrayList<>();
        }

        int actualTournamentSize = Math.min(tournamentSize, population.size());

        List<Chromosome> selectedParents = new ArrayList<>();
        for (int i = 0; i < numberOfParents; i++) {
            Chromosome winner = runTournament(population, actualTournamentSize);
            selectedParents.add(winner);
        }

        return selectedParents;
    }

    private Chromosome runTournament(List<Chromosome> population, int actualTournamentSize) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < actualTournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }

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
```

**Concept**: Run multiple "tournaments" where a few chromosomes compete, and the best one wins.

**How it works**:
1. Repeat `numberOfParents` times:
   - Randomly select `tournamentSize` chromosomes
   - Find the one with the best fitness
   - Add it to the mating pool

**Example with tournamentSize = 3**:
```
Tournament 1: [C3 (fitness=15), C7 (fitness=22), C2 (fitness=8)]
Winner: C7 (highest fitness)

Tournament 2: [C1 (fitness=30), C5 (fitness=12), C9 (fitness=18)]
Winner: C1 (highest fitness)
```

**Advantages**:
- Simple and efficient
- Less susceptible to premature convergence than roulette wheel
- Tournament size controls selection pressure (larger = more pressure)

---

## Crossover Operators

Crossover combines two parent chromosomes to create offspring.

### 1. Crossover Interface
**Location**: `src/main/java/org/example/lib/geneticalgorithm/crossover/Crossover.java`

```java
public interface Crossover {
    List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory);
}
```

**Purpose**: Defines the contract for all crossover methods.

**Parameters**:
- **p1, p2**: Parent chromosomes
- **crossoverRate**: Probability of performing crossover (0.0 to 1.0)
- **factory**: Factory to create offspring chromosomes

**Returns**: List of 2 offspring chromosomes

**Why crossoverRate?**: Sometimes we want to pass parents unchanged to the next generation to preserve good solutions.

---

### 2. SinglePointCrossover
**Location**: `src/main/java/org/example/lib/geneticalgorithm/crossover/impl/SinglePointCrossover.java`

```java
public class SinglePointCrossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {
        Random randomNumber = new Random();
        if (randomNumber.nextDouble() > crossoverRate) {
            return List.of(p1, p2); // Return parents unchanged
        }

        List<?> p1Genes = p1.getGenes();
        List<?> p2Genes = p2.getGenes();
        int size = p1.getSize();

        int crossOverPoint = randomNumber.nextInt(size-1) + 1;
        List<Object> c1Genes = new ArrayList<>();
        List<Object> c2Genes = new ArrayList<>();

        // Child 1: First part from p1, second part from p2
        for (int i=0; i<crossOverPoint; i++){
            c1Genes.add(p1Genes.get(i));
        }
        for (int i=crossOverPoint; i<size; i++){
            c1Genes.add(p2Genes.get(i));
        }

        // Child 2: First part from p2, second part from p1
        for (int i=0; i<crossOverPoint; i++){
            c2Genes.add(p2Genes.get(i));
        }
        for (int i=crossOverPoint; i<size; i++){
            c2Genes.add(p1Genes.get(i));
        }

        Chromosome c1 = factory.newInstance(size);
        Chromosome c2 = factory.newInstance(size);
        c1.setGenes(c1Genes);
        c2.setGenes(c2Genes);

        return List.of(c1, c2);
    }
}
```

**Concept**: Choose a random point and swap the tails of the two parents.

**Visual Example**:
```
Parent 1: [1, 0, 1, 1, 0]
Parent 2: [0, 1, 0, 0, 1]

Crossover point: 2

Child 1: [1, 0 | 0, 0, 1]  (first part from P1, second from P2)
Child 2: [0, 1 | 1, 1, 0]  (first part from P2, second from P1)
```

**Use Cases**: Works well for binary and integer chromosomes.

**Note**: `crossOverPoint` ranges from 1 to size-1 to ensure both parts have at least one gene.

---

### 3. KPointCrossover
**Location**: `src/main/java/org/example/lib/geneticalgorithm/crossover/impl/KPointCrossover.java`

```java
public class KPointCrossover implements Crossover {
    private int k;

    public KPointCrossover(int k){
        this.k = k;
    }

    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {
        Random randomNumber = new Random();
        if(randomNumber.nextDouble() > crossoverRate){
            return List.of(p1, p2);
        }

        List<?> p1Genes = p1.getGenes();
        List<?> p2Genes = p2.getGenes();
        int size = p1.getSize();

        // Generate k random crossover points
        List<Integer> crossoverPoints = new ArrayList<>();
        for (int i=0; i<k; i++)
            crossoverPoints.add(randomNumber.nextInt((size - 1) + 1));

        List<Object> c1Genes = new ArrayList<>();
        List<Object> c2Genes = new ArrayList<>();

        boolean useP1ForC1 = true;
        int currentIndex = 0;

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

            useP1ForC1 = !useP1ForC1; // Switch parents
            currentIndex = endIndex;
        }

        Chromosome c1 = factory.newInstance(size);
        Chromosome c2 = factory.newInstance(size);
        c1.setGenes(c1Genes);
        c2.setGenes(c2Genes);

        return List.of(c1, c2);
    }
}
```

**Concept**: Generalization of single-point crossover with k points instead of 1. Parents alternate after each crossover point.

**Visual Example (k=2)**:
```
Parent 1: [1, 0, 1, 1, 0, 1]
Parent 2: [0, 1, 0, 0, 1, 0]

Crossover points: 2, 4

Segments: [0:2], [2:4], [4:6]

Child 1: [1, 0 | 0, 0 | 0, 1]  (P1 | P2 | P1)
Child 2: [0, 1 | 1, 1 | 1, 0]  (P2 | P1 | P2)
```

**Use Cases**: Provides more mixing than single-point crossover, useful for problems where genes have less positional dependency.

---

### 4. Order1Crossover (OX1)
**Location**: `src/main/java/org/example/lib/geneticalgorithm/crossover/impl/Order1Crossover.java`

```java
public class Order1Crossover implements Crossover {
    @Override
    public List<Chromosome> crossover(Chromosome p1, Chromosome p2, double crossoverRate, ChromosomeFactory factory) {
        Random randomNumber = new Random();
        if(randomNumber.nextDouble() > crossoverRate){
            return List.of(p1, p2);
        }

        List<?> p1Genes = p1.getGenes();
        List<?> p2Genes = p2.getGenes();
        int size = p1.getSize();

        // Select two random points
        int point1 = randomNumber.nextInt(size);
        int point2 = randomNumber.nextInt(size);
        int start = Math.min(point1, point2);
        int end = Math.max(point1, point2);

        // Create child 1
        List<Object> child1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            child1.add(null);
        }

        // Copy segment from parent1
        for (int i = start; i <= end; i++) {
            child1.set(i, p1Genes.get(i));
        }

        // Fill remaining positions with genes from parent2
        int child1Index = (end + 1) % size;
        for (int i = 0; i < size; i++) {
            int p2Index = (end + 1 + i) % size;
            Object gene = p2Genes.get(p2Index);

            if (!child1.contains(gene)) {
                child1.set(child1Index, gene);
                child1Index = (child1Index + 1) % size;
            }
        }

        // Create child 2 (same process with parents swapped)
        List<Object> child2 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            child2.add(null);
        }

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

        Chromosome c1 = factory.newInstance(size);
        Chromosome c2 = factory.newInstance(size);
        c1.setGenes(child1);
        c2.setGenes(child2);

        return List.of(c1, c2);
    }
}
```

**Concept**: Designed specifically for **permutation chromosomes**. Preserves relative order from one parent while filling gaps with elements from the other parent.

**Visual Example**:
```
Parent 1: [1, 2, 3, 4, 5, 6, 7, 8]
Parent 2: [3, 7, 5, 1, 6, 8, 2, 4]

Random points: start=3, end=6

Step 1: Copy segment [start:end] from P1 to child1
Child1: [_, _, _, 4, 5, 6, 7, _]

Step 2: Fill remaining positions with P2's order, starting after end
P2 starting at position 7: [8, 2, 4, 3, 7, 5, 1, 6]
Remove already present {4, 5, 6, 7}: [8, 2, 3, 1]

Child1: [1, 8, 2, 4, 5, 6, 7, 3]
```

**Why Order1?**:
- Maintains the permutation property (no duplicates)
- Preserves partial orderings from both parents
- Essential for TSP and similar problems

---

## Mutation Operators

Mutation introduces random changes to maintain genetic diversity.

### 1. Mutation Interface
**Location**: `src/main/java/org/example/lib/geneticalgorithm/mutation/Mutation.java`

```java
public interface Mutation {
    void mutate(Chromosome chromosome, double mutationRate);
}
```

**Purpose**: Defines the contract for all mutation methods.

**Parameters**:
- **chromosome**: The chromosome to mutate (modified in place)
- **mutationRate**: Probability of mutation (0.0 to 1.0)

**Note**: Mutation modifies the chromosome directly (no return value).

---

### 2. FlipMutation
**Location**: `src/main/java/org/example/lib/geneticalgorithm/mutation/impl/FlipMutation.java`

```java
public class FlipMutation implements Mutation {
    @Override
    public void mutate(Chromosome chromosome, double mutationRate) {
        BinaryChromosome b = (BinaryChromosome) chromosome;
        Random r = new Random();
        for (int i = 0; i < b.getGenes().size(); i++) {
            if (r.nextDouble() < mutationRate) {
                int old = b.getGenes().get(i);
                if(old == 1){
                    b.getGenes().set(i, 0);
                } else {
                    b.getGenes().set(i, 1);
                }
            }
        }
    }
}
```

**Concept**: Flips bits in a binary chromosome (0→1 or 1→0).

**How it works**:
- For each gene in the chromosome:
  - Generate a random number
  - If the number is less than mutationRate, flip the bit

**Example**:
```
Original: [1, 0, 1, 1, 0]
MutationRate: 0.2

Random checks:
Position 0: 0.65 > 0.2 → no flip
Position 1: 0.15 < 0.2 → flip! (0→1)
Position 2: 0.85 > 0.2 → no flip
Position 3: 0.08 < 0.2 → flip! (1→0)
Position 4: 0.50 > 0.2 → no flip

Result: [1, 1, 1, 0, 0]
```

**Use Case**: Binary chromosomes only.

---

### 3. SwapMutation
**Location**: `src/main/java/org/example/lib/geneticalgorithm/mutation/impl/SwapMutation.java`

```java
public class SwapMutation implements Mutation {
    @Override
    public void mutate(Chromosome chromosome, double mutationRate) {
        Random r = new Random();
        if (r.nextDouble() < mutationRate) {
            int i = r.nextInt(chromosome.getSize());
            int j = r.nextInt(chromosome.getSize());
            Collections.swap(chromosome.getGenes(), i, j);
        }
    }
}
```

**Concept**: Swaps two random genes in the chromosome.

**How it works**:
1. Generate a random number
2. If less than mutationRate:
   - Pick two random positions i and j
   - Swap the genes at these positions

**Example**:
```
Original: [3, 1, 4, 2, 5]

Random positions: i=1, j=3
Swap genes at positions 1 and 3

Result: [3, 2, 4, 1, 5]
```

**Use Case**: Permutation chromosomes (preserves the permutation property).

**Note**: Unlike FlipMutation, this checks mutationRate once for the entire chromosome, not per gene.

---

## Replacement Strategies

Replacement determines how to form the next generation from the current population and offspring.

### 1. Replacement Interface
**Location**: `src/main/java/org/example/lib/geneticalgorithm/replacement/Replacement.java`

```java
public interface Replacement {
    List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring);
}
```

**Purpose**: Defines the contract for all replacement strategies.

**Parameters**:
- **population**: Current generation
- **offSpring**: Newly created offspring

**Returns**: The next generation population

---

### 2. GenerationalReplacement
**Location**: `src/main/java/org/example/lib/geneticalgorithm/replacement/impl/GenerationalReplacement.java`

```java
public class GenerationalReplacement implements Replacement {
    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        if(offSpring.size() >= population.size()){
            return new ArrayList<>(offSpring.subList(0, population.size()));
        } else {
            List<Chromosome> nextGen = new ArrayList<>(offSpring);
            nextGen.addAll(population.subList(0, population.size() - offSpring.size()));
            return nextGen;
        }
    }
}
```

**Concept**: The offspring completely replace the old population.

**How it works**:
- **Case 1**: If offspring size ≥ population size
  - Take the first `population.size()` offspring
- **Case 2**: If offspring size < population size
  - Keep all offspring
  - Fill remaining slots with chromosomes from old population

**Example**:
```
Population size: 10
Offspring size: 10

→ All 10 offspring become the new population
```

**Characteristics**:
- High exploration (doesn't preserve old solutions)
- Can lose good solutions if offspring are worse
- Fast convergence

---

### 3. ElitismReplacement
**Location**: `src/main/java/org/example/lib/geneticalgorithm/replacement/impl/ElitismReplacement.java`

```java
public class ElitismReplacement implements Replacement {
    private final int elitisimCount;

    public ElitismReplacement(int elitismCount) {
        this.elitisimCount = elitismCount;
    }

    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        population.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        offSpring.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        List<Chromosome> nextGeneration = new ArrayList<>(population.subList(0, elitisimCount));

        int remaining = population.size() - elitisimCount;
        nextGeneration.addAll(offSpring.subList(0, Math.min(remaining, offSpring.size())));

        return nextGeneration;
    }
}
```

**Concept**: Preserve the best chromosomes from the old population.

**How it works**:
1. Sort both population and offspring by fitness (best first)
2. Take the top `elitisimCount` chromosomes from the old population
3. Fill remaining slots with best offspring

**Example**:
```
Population size: 10
Elitism count: 2

Old population (sorted): [C1(100), C2(95), C3(90), ..., C10(20)]
Offspring (sorted): [O1(85), O2(80), ..., O10(30)]

Next generation:
- Keep top 2 from old: [C1(100), C2(95)]
- Add top 8 from offspring: [O1(85), O2(80), ..., O8(45)]

Result: [C1(100), C2(95), O1(85), O2(80), O3(75), ...]
```

**Advantages**:
- Guarantees best solutions are never lost
- Provides exploitation (refining good solutions)
- Balances with exploration from offspring

---

### 4. SteadyStateReplacement
**Location**: `src/main/java/org/example/lib/geneticalgorithm/replacement/impl/SteadyStateReplacement.java`

```java
public class SteadyStateReplacement implements Replacement {
    int replaceCount;

    public SteadyStateReplacement(int replaceCount) {
        this.replaceCount = replaceCount;
    }

    @Override
    public List<Chromosome> replace(List<Chromosome> population, List<Chromosome> offSpring) {
        population.sort(Comparator.comparingDouble(Chromosome::getFitness));
        offSpring.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        for(int i = 0; i < replaceCount && i < offSpring.size(); i++){
            population.set(i, offSpring.get(i));
        }

        return new ArrayList<>(population);
    }
}
```

**Concept**: Replace only the worst chromosomes with the best offspring.

**How it works**:
1. Sort population by fitness (worst first)
2. Sort offspring by fitness (best first)
3. Replace the worst `replaceCount` chromosomes with the best `replaceCount` offspring

**Example**:
```
Population size: 10
Replace count: 3

Old population (sorted worst first): [C10(20), C9(25), C8(30), ..., C1(100)]
Offspring (sorted best first): [O1(85), O2(80), O3(75), ..., O10(30)]

Replace:
- C10(20) with O1(85)
- C9(25) with O2(80)
- C8(30) with O3(75)

Result: [O1(85), O2(80), O3(75), C7(35), ..., C1(100)]
```

**Characteristics**:
- Gradual replacement (low population turnover)
- Maintains diversity better than generational
- Slower convergence but more stable

---

## Configuration Enums

Enums provide type-safe configuration options.

### 1. CHROMOSOME_TYPE
**Location**: `src/main/java/org/example/lib/geneticalgorithm/enums/CHROMOSOME_TYPE.java`

```java
public enum CHROMOSOME_TYPE {
    PERMUTATION,
    INTEGER,
    FLOAT,
    BINARY
}
```

**Purpose**: Specifies which type of chromosome to use.

**Values**:
- **PERMUTATION**: For ordering problems (TSP, scheduling)
- **INTEGER**: For discrete value problems
- **FLOAT**: Not yet implemented
- **BINARY**: For binary decision problems

---

### 2. CROSSOVER_METHOD
**Location**: `src/main/java/org/example/lib/geneticalgorithm/enums/CROSSOVER_METHOD.java`

```java
public enum CROSSOVER_METHOD {
    K_POINT,
    SINGLE_POINT,
    ORDER1
}
```

**Purpose**: Specifies which crossover operator to use.

**Values**:
- **SINGLE_POINT**: Single-point crossover
- **K_POINT**: K-point crossover
- **ORDER1**: Order-1 crossover (for permutations)

---

### 3. MUTATION_METHOD
**Location**: `src/main/java/org/example/lib/geneticalgorithm/enums/MUTATION_METHOD.java`

```java
public enum MUTATION_METHOD {
    FLIP,
    SWAP
}
```

**Purpose**: Specifies which mutation operator to use.

**Values**:
- **FLIP**: Bit flip mutation (for binary chromosomes)
- **SWAP**: Swap mutation (for permutation chromosomes)

---

### 4. SELECTION_METHOD
**Location**: `src/main/java/org/example/lib/geneticalgorithm/enums/SELECTION_METHOD.java`

```java
public enum SELECTION_METHOD {
    TOURNAMENT,
    ROULETTE_WHEEL
}
```

**Purpose**: Specifies which selection operator to use.

**Values**:
- **TOURNAMENT**: Tournament selection
- **ROULETTE_WHEEL**: Fitness-proportionate selection

---

### 5. REPLACEMENT_METHOD
**Location**: `src/main/java/org/example/lib/geneticalgorithm/enums/REPLACEMENT_METHOD.java`

```java
public enum REPLACEMENT_METHOD {
    GENERATIONAL,
    STEADY_STATE,
    ELITISM
}
```

**Purpose**: Specifies which replacement strategy to use.

**Values**:
- **GENERATIONAL**: Complete replacement
- **STEADY_STATE**: Partial replacement of worst
- **ELITISM**: Preserve best chromosomes

---

## Usage Example

### Main.java - Traveling Salesman Problem
**Location**: `src/main/java/org/example/Main.java`

This file demonstrates how to use the library to solve a TSP problem.

```java
public class Main {
    static double tspFitness(Chromosome chromosome, double[][] dist) {
        List<Integer> order = (List<Integer>) chromosome.getGenes();
        double totalDistance = 0;
        int l = order.size();
        for (int i = 0; i < l - 1; i++) {
            totalDistance += dist[order.get(i)][order.get(i + 1)];
        }
        return 1.0 / totalDistance;
    }

    public static void main(String[] args) {
        // Distance matrix for 4 cities
        double[][] dist = {
            {0, 2, 9, 10},
            {1, 0, 6, 4},
            {15, 7, 0, 8},
            {6, 3, 12, 0}
        };

        // Fitness function: higher fitness for shorter tours
        Function<Chromosome, Double> fitness = (chromosome) -> tspFitness(chromosome, dist);

        // Feasibility check: no duplicate cities
        Predicate<Chromosome> isFeasible = chromosome -> {
            List<Integer> genes = (List<Integer>) chromosome.getGenes();
            return genes.size() == new HashSet<>(genes).size();
        };

        // Configure the genetic algorithm
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.PERMUTATION);
        geneticAlgorithm.setSelectionMethod(SELECTION_METHOD.TOURNAMENT);
        geneticAlgorithm.setCrossoverMethod(CROSSOVER_METHOD.ORDER1);
        geneticAlgorithm.setMutationMethod(MUTATION_METHOD.SWAP);
        geneticAlgorithm.setReplacementMethod(REPLACEMENT_METHOD.GENERATIONAL);

        geneticAlgorithm.setChromosomeSize(dist.length);
        geneticAlgorithm.setPopulationSize(50);
        geneticAlgorithm.setMaxGenerations(500);
        geneticAlgorithm.setCrossoverRate(0.8);
        geneticAlgorithm.setMutationRate(0.2);

        geneticAlgorithm.setFitnessFunction(fitness);
        geneticAlgorithm.setIsFeasibleChromosomePredicate(isFeasible);

        // Run the algorithm
        geneticAlgorithm.run();
        geneticAlgorithm.displayResults();
    }
}
```

### Step-by-Step Explanation

#### 1. Problem Setup
```java
double[][] dist = {
    {0, 2, 9, 10},
    {1, 0, 6, 4},
    {15, 7, 0, 8},
    {6, 3, 12, 0}
};
```
**Distance matrix** for 4 cities. `dist[i][j]` = distance from city i to city j.

#### 2. Fitness Function
```java
static double tspFitness(Chromosome chromosome, double[][] dist) {
    List<Integer> order = (List<Integer>) chromosome.getGenes();
    double totalDistance = 0;
    int l = order.size();
    for (int i = 0; i < l - 1; i++) {
        totalDistance += dist[order.get(i)][order.get(i + 1)];
    }
    return 1.0 / totalDistance;
}
```
**Explanation**:
- Extract the city order from the chromosome
- Calculate total distance by summing distances between consecutive cities
- Return **1 / totalDistance** (shorter tours have higher fitness)

**Example**:
```
Chromosome: [2, 0, 3, 1]
Tour: City 2 → City 0 → City 3 → City 1

Distance calculation:
- dist[2][0] = 15
- dist[0][3] = 10
- dist[3][1] = 3
Total = 28

Fitness = 1/28 = 0.0357
```

#### 3. Feasibility Check
```java
Predicate<Chromosome> isFeasible = chromosome -> {
    List<Integer> genes = (List<Integer>) chromosome.getGenes();
    return genes.size() == new HashSet<>(genes).size();
};
```
**Explanation**: Ensures no duplicate cities in the tour. A HashSet removes duplicates, so if sizes differ, there were duplicates.

#### 4. Configuration
```java
geneticAlgorithm.setChromosomeType(CHROMOSOME_TYPE.PERMUTATION);
```
Use permutation chromosomes (city order).

```java
geneticAlgorithm.setSelectionMethod(SELECTION_METHOD.TOURNAMENT);
```
Use tournament selection.

```java
geneticAlgorithm.setCrossoverMethod(CROSSOVER_METHOD.ORDER1);
```
Use Order-1 crossover (preserves permutation property).

```java
geneticAlgorithm.setMutationMethod(MUTATION_METHOD.SWAP);
```
Use swap mutation (preserves permutation property).

```java
geneticAlgorithm.setReplacementMethod(REPLACEMENT_METHOD.GENERATIONAL);
```
Replace entire population each generation.

```java
geneticAlgorithm.setChromosomeSize(dist.length);
```
Chromosome size = number of cities (4).

```java
geneticAlgorithm.setPopulationSize(50);
```
Maintain 50 chromosomes per generation.

```java
geneticAlgorithm.setMaxGenerations(500);
```
Run for up to 500 generations.

```java
geneticAlgorithm.setCrossoverRate(0.8);
```
80% chance of crossover (20% chance parents pass unchanged).

```java
geneticAlgorithm.setMutationRate(0.2);
```
20% chance of mutation.

#### 5. Run
```java
geneticAlgorithm.run();
geneticAlgorithm.displayResults();
```
Execute the algorithm and display results.

---

## Complete Code Walkthrough

Let's trace through one complete generation of the algorithm with a small example.

### Initial Setup
```
Population size: 4
Chromosome size: 4 (cities 0, 1, 2, 3)
Generation: 0
```

### Generation 0: Initialization

**Step 1: Initialize Population**
```
Population:
C1: [2, 1, 3, 0] - random permutation
C2: [1, 3, 0, 2] - random permutation
C3: [0, 2, 1, 3] - random permutation
C4: [3, 0, 2, 1] - random permutation
```

**Step 2: Calculate Fitness**
```
Using dist matrix:
    0   1   2   3
0 [ 0,  2,  9, 10]
1 [ 1,  0,  6,  4]
2 [15,  7,  0,  8]
3 [ 6,  3, 12,  0]

C1: [2, 1, 3, 0] → dist[2][1]=7 + dist[1][3]=4 + dist[3][0]=6 = 17 → fitness=1/17=0.059
C2: [1, 3, 0, 2] → dist[1][3]=4 + dist[3][0]=6 + dist[0][2]=9 = 19 → fitness=1/19=0.053
C3: [0, 2, 1, 3] → dist[0][2]=9 + dist[2][1]=7 + dist[1][3]=4 = 20 → fitness=1/20=0.050
C4: [3, 0, 2, 1] → dist[3][0]=6 + dist[0][2]=9 + dist[2][1]=7 = 22 → fitness=1/22=0.045
```

**Step 3: Update Best Chromosome**
```
Best: C1 with fitness 0.059
```

### Generation 1: Evolution

**Step 4: Selection (Tournament, size=2)**
```
Tournament 1: [C1, C3] → Winner: C1 (fitness=0.059)
Tournament 2: [C2, C4] → Winner: C2 (fitness=0.053)
Tournament 3: [C4, C1] → Winner: C1 (fitness=0.059)
Tournament 4: [C3, C2] → Winner: C2 (fitness=0.053)

Mating Pool: [C1, C2, C1, C2]
```

**Step 5: Crossover (Order1, rate=0.8)**

**Pair 1: C1 and C2**
```
C1: [2, 1, 3, 0]
C2: [1, 3, 0, 2]

Random: 0.65 < 0.8 → perform crossover
Points: start=1, end=2

Child1: Copy C1[1:2] → [_, 1, 3, _]
Fill from C2 starting at 3: [0, 2, 1, 3] (skip 1, 3 already present)
Result: [0, 1, 3, 2]

Child2: Copy C2[1:2] → [_, 3, 0, _]
Fill from C1 starting at 3: [2, 1, 3, 0] (skip 3, 0 already present)
Result: [2, 3, 0, 1]
```

**Pair 2: C1 and C2 (again)**
```
Random: 0.92 > 0.8 → no crossover, return parents
Child3: [2, 1, 3, 0]
Child4: [1, 3, 0, 2]
```

**Step 6: Mutation (Swap, rate=0.2)**
```
Child1: [0, 1, 3, 2]
Random: 0.15 < 0.2 → mutate
Swap positions 1 and 3 → [0, 2, 3, 1]

Child2: [2, 3, 0, 1]
Random: 0.85 > 0.2 → no mutation

Child3: [2, 1, 3, 0]
Random: 0.10 < 0.2 → mutate
Swap positions 0 and 2 → [3, 1, 2, 0]

Child4: [1, 3, 0, 2]
Random: 0.45 > 0.2 → no mutation

Mating Pool after mutation: [[0, 2, 3, 1], [2, 3, 0, 1], [3, 1, 2, 0], [1, 3, 0, 2]]
```

**Step 7: Replacement (Generational)**
```
Old population: [C1, C2, C3, C4]
Offspring: [[0, 2, 3, 1], [2, 3, 0, 1], [3, 1, 2, 0], [1, 3, 0, 2]]

New population = offspring (complete replacement)
```

**Step 8: Calculate Fitness (new population)**
```
C1': [0, 2, 3, 1] → fitness = 0.056
C2': [2, 3, 0, 1] → fitness = 0.050
C3': [3, 1, 2, 0] → fitness = 0.062
C4': [1, 3, 0, 2] → fitness = 0.053
```

**Step 9: Update Best Chromosome**
```
Best in generation: C3' with fitness 0.062
All-time best: Update to C3' (0.062 > 0.059)
```

### Continue for 500 generations...

Eventually, the algorithm converges to an optimal or near-optimal tour.

---

## Summary of Key Concepts

### 1. Genetic Algorithm Flow
```
Initialize → Evaluate → [Select → Crossover → Mutate → Replace → Evaluate] × N generations
```

### 2. Design Patterns
- **Strategy**: Pluggable operators (selection, crossover, mutation, replacement)
- **Factory**: Chromosome creation abstraction
- **Template Method**: Algorithm skeleton in `GeneticAlgorithm.run()`

### 3. Chromosome Types
- **Binary**: 0/1 values (Knapsack, feature selection)
- **Integer**: Integer ranges (scheduling, allocation)
- **Permutation**: Unique orderings (TSP, job sequencing)

### 4. Selection Methods
- **Roulette Wheel**: Fitness-proportionate (higher fitness = higher probability)
- **Tournament**: Best of random subset (configurable selection pressure)

### 5. Crossover Operators
- **Single Point**: Split at one point, swap tails
- **K-Point**: Split at k points, alternate segments
- **Order1**: Preserve relative order for permutations

### 6. Mutation Operators
- **Flip**: Bit flip for binary chromosomes
- **Swap**: Position swap for permutation chromosomes

### 7. Replacement Strategies
- **Generational**: Complete replacement with offspring
- **Elitism**: Keep best, fill with offspring
- **Steady State**: Replace worst with best offspring

---

## Project File Reference

### Core Files
- `Algorithm.java`: Base algorithm interface (src/main/java/org/example/lib/Algorithm.java:4)
- `GeneticAlgorithm.java`: Main orchestrator (src/main/java/org/example/lib/geneticalgorithm/GeneticAlgorithm.java:98-114)

### Chromosome System
- `Chromosome.java`: Abstract base (src/main/java/org/example/lib/geneticalgorithm/chromosome/Chromosome.java:13)
- `BinaryChromosome.java`: Binary implementation (src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/BinaryChromosome.java:9)
- `IntChromosome.java`: Integer implementation (src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/IntChromosome.java:9)
- `PermutationChromosome.java`: Permutation implementation (src/main/java/org/example/lib/geneticalgorithm/chromosome/impl/PermutationChromosome.java:9)

### Factories
- `ChromosomeFactory.java`: Factory interface (src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/ChromosomeFactory.java:5)
- `BinaryChromosomeFactory.java`: Binary factory (src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/BinaryChromosomeFactory.java:7)
- `IntChromosomeFactory.java`: Integer factory (src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/IntChromosomeFactory.java:7)
- `PermutationChromosomeFactory.java`: Permutation factory (src/main/java/org/example/lib/geneticalgorithm/chromosome/factory/impl/PermutationChromosomeFactory.java:7)

### Selection Operators
- `Selection.java`: Selection interface (src/main/java/org/example/lib/geneticalgorithm/selection/Selection.java:7)
- `RouletteWheelSelection.java`: Fitness-proportionate (src/main/java/org/example/lib/geneticalgorithm/selection/impl/RouletteWheelSelection.java:10)
- `TournamentSelection.java`: Tournament-based (src/main/java/org/example/lib/geneticalgorithm/selection/impl/TournamentSelection.java:11)

### Crossover Operators
- `Crossover.java`: Crossover interface (src/main/java/org/example/lib/geneticalgorithm/crossover/Crossover.java:9)
- `SinglePointCrossover.java`: Single-point (src/main/java/org/example/lib/geneticalgorithm/crossover/impl/SinglePointCrossover.java:10)
- `KPointCrossover.java`: K-point (src/main/java/org/example/lib/geneticalgorithm/crossover/impl/KPointCrossover.java:14)
- `Order1Crossover.java`: Order-1 (src/main/java/org/example/lib/geneticalgorithm/crossover/impl/Order1Crossover.java:11)

### Mutation Operators
- `Mutation.java`: Mutation interface (src/main/java/org/example/lib/geneticalgorithm/mutation/Mutation.java:5)
- `FlipMutation.java`: Bit flip (src/main/java/org/example/lib/geneticalgorithm/mutation/impl/FlipMutation.java:9)
- `SwapMutation.java`: Position swap (src/main/java/org/example/lib/geneticalgorithm/mutation/impl/SwapMutation.java:10)

### Replacement Strategies
- `Replacement.java`: Replacement interface (src/main/java/org/example/lib/geneticalgorithm/replacement/Replacement.java:7)
- `GenerationalReplacement.java`: Complete replacement (src/main/java/org/example/lib/geneticalgorithm/replacement/impl/GenerationalReplacement.java:9)
- `ElitismReplacement.java`: Elitist replacement (src/main/java/org/example/lib/geneticalgorithm/replacement/impl/ElitismReplacement.java:10)
- `SteadyStateReplacement.java`: Steady-state (src/main/java/org/example/lib/geneticalgorithm/replacement/impl/SteadyStateReplacement.java:10)

### Configuration
- `CHROMOSOME_TYPE.java`: Chromosome types (src/main/java/org/example/lib/geneticalgorithm/enums/CHROMOSOME_TYPE.java:3)
- `CROSSOVER_METHOD.java`: Crossover methods (src/main/java/org/example/lib/geneticalgorithm/enums/CROSSOVER_METHOD.java:3)
- `MUTATION_METHOD.java`: Mutation methods (src/main/java/org/example/lib/geneticalgorithm/enums/MUTATION_METHOD.java:3)
- `SELECTION_METHOD.java`: Selection methods (src/main/java/org/example/lib/geneticalgorithm/enums/SELECTION_METHOD.java:3)
- `REPLACEMENT_METHOD.java`: Replacement methods (src/main/java/org/example/lib/geneticalgorithm/enums/REPLACEMENT_METHOD.java:3)

### Example
- `Main.java`: TSP example (src/main/java/org/example/Main.java:15)

---

## Conclusion

This Soft Computing Library provides a complete, flexible framework for implementing genetic algorithms. The modular design makes it easy to:

1. **Solve different problems** by changing the chromosome type and fitness function
2. **Experiment with algorithms** by swapping selection, crossover, mutation, and replacement strategies
3. **Extend functionality** by implementing new operators

The codebase demonstrates clean software engineering principles with clear separation of concerns, interface-based design, and design patterns that promote maintainability and extensibility.

Every team member should now understand:
- The overall architecture and how components interact
- The purpose and implementation of each file
- How to configure and use the genetic algorithm
- The theoretical concepts behind each operator
- How to extend the library for new use cases
