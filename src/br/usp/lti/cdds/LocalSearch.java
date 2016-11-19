/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class LocalSearch extends Scheduling {

    private ArrayList<Job> paB;

    public LocalSearch(int size, double h) {
        super(size, h);
    }

    private ArrayList<ArrayList<Job>> getOrderedTwoSet(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<Job> ordered = new ArrayList<>();
        ArrayList<Job> auxSet = new ArrayList<>(toOrder);
        Collections.sort(auxSet, new BetaAlphaComparator());
        Job j = auxSet.get(0);
        ordered.add(j);
        int processingTimeSum = this.getSum_P(ordered);
        int gap = d - processingTimeSum - begin;
        while (gap > 0) {
            auxSet.remove(0);
            j = auxSet.get(0);
            ordered.add(j);
            processingTimeSum = this.getSum_P(ordered);
            gap = d - processingTimeSum - begin;
        }
        if (gap < 0) {
            ordered.remove(ordered.size() - 1);
        }
        ArrayList<ArrayList<Job>> toReturn = new ArrayList<>();
        toReturn.add(ordered);
        toReturn.add(auxSet);
        return toReturn;
    }

    private ArrayList<Job> findOrderInConstruction(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<ArrayList<Job>> twoSet = this.getOrderedTwoSet(toOrder, d, begin);
        ArrayList<Job> ordered = twoSet.get(0);
        ArrayList<Job> auxSet = twoSet.get(1);
        Collections.sort(ordered, new ProcessingTimeAlphaComparator());
        Collections.sort(auxSet, new ProcessingTimeBetaComparator());
        while (auxSet.size() > 0) {
            Job j = auxSet.remove(0);
            ordered.add(j);
        }
        return ordered;
    }

    private Object[] order(ArrayList<Job> base, int d, int beginOriginal) {
        ArrayList<Job> order = this.findOrderInConstruction(base, d, beginOriginal);
        int begin = this.findBetterBegin(d, order);
        double orderFitness = this.getPenalty(d, begin, order);
        boolean finished = true;
        while (!finished) {
            ArrayList<Job> newOrder = this.findOrderInConstruction(order, d, begin);
            int secondBegin = this.findBetterBegin(d, newOrder);
            double newOrderFitness = this.getPenalty(d, secondBegin, newOrder);
            if (orderFitness > newOrderFitness) {
                order = newOrder;
                orderFitness = newOrderFitness;
                begin = secondBegin;
            } else {
                finished = true;
            }
        }
        Object[] toRet = new Object[2];
        toRet[0] = order;
        toRet[1] = begin;
        return toRet;
    }

    private Object[] localSearch(ArrayList<Job> base, int d, int begin) {
        ArrayList<ArrayList<Job>> allgenerated = new ArrayList<>();
        ArrayList<ArrayList<Job>> splitBase = this.getOrderedTwoSet(base, d, begin);
        ArrayList<Job> beforeD = splitBase.get(0);
        ArrayList<Job> afterD = splitBase.get(1);
        //foward
        for (int i = 0; i < beforeD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            Job j = myBefore.remove(i);
            myAfter.add(j);

            Collections.sort(myBefore, new ProcessingTimeAlphaComparator());
            Collections.sort(myAfter, new ProcessingTimeBetaComparator());

            while (myAfter.size() > 0) {
                j = myAfter.remove(0);
                myBefore.add(j);
            }
            allgenerated.add(myBefore);
        }

        //back
        for (int i = 0; i < afterD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            Job j = myAfter.remove(i);
            myBefore.add(j);

            Collections.sort(myBefore, new ProcessingTimeAlphaComparator());
            Collections.sort(myAfter, new ProcessingTimeBetaComparator());

            while (myAfter.size() > 0) {
                j = myAfter.remove(0);
                myBefore.add(j);
            }
            allgenerated.add(myBefore);
        }

        //find best
        double fitness = this.getPenalty(d, begin, base);
        ArrayList<Job> best = base;
        for (int i = 0; i < allgenerated.size(); i++) {
            ArrayList<Job> aux = allgenerated.get(i);
            int auxBegin = this.findBetterBegin(d, aux);
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

    public void method(int d) {
        Object[] toRet = this.order(this.baseJobs, d, 0);
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

    private int findBetterBegin(int d, ArrayList<Job> jobs) {
        int begin = 0;
        int end = d;
        int fitnessBegin = (int) this.getPenalty(d, begin, jobs);
        int fitnessEnd = (int) this.getPenalty(d, end, jobs);
        while (begin < end) {
            int mid = (end - begin) / 2;
            int fitnessMid = (int) this.getPenalty(d, mid, jobs);
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
}
