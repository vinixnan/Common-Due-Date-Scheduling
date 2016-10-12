/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.BetaAlphaComparatorReverse;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class HeuristicII extends Scheduling {

    private ArrayList<Job> paB;
    private ArrayList<Job> orderedSet;//B Set

    public HeuristicII(int size, double h) {
        super(size,h);
        this.paB = new ArrayList<>();
        this.orderedSet = new ArrayList<>();
    }

    public void method(int d) {
        this.paB=new ArrayList<>(baseJobs);
        Collections.sort(this.paB, new BetaAlphaComparator());
        int maxSize = this.baseJobs.size() / 2;
        
        Job j = this.paB.get(0);
        this.orderedSet.add(j);
        int processingTimeSum = this.getSum_P(orderedSet);
        int gap = d - processingTimeSum;
        
        while (this.orderedSet.size() < maxSize && gap > 0) {
            this.paB.remove(0);
            j = this.paB.get(0);
            this.orderedSet.add(j);
            processingTimeSum = this.getSum_P(orderedSet);
            gap = d - processingTimeSum;
        }
        if(gap < 0){
            this.orderedSet.remove(this.orderedSet.size()-1);
        }
        Collections.sort(this.orderedSet, new ProcessingTimeAlphaComparator());
        Collections.sort(this.paB, new ProcessingTimeBetaComparator());
        while (this.paB.size() > 0) {
            j = this.paB.remove(0);
            this.orderedSet.add(j);
        }
    }

    public ArrayList<Job> getOrderedSet() {
        return orderedSet;
    }

    public void setOrderedSet(ArrayList<Job> orderedSet) {
        this.orderedSet = orderedSet;
    }

}
