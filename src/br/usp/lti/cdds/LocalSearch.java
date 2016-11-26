/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class LocalSearch extends ConstructionHeuristic {

    public LocalSearch(int size, double h) {
        super(size, h);
    }
    
    private Object[] localSearch(ArrayList<Job> base, int d, int begin) {
        ArrayList<Job> beforeD = new ArrayList<>();
        ArrayList<Job> afterD = new ArrayList<>(base);
        int processingTimeSum = 0;
        int gap = d - processingTimeSum - begin;
        while (gap > 0) {
            Job j = afterD.remove(0);
            beforeD.add(j);
            processingTimeSum = this.getSum_P(beforeD);
            gap = d - processingTimeSum - begin;
        }
        if (gap < 0) {
            Job j = beforeD.remove(beforeD.size() - 1);
            afterD.add(j);
        }
        return this.localSearch(base, beforeD, afterD, d, begin);
    }
    
    private Object[] vshapedSort(ArrayList<Job> beforeD, ArrayList<Job> afterD, int d){
        
        ArrayList<Job> all=new ArrayList<>();
        Collections.sort(beforeD, new ProcessingTimeAlphaComparator());
        Collections.sort(afterD, new ProcessingTimeBetaComparator());
        all.addAll(beforeD);
        all.addAll(afterD);
        int currentBegin=0;
        int nowBegin=0;
        ArrayList<Job> currentOrder=all;
        ArrayList<Job> nowOrder=all;
        double currentFitness=this.getPenalty(d, currentBegin, all);
        double nowFitness=currentFitness;
        
        do {
            if (nowFitness < currentFitness) {
                currentFitness = nowFitness;
                currentOrder = nowOrder;
                currentBegin = nowBegin;
            }
            nowOrder=new ArrayList<>();
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            
            
            int processingTimeSum = 0;
            int gap = d - processingTimeSum - currentBegin;
            while (gap > 0) {
                Job j = myAfter.remove(0);
                myBefore.add(j);
                processingTimeSum = this.getSum_P(myBefore);
                gap = d - processingTimeSum - currentBegin;
            }
            if (gap < 0) {
                Job j = myBefore.remove(myBefore.size() - 1);
                myAfter.add(j);
            }
            Collections.sort(myBefore, new ProcessingTimeAlphaComparator());
            Collections.sort(myAfter, new ProcessingTimeBetaComparator());
            nowOrder.addAll(myBefore);
            nowOrder.addAll(myAfter);
            nowBegin = this.findBetterBegin(d, nowOrder, currentBegin);
            nowFitness = this.getPenalty(d, nowBegin, nowOrder);

        } while (nowFitness < currentFitness);
        currentBegin=this.hardfindBetterBegin(currentOrder, d, currentBegin);
        Object[] toRet = new Object[2];
        toRet[0] = currentOrder;
        toRet[1] = currentBegin;
        return toRet;
    }

    private Object[] localSearch(ArrayList<Job> base, ArrayList<Job> beforeD, ArrayList<Job> afterD, int d, int begin) {
        ArrayList<ArrayList<Job>> allgenerated = new ArrayList<>();
        ArrayList<Integer> allbegins = new ArrayList<>();
        //foward
        for (int i = 0; i < beforeD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            ArrayList<Job> all = new ArrayList<>();
            
            //MOVEMENT
            Job j = myBefore.remove(i);
            myAfter.add(j);
            //MOVEMENT
            Object[] returned=this.vshapedSort(myBefore, myAfter, d);
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
            Object[] returned=this.vshapedSort(myBefore, myAfter, d);
            allgenerated.add((ArrayList<Job>) returned[0]);
            allbegins.add((Integer) returned[1]);
        }

        //find best
        double fitness = this.getPenalty(d, begin, base);
        ArrayList<Job> best = base;
        for (int i = 0; i < allgenerated.size(); i++) {
            ArrayList<Job> aux = allgenerated.get(i);
            int auxBegin=allbegins.get(i);
            double auxFitness = this.getPenalty(d, auxBegin, aux);
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

    @Override
    public void method(int d) {
        Object[] toRet = this.sortUsingConstructionMethod(this.baseJobs, d);
        ArrayList<Job> currentSet = (ArrayList<Job>) toRet[0];
        int currentBegin = (int) toRet[1];
        Object[] searchResult = this.localSearch(currentSet, d, currentBegin);
        ArrayList<Job> resultFromSearch = (ArrayList<Job>) searchResult[0];
        int searchBegin = (int) searchResult[1];

        double currentFitness = this.getPenalty(d, currentBegin, currentSet);
        double searchFitness = this.getPenalty(d, searchBegin, resultFromSearch);

        while (searchFitness < currentFitness) {
            currentSet = resultFromSearch;
            currentFitness = searchFitness;
            currentBegin = searchBegin;
            searchResult = this.localSearch(currentSet, d, currentBegin);
            resultFromSearch = (ArrayList<Job>) searchResult[0];
            searchBegin = (int) searchResult[1];
        }

        this.orderedSet = currentSet;
        this.beginAt = currentBegin;
    }
}
