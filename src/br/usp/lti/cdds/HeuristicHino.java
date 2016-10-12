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
public class HeuristicHino extends Scheduling {

    private ArrayList<Job> SE;
    private ArrayList<Job> ST;
    private ArrayList<Job> ordered;
    private double g;
    private double de;
    private double dt;

    public HeuristicHino(int size, double h) {
        super(size, h);

    }

    private Job getMaxPAlpha() {
        ProcessingTimeAlphaComparator comparator = new ProcessingTimeAlphaComparator();
        Collections.sort(this.baseJobs, comparator);
        return this.baseJobs.get(0);
    }

    private Job getMaxPBeta() {
        ProcessingTimeBetaComparator comparator = new ProcessingTimeBetaComparator();
        Collections.sort(this.baseJobs, comparator);
        return this.baseJobs.get(0);
    }

    private double fitness(int d, int idle, ArrayList<Job> SE, ArrayList<Job> ST) {
        ProcessingTimeAlphaComparator comparatorAlpha = new ProcessingTimeAlphaComparator();
        ProcessingTimeBetaComparator comparatorBeta = new ProcessingTimeBetaComparator();
        Collections.sort(SE, comparatorAlpha);
        Collections.sort(ST, comparatorBeta);
        ArrayList<Job> concat = new ArrayList<>(SE);
        concat.addAll(ST);
        return this.getPenalty(d, idle, concat);
    }
    
    private void addIfNotContains(Job j, int pos, ArrayList<Job> jobs){
        if(!jobs.contains(j)){
            jobs.add(pos, j);
        }
    }
    
    private void addIfNotContains(Job j, ArrayList<Job> jobs){
        this.addIfNotContains(j, Math.max(0, jobs.size()-1), jobs);
    }

    public void method(int d) {
        //STEP 1
        this.SE = new ArrayList<>();
        this.ST = new ArrayList<>();
        int H = this.getSum_P();
        g = Math.max(0, d - 0.5 * H);
        de = d - g;
        dt = g + H - d;

        while (this.baseJobs.size() > 0) {

            //STEP 2
            Job e = this.getMaxPAlpha();
            Job t = this.getMaxPBeta();
            //Step 3
            double EE = de - e.getProcessingTime();
            double TT = dt;

            if (EE <= 0) {
                //go to 5
                //Step 5
                if (g + EE < 0) {
                    this.addIfNotContains(t, 0, ST);
                    dt = dt - t.getProcessingTime();
                    this.baseJobs.remove(t);
                } else {
                    ArrayList<Job> SELINE = new ArrayList<>(this.SE);
                    ArrayList<Job> STLINE = new ArrayList<>(this.ST);
                    //STLINE.addAll(this.baseJobs);
                    int gLine = d - this.getSum_P(SELINE);
                    double F = this.fitness(d, gLine, SELINE, STLINE);
                    ArrayList<Job> SELINETWO = new ArrayList<>(SELINE);
                    this.addIfNotContains(e, SELINETWO);
                    ArrayList<Job> STLINETWO = new ArrayList<>(STLINE);
                    STLINETWO.remove(e);
                    int gLineTwo = d - this.getSum_P(SELINETWO);
                    double FTWO = this.fitness(d, gLineTwo, SELINETWO, STLINETWO);
                    if (F <= FTWO) {
                        this.addIfNotContains(t, 0, ST);
                        g = gLine;
                        dt = dt - t.getProcessingTime();
                        this.baseJobs.remove(t);
                    } else {
                        this.addIfNotContains(t, SE);
                        g = gLineTwo;
                        de = de - e.getProcessingTime();
                        this.baseJobs.remove(e);
                    }
                }
                //Go to 7
            }
            if (TT <= 0) {
                //IF  go to 6
                //STEP 6
                ArrayList<Job> SELINE = new ArrayList<>(this.SE);
                //SELINE.addAll(this.baseJobs);
                ArrayList<Job> STLINE = new ArrayList<>(this.ST);
                int gLine = d - this.getSum_P(SELINE);
                double F1 = this.fitness(d, gLine, SELINE, STLINE);
                ArrayList<Job> SELINETWO = new ArrayList<>(this.SE);
                //SELINETWO.addAll(this.baseJobs);
                SELINETWO.remove(t);
                ArrayList<Job> STLINETWO = new ArrayList<>(this.ST);
                this.addIfNotContains(t, 0, STLINETWO);
                int gLineTwo = d - this.getSum_P(SELINETWO);
                double F2 = this.fitness(d, gLineTwo, SELINETWO, STLINETWO);
                if (F1 <= F2) {
                    this.addIfNotContains(t, 0, ST);
                    g = gLine;
                    dt = dt - t.getProcessingTime();
                    this.baseJobs.remove(t);
                } else {
                    this.addIfNotContains(t, SE);
                    g = gLineTwo;
                    de = de - e.getProcessingTime();
                    this.baseJobs.remove(e);
                }
            }

            //Step4
            if (EE > TT) {
                this.addIfNotContains(e, SE);
                de = de - e.getProcessingTime();
                this.baseJobs.remove(e);
            } else if (EE < TT) {
                this.addIfNotContains(t, 0, ST);
                dt = dt - t.getProcessingTime();
                this.baseJobs.remove(t);
            } else if (EE == TT) {
                if (e.getEarliness() > t.getTardiness()) {
                    this.addIfNotContains(t, 0, ST);
                    dt = dt - t.getProcessingTime();
                    this.baseJobs.remove(t);
                } else {
                    this.addIfNotContains(t, SE);
                    de = de - e.getProcessingTime();
                    this.baseJobs.remove(e);
                }
            }

        }
        this.ordered=this.SE;
        this.ordered.addAll(this.ST);
    }

    public void setOrderedSet(ArrayList<Job> ordered) {
        this.ordered = ordered;
    }

    ArrayList<Job> getOrderedSet() {
        return ordered;
    }
    
    
}
