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

    public ConstructionHeuristic(int size, double h) {
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
        this.orderedSet = this.setOrder(baseJobs, d, 0);
        this.beginAt = this.findBetterBegin(d, orderedSet);
        this.orderedSet = this.setOrder(orderedSet, d, this.beginAt);
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
