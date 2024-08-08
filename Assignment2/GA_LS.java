import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; // Import ArrayList
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GA_LS {
    private int[] weights;
    private int[] values;
    private int[][] population;
    private int capacity;
    private Random random;

    public GA_LS(int[] weights, int[] values, int capacity, long seed) {
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        this.random = new Random(seed);
    }

    // Genetic Algorithm
    public void runGA(int popSize, double crossoverRate, double mutationRate, int maxGenerations, int knownOptimum) {
        long startTime = System.currentTimeMillis();
        population = new int[popSize][weights.length];
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < weights.length; j++) {
                population[i][j] = random.nextBoolean() ? 1 : 0;
            }
        }
    
        System.out.println("Genetic algorithm is running...");
        for (int generation = 0; generation < maxGenerations; generation++) {
            int[][] newPopulation = new int[popSize][weights.length];
            for (int i = 0; i < popSize; i++) {
                int[] parent1 = population[random.nextInt(popSize)];
                int[] parent2 = population[random.nextInt(popSize)];
    
                if (random.nextDouble() < crossoverRate) {
                    int crossoverPoint = random.nextInt(weights.length);
                    for (int j = crossoverPoint; j < weights.length; j++) {
                        int temp = parent1[j];
                        parent1[j] = parent2[j];
                        parent2[j] = temp;
                    }
                }
    
                if (random.nextDouble() < mutationRate) {
                    int mutationPoint = random.nextInt(weights.length);
                    parent1[mutationPoint] = 1 - parent1[mutationPoint];
                    parent2[mutationPoint] = 1 - parent2[mutationPoint];
                }
                newPopulation[i] = fitness(parent1) > fitness(parent2) ? parent1 : parent2;
            }
            population = newPopulation;
        }
        
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        int[] bestIndividual = findBestIndividual();
        int knownOptimums = fitness(bestIndividual);
        System.out.println("Best Solution: " + Arrays.toString(findBestIndividual()));
        System.out.println("Known Optimum: " + knownOptimums);
        System.out.println("Runtime (seconds): " + runtime / 1000.0);
    }

    public void runLocalSearch(int maxIterations, int knownOptimum) {
        long startTime = System.currentTimeMillis();
        System.out.println("Local Search is running...");

        int[] currentSolution = new int[weights.length];
        for (int i = 0; i < weights.length; i++) {
            currentSolution[i] = random.nextBoolean() ? 1 : 0;
        }
        int currentFitness = fitness(currentSolution);
    
        int[] bestSolution = currentSolution.clone();
        int bestFitness = currentFitness;
    
        for (int i = 0; i < maxIterations; i++) {
            int[] newSolution = currentSolution.clone();
            int mutationPoint = random.nextInt(weights.length);
            newSolution[mutationPoint] = 1 - newSolution[mutationPoint];
            int newFitness = fitness(newSolution);
    
            if (newFitness > currentFitness) {
                currentSolution = newSolution;
                currentFitness = newFitness;
            }
    
            if (currentFitness > bestFitness) {
                bestSolution = currentSolution.clone();
                bestFitness = currentFitness;
            }
        }
    
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
    
        System.out.println("Best Solution: " + Arrays.toString(bestSolution));
        System.out.println("Known Optimum: " + bestFitness);
        System.out.println("Runtime (seconds): " + runtime / 1000.0);
    }

    private int fitness(int[] solution) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        if (totalWeight > capacity) {
            return 0; 
        } else {
            return totalValue;
        }
    }

    private int[] findBestIndividual() {
        int[] best = null;
        int bestFitness = Integer.MIN_VALUE;
        for (int[] individual : population) {
            int fitness = fitness(individual);
            if (fitness > bestFitness) {
                best = individual.clone();
                bestFitness = fitness;
            }
        }
        return best;    
    }

    // Main method
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java GA_SA <input_file>");
            return;
        }

        String inputFile = args[0];
        int capacity = 0;
        ArrayList<Integer> weightList = new ArrayList<Integer>(); // Specify type argument
        ArrayList<Integer> valueList = new ArrayList<Integer>(); // Specify type argument
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String[] firstLine = br.readLine().split("\\s+");
            capacity = Integer.parseInt(firstLine[0]);
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                weightList.add(Integer.parseInt(parts[0]));
                valueList.add(Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Convert ArrayLists to arrays
        int[] weights = weightList.stream().mapToInt(Integer::intValue).toArray();
        int[] values = valueList.stream().mapToInt(Integer::intValue).toArray();

        // Ask the user for the seed value
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the seed value: ");
        long seed = scanner.nextLong();

        GA_LS ga_sa = new GA_LS(weights, values, capacity, seed);

        ga_sa.runGA(100, 0.8, 0.01, 1000, 1024);
        ga_sa.runLocalSearch(1000, 1024);
    }
}
