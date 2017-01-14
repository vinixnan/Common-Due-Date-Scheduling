package br.usp.lti.cdds;

import br.usp.lti.cdds.localsearch.LocalSearch;
import br.usp.lti.cdds.heuristics.ConstructionHeuristic;
import br.usp.lti.cdds.core.OneSolutionImprovementHeuristic;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.ProblemReader;
import br.usp.lti.cdds.core.Solution;

public class Main {

    public static void main(String[] args) {

        int size = 20;
        int type = 1;//1 construction 2 local search
        double[] hs = {0.2, 0.4, 0.6, 0.8};
        //double[] hs={0.8};

        if (args.length == 2) {
            size = Integer.parseInt(args[0]);
            type = Integer.parseInt(args[1]);
        }
        int[][] table = new int[hs.length][10];
        int[] sums = new int[10];
        ProblemReader pr=new ProblemReader(size);
        
        for (int i = 0; i < hs.length; i++) {
            
            OneSolutionImprovementHeuristic sdh;
            pr.readDataFromFile();
            int j = 0;
            while (pr.readNextProblem()) {
                double h = hs[i];
                sums[j] = Problem.getSum_P(pr.getCurrentProblem());
                int d = (int) Math.floor(sums[j] * h);
                Problem problem=new Problem(d, h, pr.getCurrentProblem());
                if (type == 1) {
                    sdh = new ConstructionHeuristic(problem, pr.getCurrentProblem());
                } else {
                    sdh = new LocalSearch(problem, pr.getCurrentProblem());
                }
                //sdh.printStatus(sdh.getBaseJobs());
                sdh.method(d);
                Solution s=sdh.getSolution();
                //sdh.printStatus(sdh);
                table[i][j] = (int) s.getFitness();
                j++;
            }
        }

        //System.out.print("k;SUM;");
        //for (double h : hs) {
        //    System.out.print("h=" + h + ";");
        //}
        //System.out.println("");
        for (int i = 0; i < table[0].length; i++) {
            //System.out.print((i + 1) + ";" + sums[i] + ";");
            for (int j = 0; j < table.length; j++) {
                System.out.print(table[j][i] + ";");
            }
            System.out.println("");
        }

    }

}
