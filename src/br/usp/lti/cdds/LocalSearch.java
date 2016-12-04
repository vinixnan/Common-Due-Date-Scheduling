/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class LocalSearch extends ConstructionHeuristic {

    public LocalSearch(Problem problem, ArrayList<Job> toOrder) {
        super(problem, toOrder);
    }

    private Object[] localSearch(ArrayList<Job> base, int d, int begin) {
        //Split
        ArrayList<ArrayList<Job>> splitted = this.split(base, d, begin);
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        return this.localSearch(base, beforeD, afterD, d, begin);
    }

    private Object[] localSearch(ArrayList<Job> base, ArrayList<Job> beforeD, ArrayList<Job> afterD, int d, int begin) {
        ArrayList<ArrayList<Job>> allgenerated = new ArrayList<>();
        ArrayList<Integer> allbegins = new ArrayList<>();
        //foward
        for (int i = 0; i < beforeD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            //MOVEMENT
            Job j = myBefore.remove(i);
            myAfter.add(j);
            //MOVEMENT
            Object[] returned = this.vshapedSort(myBefore, myAfter, d);
            allgenerated.add((ArrayList<Job>) returned[0]);
            allbegins.add((Integer) returned[1]);
        }

        //back
        for (int i = 0; i < afterD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            //MOVEMENT
            Job j = myAfter.remove(i);
            myBefore.add(j);
            //movement
            Object[] returned = this.vshapedSort(myBefore, myAfter, d);
            allgenerated.add((ArrayList<Job>) returned[0]);
            allbegins.add((Integer) returned[1]);
        }

        //find best
        double fitness = problem.getPenalty(d, begin, base);
        ArrayList<Job> best = base;
        for (int i = 0; i < allgenerated.size(); i++) {
            ArrayList<Job> aux = allgenerated.get(i);
            int auxBegin = allbegins.get(i);
            double auxFitness = problem.getPenalty(d, auxBegin, aux);
            if (auxFitness < fitness) {
                best = aux;
                fitness = auxFitness;
                begin = auxBegin;
            }
        }
        Object[] toRet = new Object[2];
        toRet[0] = best;
        toRet[1] = begin;
        return toRet;
    }

    private Object[] vshapedSort(ArrayList<Job> beforeD, ArrayList<Job> afterD, int d) {

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
            while (gap > 0) {
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

    @Override
    public void method(int d) {
        Object[] toRet = this.sortUsingConstructionMethod(solution.getSequenceOfJobs(), d);
        ArrayList<Job> currentSet = (ArrayList<Job>) toRet[0];
        int currentBegin = (int) toRet[1];
        Object[] searchResult = this.localSearch(currentSet, d, currentBegin);
        ArrayList<Job> resultFromSearch = (ArrayList<Job>) searchResult[0];
        int searchBegin = (int) searchResult[1];

        double currentFitness = problem.getPenalty(d, currentBegin, currentSet);
        double searchFitness = problem.getPenalty(d, searchBegin, resultFromSearch);

        while (searchFitness < currentFitness) {
            currentSet = resultFromSearch;
            currentFitness = searchFitness;
            currentBegin = searchBegin;
            searchResult = this.localSearch(currentSet, d, currentBegin);
            resultFromSearch = (ArrayList<Job>) searchResult[0];
            searchBegin = (int) searchResult[1];
        }
        solution.setSequenceOfJobs(currentSet);
        solution.setBeginAt(currentBegin);
        problem.evaluate(solution);
    }
}
