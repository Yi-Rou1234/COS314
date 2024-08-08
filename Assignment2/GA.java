import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GA {
    private int popSize;
    private double crossoverRate;
    private double mutationRate;
    private int maxGenerations;
    private float[] weights;
    private float[] values;
    private int capacity;
    private int[][] population;
    private Random random;

    public GA(int popSize, double crossoverRate, double mutationRate, int maxGenerations, float[] weights, float[] values, int capacity, long seed) {
        this.popSize = popSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.maxGenerations = maxGenerations;
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        this.random = new Random(seed);
        this.population = new int[popSize][weights.length];
    }

    public void initializePopulation() {
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < weights.length; j++) {
                population[i][j] = random.nextInt(2);
            }
        }
    }

    public int[] selection() {
        int tournamentSize = 5;
        int[] best = null;
        int bestFitness = Integer.MIN_VALUE;

        for (int i = 0; i < tournamentSize; i++) {
            int randomId = random.nextInt(popSize);
            int[] competitor = population[randomId];
            int competitorFitness = fitness(competitor);

            if (best == null || competitorFitness > bestFitness) {
                best = competitor;
                bestFitness = competitorFitness;
            }
        }

        return best;
    }

    public void crossover(int[] parent1, int[] parent2) {
        int crossoverPoint = random.nextInt(weights.length);

        for (int i = crossoverPoint; i < weights.length; i++) {
            int temp = parent1[i];
            parent1[i] = parent2[i];
            parent2[i] = temp;
        }
    }

    public void mutation(int[] individual) {
        for (int i = 0; i < weights.length; i++) {
            if (random.nextDouble() < mutationRate) {
                individual[i] = 1 - individual[i];
            }
        }
    }

    public int fitness(int[] individual) {
        int totalWeight = 0;
        int totalValue = 0;

        for (int i = 0; i < weights.length; i++) {
            if (individual[i] == 1) {
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

    public int runGA() {
        long startTime = System.currentTimeMillis();
        initializePopulation();
        int[] bestIndividual = findBestIndividual();
        System.out.println("Selected Items:");
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < bestIndividual.length; i++) {
            if (bestIndividual[i] == 1) {
                System.out.println("Item " + i + ": Weight = " + weights[i] + ", Value = " + values[i]);
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        System.out.println("Total Weight of Selected Items: " + totalWeight);
        System.out.println("Total Value of Selected Items: " + totalValue);
        System.out.println("Best Solution: " + Arrays.toString(bestIndividual));
        System.out.println("Runtime (seconds): " + runtime / 1000.0);
        return totalValue;
    }

    public int[] findBestIndividual() {
        int[] best = null;
        int bestFitness = Integer.MIN_VALUE;

        for (int[] individual : population) {
            int individualFitness = fitness(individual);
            if (best == null || individualFitness > bestFitness) {
                best = individual;
                bestFitness = individualFitness;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        String inputFile = args[0];
        int capacity = 0;
        float[] weights = new float[100]; // Assuming a maximum of 100 items
        float[] values = new float[100];  // Assuming a maximum of 100 items
        int itemCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            // Read capacity from the first line
            line = br.readLine();
            if (line != null) {
                String[] firstLine = line.split("\\s+");
                capacity = Integer.parseInt(firstLine[0]);
            } else {
                System.err.println("Error: file is empty");
                return;
            }
            // Read weights and values from the following lines
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                weights[itemCount] = Float.parseFloat(parts[0]);
                values[itemCount] = Float.parseFloat(parts[1]);
                itemCount++;
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the seed value: ");
        long seed = scanner.nextLong();

        int popSize = 100;
        double crossoverRate = 0.8;
        double mutationRate = 0.01;
        int maxGenerations = 1000;

        GA ga = new GA(popSize, crossoverRate, mutationRate, maxGenerations, Arrays.copyOfRange(weights, 0, itemCount), Arrays.copyOfRange(values, 0, itemCount), capacity, seed);

        int bestFitness = ga.runGA();

        System.out.println("Best Value (Known Optimum): " + bestFitness);
    }
}
