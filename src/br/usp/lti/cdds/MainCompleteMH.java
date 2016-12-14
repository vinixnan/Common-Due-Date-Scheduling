package br.usp.lti.cdds;

import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.ProblemReader;
import br.usp.lti.cdds.core.Solution;
import br.usp.lti.cdds.metaheuristics.GeneticAlgorithm;
import br.usp.lti.cdds.util.FileUtils;
import java.util.Arrays;

public class MainCompleteMH {

    public static void main(String[] args) {

        int[] sizes = {10, 20, 50, 100, 200, 500, 1000};
        //int[] sizes = {10};
        double[] hs = {0.2, 0.4, 0.6, 0.8};
        int populationSize;//10+raiz de N floor
        int maxGenerations = 1000;//10000
        double crossProbability = 1.0;//0.4
        double mutationProbability = 0.5;//0.8
        double parcel=0.9;
        String crossType="PMXCrossover";
        String mutaType="SwapMutation";
        //BitFlipMutationFoward
        //crossType="TwoPointCrossover";
        mutaType="SwapMutation";
        //crossType="OnePointCrossoverInternalBefore";
        if (args.length == 6) {
            maxGenerations = Integer.parseInt(args[0]);
            crossProbability = Double.parseDouble(args[1]);
            mutationProbability = Double.parseDouble(args[2]);
            crossType=args[3];
            mutaType=args[4];
            parcel = Double.parseDouble(args[5]);
        }

        int[][][] table = new int[sizes.length][hs.length][10];
        long[][] times = new long[sizes.length][hs.length];
        int[][] sums = new int[sizes.length][10];
        int[][][] bench = new int[sizes.length][10][hs.length];
        for (int n = 0; n < sizes.length; n++) {
            int size = sizes[n];
            populationSize = (int) (2*(10.0 + Math.floor(Math.sqrt(size))));
            //populationSize=100;
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
                    //System.out.println("h="+h+" k="+(k+1));
                    sums[n][k] = Problem.getSum_P(pr.getCurrentProblem());
                    int d = (int) Math.round(sums[n][k] * h);
                    Problem problem = new Problem(d, h, pr.getCurrentProblem());
                    GeneticAlgorithm sdh = new GeneticAlgorithm(problem, populationSize, maxGenerations, crossProbability, mutationProbability, crossType, mutaType);
                    Solution s = sdh.execute(pr.getCurrentProblem());
                    //System.out.println(s.getOrderAsString());
                    //System.out.println(s.getBeginAt());
                    //System.out.println(s.getFitness());
                    //System.out.println(s.getAsString());
                    //System.out.println("");
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
