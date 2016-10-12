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
public class HeuristicI extends Scheduling {

    private ArrayList<Job> A;
    private ArrayList<Job> B;
    private ArrayList<Job> orderedSet;//Final Set

    public HeuristicI(int size, double h) {
        super(size,h);
        this.A = new ArrayList<>();
        this.B = new ArrayList<>();
        this.orderedSet = new ArrayList<>();
    }
    
    private ArrayList<Job> propertyIOrdering(ArrayList<Job> jobs, int d){
        int sum=0;
        int i=0;
        ArrayList<Job> partI=new ArrayList<>();
        ArrayList<Job> partII=new ArrayList<>();
        Job j=jobs.get(i);
        sum+=j.getProcessingTime();
        while(sum < d){
            partI.add(j);
            i++;
            j=jobs.get(i);
            sum+=j.getProcessingTime();
        }
        while(i < jobs.size()){
           partII.add(jobs.get(i++));
        }
        Collections.sort(partI, new ProcessingTimeAlphaComparator());
        Collections.sort(partII, new ProcessingTimeBetaComparator());
        partI.addAll(partII);
        return partI;
    }
    
    private int bestToSelect(int d, ArrayList<Job> origin, ArrayList<Job> dest){
        int bestPos=Integer.MIN_VALUE;
        double bestFitness=Double.POSITIVE_INFINITY;
        for(int i=0; i < origin.size(); i++){
            ArrayList<Job> destiny=new ArrayList<>(dest);//copy from original
            destiny.add(origin.get(i)); //add current
            double fitness=this.getPenalty(d,0, destiny);
            if(fitness < bestFitness){
                bestFitness=fitness;
                bestPos=i;
            }
        }
        return bestPos;
    }

    public void method(int d) {
        this.A=this.propertyIOrdering(this.baseJobs, d);
        
        while(this.A.size() > 0){
            int selected=this.bestToSelect(d, A, B);
            B.add(A.remove(selected));
        }
        this.orderedSet=B;
    }

    public ArrayList<Job> getOrderedSet() {
        return orderedSet;
    }

    public void setOrderedSet(ArrayList<Job> orderedSet) {
        this.orderedSet = orderedSet;
    }

}
