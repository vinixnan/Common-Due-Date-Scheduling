package br.usp.lti.cdds.core;

import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

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

    public HeuristicBase(Problem problem) {
        this.problem = problem;

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
        int d = (int) Math.floor(SUM_P * problem.getH());
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

    protected ArrayList<ArrayList<Job>> getOrderedTwoSet(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<Job> ordered = new ArrayList<>();
        ArrayList<Job> auxSet = new ArrayList<>(toOrder);
        Collections.sort(auxSet, new BetaAlphaComparator());
        int processingTimeSum = 0;
        int gap = d - processingTimeSum - begin;
        while (gap > 0 && auxSet.size() > 0) {
            Job j = auxSet.remove(0);
            ordered.add(j);
            processingTimeSum = problem.getSum_P(ordered);
            gap = d - (processingTimeSum + begin);
        }
        while (gap < 0) {
            Job j = ordered.remove(ordered.size() - 1);
            auxSet.add(j);
            processingTimeSum = problem.getSum_P(ordered);
            gap = d - (processingTimeSum + begin);
        }
        Collections.sort(ordered, new ProcessingTimeAlphaComparator());
        Collections.sort(auxSet, new ProcessingTimeBetaComparator());
        ArrayList<ArrayList<Job>> toReturn = new ArrayList<>();
        toReturn.add(ordered);
        toReturn.add(auxSet);
        return toReturn;
    }
    
    public Object[] vshapedSortIteractive(ArrayList<Job> sequence, int d) {
        int begin = this.findBetterBegin(d, sequence, 0);
        ArrayList<ArrayList<Job>> returned=this.getOrderedTwoSet(sequence, d, begin);
        return vshapedSortIterative(returned.get(0), returned.get(1), d);
    }

    public Object[] vshapedSortIterative(ArrayList<Job> beforeD, ArrayList<Job> afterD, int d) {

        ArrayList<Job> all = new ArrayList<>();
        Collections.sort(beforeD, new ProcessingTimeAlphaComparator());
        Collections.sort(afterD, new ProcessingTimeBetaComparator());
        all.addAll(beforeD);
        all.addAll(afterD);
        //int currentBegin =0;
        int currentBegin = this.findBetterBegin(d, all, 0);
        int nowBegin = currentBegin;
        ArrayList<Job> currentOrder = all;
        ArrayList<Job> nowOrder = all;
        double currentFitness = problem.getPenalty(d, currentBegin, all);
        double nowFitness = currentFitness;

        do {
            if (nowFitness < currentFitness) {
                currentFitness = nowFitness;
                currentOrder = nowOrder;
                currentBegin = nowBegin;
            }
            nowOrder = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);

            int processingTimeSum = 0;
            int gap = d - processingTimeSum - currentBegin;
            while (gap > 0 && myAfter.size() > 0) {
                Job j = myAfter.remove(0);
                nowOrder.add(j);
                processingTimeSum = problem.getSum_P(nowOrder);
                gap = d - processingTimeSum - currentBegin;
            }
            while (gap < 0) {
                Job j = nowOrder.remove(nowOrder.size() - 1);
                myAfter.add(j);
                processingTimeSum = problem.getSum_P(nowOrder);
                gap = d - (processingTimeSum + currentBegin);
            }
            Collections.sort(nowOrder, new ProcessingTimeAlphaComparator());
            Collections.sort(myAfter, new ProcessingTimeBetaComparator());
            nowOrder.addAll(myAfter);
            nowBegin = this.findBetterBegin(d, nowOrder, currentBegin);
            nowFitness = problem.getPenalty(d, nowBegin, nowOrder);

        } while (nowFitness < currentFitness);
        //currentBegin=this.hardfindBetterBegin(currentOrder, d, currentBegin);
        Object[] toRet = new Object[2];
        toRet[0] = currentOrder;
        toRet[1] = currentBegin;
        return toRet;
    }
    
    public  void fixBeginOfSolution(Solution s){
        s.setBeginAt(this.findBetterBegin(problem.getD(), s.getSequenceOfJobs()));
    }

    protected int findBetterBegin(int d, ArrayList<Job> jobs) {
        return this.findBetterBegin(d, jobs, 0);
    }

    protected int findBetterBegin(int d, ArrayList<Job> jobs, int currentBegin) {
        int bg1 = 0;
        if (currentBegin > 0) {
            bg1 = this.findBetterBegin(d, jobs, 0, currentBegin);
        }
        int bg2 = this.findBetterBegin(d, jobs, currentBegin, d);
        int bg3 = this.findBetterBegin(d, jobs, 0, d);
        int fitnessBg1 = (int) problem.getPenalty(d, bg1, jobs);
        int fitnessBg2 = (int) problem.getPenalty(d, bg2, jobs);
        int fitnessBg3 = (int) problem.getPenalty(d, bg3, jobs);
        if (fitnessBg1 < fitnessBg2 && fitnessBg1 < fitnessBg3) {
            return bg1;
        } else if (fitnessBg2 < fitnessBg3) {
            return bg2;
        } else {
            return bg3;
        }
    }

    protected int findBetterBegin(int d, ArrayList<Job> jobs, int begin, int end) {
        int fitnessBegin = (int) problem.getPenalty(d, begin, jobs);
        int fitnessEnd = (int) problem.getPenalty(d, end, jobs);
        while (begin < end) {
            int mid = (end - begin) / 2;
            int fitnessMid = (int) problem.getPenalty(d, mid, jobs);
            if (fitnessMid < fitnessEnd) {
                end = mid;
                fitnessEnd = fitnessMid;
            } else if (fitnessMid < fitnessBegin) {
                begin = mid;
                fitnessBegin = fitnessMid;
            } else if (fitnessBegin < fitnessEnd) {
                return begin;
            } else {
                return end;
            }
        }
        return begin;
    }
    
    public void solutionVshapedSort(Solution s) {
        int d=problem.getD();
        int begin=s.getBeginAt();
        begin = this.findBetterBegin(d, s.getSequenceOfJobs(), begin);
        ArrayList<Job> before = new ArrayList<>();
        ArrayList<Job> after = new ArrayList<>(s.getSequenceOfJobs());
        
        int processingTimeSum = 0;
        int gap = d - processingTimeSum - begin;
        while (gap > 0) {
            Job j = after.remove(0);
            before.add(j);
            processingTimeSum = Problem.getSum_P(before);
            gap = d - (processingTimeSum + begin);
        }
        while (gap < 0) {
            Job j = before.remove(before.size() - 1);
            after.add(j);
            processingTimeSum = Problem.getSum_P(before);
            gap = d - (processingTimeSum + begin);
        }
        this.vshapedSort(before, after);
        before.addAll(after);
        begin = this.findBetterBegin(d, before, begin);
        s.setSequenceOfJobs(before);
        s.setBeginAt(begin);
    }
    
    protected void vshapedSort(ArrayList<Job> before, ArrayList<Job> after) {
        Collections.sort(before, new ProcessingTimeAlphaComparator());
        Collections.sort(after, new ProcessingTimeBetaComparator());
    }
    
    public String getOrderAsString(ArrayList<Job> sequenceOfJobs) {
        String order = "";
        for (Job j : sequenceOfJobs) {
            order += " " + j.getOrderId();
        }
        return order.trim();
    }
}
