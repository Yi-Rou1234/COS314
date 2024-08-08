import java.util.*;

public class ILS {
    private static final int NUM_ITERATIONS = 1000;
    private static final int NUM_CAMPUSES = 5;
    private static final int[][] DISTANCES = {
        {0, 15, 20, 22, 30},
        {15, 0, 10, 12, 25},
        {20, 10, 0, 8, 22},
        {22, 12, 8, 0, 18},
        {30, 25, 22, 18, 0}
    };

    public void run() {
        long startTime = System.currentTimeMillis();

        int[] bestSolution = new int[NUM_CAMPUSES];
        int bestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            int[] solution = generateInitialSolution();
            int distance = calculateTotalDistance(solution);

            if (distance < bestDistance) {
                bestSolution = solution;
                bestDistance = distance;
            }

            solution = perturbSolution(solution);
        }

        long endTime = System.currentTimeMillis();
        double runtime = (endTime - startTime) / 1000.0;

        System.out.println("| Problem Set | Best solutions(route) | Objective Function Val | Runtime | Av Obj Func |");
        System.out.println("| ILS         | " + Arrays.toString(bestSolution) + " | " + bestDistance + " | " + runtime + " seconds |  |");
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
}
