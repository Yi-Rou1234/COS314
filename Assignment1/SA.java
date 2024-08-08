import java.util.*;

public class SA {
    private static final int NUM_ITERATIONS = 1000;
    private static final int NUM_CAMPUSES = 5;
    private static final double START_TEMP = 1000.0;
    private static final double END_TEMP = 0.01;
    private static final double COOLING_RATE = 0.99;
    private static final int[][] DISTANCES = {
        {0, 15, 20, 22, 30},
        {15, 0, 10, 12, 25},
        {20, 10, 0, 8, 22},
        {22, 12, 8, 0, 18},
        {30, 25, 22, 18, 0}
    };

    public void run() {
        long startTime = System.currentTimeMillis();

        int[] currentSolution = generateInitialSolution();
        int currentDistance = calculateTotalDistance(currentSolution);

        int[] bestSolution = currentSolution.clone();
        int bestDistance = currentDistance;

        double temp = START_TEMP;

        while (temp > END_TEMP) {
            int[] newSolution = perturbSolution(currentSolution);
            int newDistance = calculateTotalDistance(newSolution);

            if (acceptanceProbability(currentDistance, newDistance, temp) > Math.random()) {
                currentSolution = newSolution.clone();
                currentDistance = newDistance;
            }

            if (currentDistance < bestDistance) {
                bestSolution = currentSolution.clone();
                bestDistance = currentDistance;
            }

            temp *= COOLING_RATE;
        }

        long endTime = System.currentTimeMillis();
        double runtime = (endTime - startTime) / 1000.0;

        System.out.println("| Problem Set | Best solutions(route) | Objective Function Val | Runtime | Av Obj Func |");
        System.out.println("| SA         | " + Arrays.toString(bestSolution) + " | " + bestDistance + " | " + runtime + " seconds |  |");
    }

    private static int[] generateInitialSolution() {
        int[] solution = new int[NUM_CAMPUSES];
        for (int i = 0; i < NUM_CAMPUSES; i++) {
            solution[i] = i;
        }

        Random rand = new Random();
        for (int i = 0; i < solution.length; i++) {
            int j = rand.nextInt(solution.length);
            int temp = solution[i];
            solution[i] = solution[j];
            solution[j] = temp;
        }

        return solution;
    }

    private static int calculateTotalDistance(int[] solution) {
        int totalDistance = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            totalDistance += DISTANCES[solution[i]][solution[i + 1]];
        }
        totalDistance += DISTANCES[solution[solution.length - 1]][solution[0]]; // Return to starting point
        return totalDistance;
    }

    private static int[] perturbSolution(int[] solution) {
        int[] perturbedSolution = solution.clone();
        Random rand = new Random();
        int index1 = rand.nextInt(perturbedSolution.length);
        int index2 = rand.nextInt(perturbedSolution.length);
        int temp = perturbedSolution[index1];
        perturbedSolution[index1] = perturbedSolution[index2];
        perturbedSolution[index2] = temp;
        return perturbedSolution;
    }

    private static double acceptanceProbability(int currentDistance, int newDistance, double temp) {
        if (newDistance < currentDistance) {
            return 1.0;
        }
        return Math.exp((currentDistance - newDistance) / temp);
    }
}
