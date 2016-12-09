package br.usp.lti.cdds;

import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.ProblemReader;
import br.usp.lti.cdds.core.Solution;
import br.usp.lti.cdds.metaheuristics.GeneticAlgorithm;
import br.usp.lti.cdds.util.FileUtils;
import java.util.Arrays;

public class MainCompleteMH {

    public static void main(String[] args) {

        //int[] sizes = {10, 20, 50, 100, 200, 500, 1000};
        int[] sizes = {10, 20};
        double[] hs = {0.2, 0.4, 0.6, 0.8};
        int populationSize;//10+raiz de N floor
        int maxGenerations = 500;//10000
        double crossProbability = 1;//0.4
        double mutationProbability = 0.5;//0.8
        double parcel=1;
        String crossType="PMXCrossover";
        String mutaType="SwapMutation";
        //BitFlipMutationFoward
        //crossType="TwoPointCrossover";
        //mutaType="SwapMutationInternalBefore";
        //crossType="OnePointCrossoverInternalBefore";
        if (args.length == 5) {
            maxGenerations = Integer.parseInt(args[0]);
            crossProbability = Double.parseDouble(args[1]);
            mutationProbability = Double.parseDouble(args[2]);
            crossType=args[3];
            mutaType=args[4];
        }

        int[][][] table = new int[sizes.length][hs.length][10];
        long[][] times = new long[sizes.length][hs.length];
        int[][] sums = new int[sizes.length][10];
        int[][][] bench = new int[sizes.length][10][hs.length];
        for (int n = 0; n < sizes.length; n++) {
            int size = sizes[n];
            populationSize = (int) (10.0 + Math.floor(Math.sqrt(size)));
            populationSize=100;
            System.out.println(populationSize);
            String benchmark = "bench/bench" + size + ".csv";
            //benchmark = "csv/saida_" + size + "_const.csv";
            System.out.println(benchmark);
            ProblemReader pr = new ProblemReader(size);
            bench[n] = FileUtils.readBenchMark(benchmark);//int[k][h.size]
            for (int i = 0; i < hs.length; i++) {
                double h = hs[i];
                pr.readDataFromFile();
                long begin = System.currentTimeMillis();
                int k = 0;
                while (pr.readNextProblem()) {
                    sums[n][k] = Problem.getSum_P(pr.getCurrentProblem());
                    int d = (int) Math.round(sums[n][k] * h);
                    Problem problem = new Problem(d, h, pr.getCurrentProblem());
                    GeneticAlgorithm sdh = new GeneticAlgorithm(problem, populationSize, maxGenerations, crossProbability, mutationProbability, crossType, mutaType, parcel);
                    Solution s = sdh.execute(pr.getCurrentProblem());
                    //System.out.println(s.getOrderAsString());
                    table[n][i][k] = (int) s.getFitness();
                    k++;
                }
                long finish = System.currentTimeMillis();
                times[n][i] = finish - begin;
            }
        }

        //calc difference between found and benchmark
        double[][][] difference = new double[sizes.length][hs.length][10];
        for (int n = 0; n < sizes.length; n++) {
            for (int h = 0; h < hs.length; h++) {
                for (int k = 0; k < 10; k++) {
                    difference[n][h][k] = ((((double) table[n][h][k]) - ((double) bench[n][k][h])) / ((double) bench[n][k][h])) * 100.0;
                }
            }
        }

        double overallDifference = 0.0;
        for (int n = 0; n < sizes.length; n++) {
            for (int h = 0; h < hs.length; h++) {
                for (int k = 0; k < 10; k++) {
                    overallDifference += difference[n][h][k];
                }
            }
        }
        overallDifference = overallDifference / (sizes.length * hs.length * 10);

        double[][] differencesAverages = new double[sizes.length][hs.length];
        for (int n = 0; n < sizes.length; n++) {
            for (int h = 0; h < hs.length; h++) {
                double sum = 0.0;
                for (int k = 0; k < 10; k++) {
                    sum += difference[n][h][k];
                }
                sum = sum / 10.0;
                differencesAverages[n][h] = sum;
            }
        }

        double[][] fitnessAverages = new double[sizes.length][hs.length];
        for (int n = 0; n < sizes.length; n++) {
            for (int h = 0; h < hs.length; h++) {
                double sum = 0.0;
                for (int k = 0; k < 10; k++) {
                    sum += table[n][h][k];
                }
                sum = sum / 10.0;
                fitnessAverages[n][h] = sum;
            }
        }

        System.out.println("\nFitness Average Tables");
        System.out.println("Size\\H;" + Arrays.toString(hs).replace(",", ";").replace("[", "").replace("]", "") + ";Total;");
        for (int n = 0; n < sizes.length; n++) {
            int size = sizes[n];
            System.out.print(size + ";");
            double meanH = 0.0;
            for (int h = 0; h < hs.length; h++) {
                System.out.print(fitnessAverages[n][h] + ";");
                meanH += fitnessAverages[n][h];
            }
            meanH = meanH / hs.length;
            System.out.println(meanH + ";");
        }
        System.out.print("Total;");
        for (int h = 0; h < hs.length; h++) {
            double meanH = 0.0;
            for (int n = 0; n < sizes.length; n++) {
                meanH += fitnessAverages[n][h];
            }
            meanH = meanH / sizes.length;
            System.out.print(meanH + ";");
        }

        System.out.println("\n\n\nDifferences Average Tables");
        System.out.println("Size\\H;" + Arrays.toString(hs).replace(",", ";").replace("[", "").replace("]", "") + ";Total;");
        for (int n = 0; n < sizes.length; n++) {
            int size = sizes[n];
            System.out.print(size + ";");
            double meanH = 0.0;
            for (int h = 0; h < hs.length; h++) {
                System.out.print(differencesAverages[n][h] + ";");
                meanH += differencesAverages[n][h];
            }
            meanH = meanH / hs.length;
            System.out.println(meanH + ";");
        }

        System.out.print("Total;");
        double overrall2 = 0.0;
        for (int h = 0; h < hs.length; h++) {
            double meanH = 0.0;
            for (int n = 0; n < sizes.length; n++) {
                meanH += differencesAverages[n][h];
            }
            meanH = meanH / sizes.length;
            overrall2 += meanH;
            System.out.print(meanH + ";");
        }
        overrall2 = overrall2 / hs.length;

        System.out.println("\n\nOverall Average=" + overallDifference);
        System.out.println("Overall Average=" + overrall2);
        System.out.println("N;Total");
        long sumAll = 0;
        for (int n = 0; n < sizes.length; n++) {
            int size = sizes[n];
            long sumTimeH = 0;
            for (int h = 0; h < hs.length; h++) {
                sumTimeH += times[n][h];
            }
            sumAll += sumTimeH;
            System.out.println(size + ";" + sumTimeH + "ms;");
        }
        System.out.println("Total time " + sumAll + "ms");
    }

}
