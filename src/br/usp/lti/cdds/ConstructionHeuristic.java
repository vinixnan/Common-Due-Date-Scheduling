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
public class ConstructionHeuristic extends Scheduling {

    private ArrayList<Job> paB;
    private ArrayList<Job> orderedSet;//B Set

    public ConstructionHeuristic(int size, double h) {
        super(size, h);
    }

    public void method(int d) {
        this.orderedSet = new ArrayList<>();
        this.paB = new ArrayList<>(baseJobs);
        Collections.sort(this.paB, new BetaAlphaComparator());
        Job j = this.paB.get(0);
        this.orderedSet.add(j);
        int processingTimeSum = this.getSum_P(orderedSet);
        int gap = d - processingTimeSum;

        while (gap > 0) {
            this.paB.remove(0);
            j = this.paB.get(0);
            this.orderedSet.add(j);
            processingTimeSum = this.getSum_P(orderedSet);
            gap = d - processingTimeSum;
        }
        if (gap < 0) {
            this.orderedSet.remove(this.orderedSet.size() - 1);
        }
        Collections.sort(this.orderedSet, new ProcessingTimeAlphaComparator());
        Collections.sort(this.paB, new ProcessingTimeBetaComparator());
        while (this.paB.size() > 0) {
            j = this.paB.remove(0);
            this.orderedSet.add(j);
        }
        this.beginAt = this.findBetterBegin(d, orderedSet);
    }

    private int findBetterBegin(int d, ArrayList<Job> jobs) {
        int begin = 0;
        int end = d;
        int chosen = Integer.MIN_VALUE;
        boolean stop = false;
        int fitnessBegin = (int) this.getPenalty(d, begin, jobs);
        int fitnessEnd = (int) this.getPenalty(d, end, jobs);

        while (!stop && begin < end) {
            int mid = (end - begin) / 2;
            int fitnessMid = (int) this.getPenalty(d, mid, jobs);
            if (fitnessBegin < fitnessEnd) {
                if (fitnessMid < fitnessBegin) {
                    begin = mid;
                    fitnessBegin = fitnessMid;
                    chosen = mid;
                } else if (fitnessMid < fitnessEnd) {
                    end = mid;
                    fitnessEnd = fitnessMid;
                    chosen = begin;
                } else {
                    stop = true;
                }
            } else if (fitnessMid < fitnessEnd) {
                end = mid;
                fitnessEnd = fitnessMid;
                chosen = mid;
            } else if (fitnessMid < fitnessBegin) {
                begin = mid;
                fitnessBegin = fitnessMid;
                chosen = end;
            } else {
                stop = true;
            }
        }
        return chosen;
    }

    public ArrayList<Job> getOrderedSet() {
        return orderedSet;
    }

    public void setOrderedSet(ArrayList<Job> orderedSet) {
        this.orderedSet = orderedSet;
    }

}
