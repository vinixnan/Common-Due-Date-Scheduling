/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds;

import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import com.google.common.primitives.Ints;
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
        this.beginAt=this.findBetterBegin(d, paB);
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
    }
    
    private int findBetterBegin(int d, ArrayList<Job> jobs){
        double[] percentOfD={0.0, 0.01, 0.02, 0.05, 0.1};
        int[] values=new int[percentOfD.length];
        for(int i=0; i<percentOfD.length;i++){
            int begin=(int) (d*percentOfD[i]);
            values[i]=(int) this.getPenalty(d, begin, jobs);
        }
        int min=Ints.min(values);
        int pos=Ints.indexOf(values, min);
        return (int) (d*percentOfD[pos]);
    }

    public ArrayList<Job> getOrderedSet() {
        return orderedSet;
    }

    public void setOrderedSet(ArrayList<Job> orderedSet) {
        this.orderedSet = orderedSet;
    }

}
