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
    private ArrayList<Job> orderedSet;//B Set

    public LocalSearch(int size, double h) {
        super(size, h);
    }

    private ArrayList<Job> setOrder(ArrayList<Job> toOrder, int d, int begin) {
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
        Collections.sort(ordered, new ProcessingTimeAlphaComparator());
        Collections.sort(auxSet, new ProcessingTimeBetaComparator());
        while (auxSet.size() > 0) {
            j = auxSet.remove(0);
            ordered.add(j);
        }
        return ordered;
    }

    public void method(int d) {
        ArrayList<Job> order = this.setOrder(baseJobs, d, 0);
        int begin = this.findBetterBegin(d, order);
        double orderFitness = this.getPenalty(d, begin, order);
        boolean finished=false;
        while (!finished) {
            ArrayList<Job> newOrder = this.setOrder(order, d, begin);
            int secondBegin = this.findBetterBegin(d, newOrder);
            double newOrderFitness = this.getPenalty(d, secondBegin, newOrder);
            if (orderFitness > newOrderFitness) {
                order=newOrder;
                orderFitness=newOrderFitness;
                begin=secondBegin;
            } else {
                finished=true;
            }
        }
        this.orderedSet=order;
        this.beginAt=begin;
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

    public ArrayList<Job> getOrderedSet() {
        return orderedSet;
    }

    public void setOrderedSet(ArrayList<Job> orderedSet) {
        this.orderedSet = orderedSet;
    }

}
