package br.usp.lti.cdds.core;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vinicius
 */
public abstract class HeuristicBase {

    protected Problem problem;
    protected Solution solution;

    public HeuristicBase(Problem problem, ArrayList<Job> toOrder) {
        this.problem = problem;
        this.solution = new Solution(toOrder);
        this.problem.evaluate(solution);
    }

    protected ArrayList<ArrayList<Job>> split(ArrayList<Job> jobs, int d, int begin) {
        ArrayList<Job> before = new ArrayList<>();
        ArrayList<Job> after = new ArrayList<>();
        int processingTimeSum = 0;
        int gap = d - processingTimeSum - begin;
        int i = 0;
        Job j;
        boolean perform = false;
        do {
            j = jobs.get(i);
            int calc = d - (processingTimeSum + j.getProcessingTime()) - begin;
            if (calc >= 0) {
                before.add(j);
                processingTimeSum += j.getProcessingTime();
                gap = d - processingTimeSum - begin;
                perform = true;
                i++;
            } else {
                perform = false;

            }

        } while (i < jobs.size() && perform);
        while (i < jobs.size()) {
            j = jobs.get(i++);
            after.add(j);
        }
        ArrayList<ArrayList<Job>> toReturn = new ArrayList<>();
        toReturn.add(before);
        toReturn.add(after);
        return toReturn;
    }

    public void printStatus(Solution s) {
        ArrayList<Job> jobs = s.getSequenceOfJobs();
        double SUM_P = problem.getSum_P(s);
        int d = (int) Math.round(SUM_P * problem.getH());
        double minSum = s.getFitness(); //a fazer: mtodo que calcula a penalidade dos jobs
        ArrayList<ArrayList<Job>> splitted = this.split(jobs, d, s.getBeginAt());
        ArrayList<Job> before = splitted.get(0);
        ArrayList<Job> after = splitted.get(1);
        int beforeSum = problem.getSum_P(before);
        int afterSum = problem.getSum_P(after);
        System.err.println("Sum " + SUM_P + " D=" + d + " Fitness=" + minSum + " qtdBefore=" + before.size() + " sum= " + beforeSum + " qtdAfter=" + after.size() + " sum=" + afterSum + " begin=" + s.getBeginAt() + " space before middle " + (d - beforeSum - s.getBeginAt()));
        System.err.println(s.getOrderAsString());

    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public abstract void method(int d);

}
