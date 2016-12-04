/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.HeuristicBase;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class ConstructionHeuristic extends HeuristicBase {

    public ConstructionHeuristic(Problem problem, ArrayList<Job> toOrder) {
        super(problem, toOrder);
    }

    protected ArrayList<Job> findOrderInConstruction(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<ArrayList<Job>> twoSet = this.getOrderedTwoSet(toOrder, d, begin);
        ArrayList<Job> ordered = twoSet.get(0);
        ArrayList<Job> auxSet = twoSet.get(1);
        while (auxSet.size() > 0) {
            Job j = auxSet.remove(0);
            ordered.add(j);
        }
        return ordered;
    }

    protected ArrayList<ArrayList<Job>> getOrderedTwoSet(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<Job> ordered = new ArrayList<>();
        ArrayList<Job> auxSet = new ArrayList<>(toOrder);
        Collections.sort(auxSet, new BetaAlphaComparator());
        int processingTimeSum = 0;
        int gap = d - processingTimeSum - begin;
        while (gap > 0) {
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

    protected Object[] sortUsingConstructionMethod(ArrayList<Job> currentOrder, int d) {
        ArrayList<Job> nowOrder = currentOrder;
        int currentBegin = this.findBetterBegin(d, nowOrder, 0);
        int nowBegin = currentBegin;
        double currentFitness = problem.getPenalty(d, currentBegin, currentOrder);
        double nowFitness = currentFitness;

        do {
            if (nowFitness < currentFitness) {
                currentFitness = nowFitness;
                currentOrder = nowOrder;
                currentBegin = nowBegin;
            }
            nowOrder = this.findOrderInConstruction(currentOrder, d, currentBegin);
            nowBegin = this.findBetterBegin(d, nowOrder, currentBegin);
            nowFitness = problem.getPenalty(d, nowBegin, nowOrder);

        } while (nowFitness < currentFitness);
        //currentBegin=this.hardfindBetterBegin(currentOrder, d, currentBegin);
        Object[] toRet = new Object[2];
        toRet[0] = currentOrder;
        toRet[1] = currentBegin;
        return toRet;
    }

    protected int hardfindBetterBegin(ArrayList<Job> jobs, int d, int foundBegin) {
        int fitnessCurrent = (int) problem.getPenalty(d, foundBegin, jobs);
        int toReturn = foundBegin;
        int middle = d / 2;
        int distanceFromD = Math.abs(d - foundBegin);
        int distanceFromZero = Math.abs(foundBegin - 0);
        int distanceFromMiddle = Math.abs(foundBegin - middle);
        if (distanceFromD < distanceFromZero && distanceFromD < distanceFromMiddle) {
            for (int i = foundBegin + 1; i < d; i++) {
                int fitnessNow = (int) problem.getPenalty(d, i, jobs);
                if (fitnessNow < fitnessCurrent) {
                    toReturn = i;
                }
            }
        } else if (distanceFromZero < distanceFromMiddle) {
            for (int i = 0; i < foundBegin - 1; i++) {
                int fitnessNow = (int) problem.getPenalty(d, i, jobs);
                if (fitnessNow < fitnessCurrent) {
                    toReturn = i;
                }
            }
        } else {
            for (int i = foundBegin - distanceFromMiddle; i < foundBegin - 1; i++) {
                int fitnessNow = (int) problem.getPenalty(d, i, jobs);
                if (fitnessNow < fitnessCurrent) {
                    toReturn = i;
                }
            }
            for (int i = foundBegin + 1; i < foundBegin + distanceFromMiddle; i++) {
                int fitnessNow = (int) problem.getPenalty(d, i, jobs);
                if (fitnessNow < fitnessCurrent) {
                    toReturn = i;
                }
            }
        }

        return toReturn;
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

    @Override
    public void method(int d) {
        Object[] returned = this.sortUsingConstructionMethod(solution.getSequenceOfJobs(), d);
        solution.setSequenceOfJobs((ArrayList<Job>) returned[0]);
        solution.setBeginAt((int) returned[1]);
        problem.evaluate(solution);
    }

}
