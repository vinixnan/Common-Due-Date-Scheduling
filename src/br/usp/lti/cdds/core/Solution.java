/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class Solution {

    private double fitness;

    protected int beginAt;

    protected ArrayList<Job> sequenceOfJobs;

    public Solution(ArrayList<Job> sequenceOfJobs) {
        this.sequenceOfJobs=new ArrayList<>(sequenceOfJobs);
        this.beginAt = 0;
        fitness = 0;
    }
    
    public Solution(Solution s) {
        this.sequenceOfJobs=new ArrayList<>(s.getSequenceOfJobs());
        this.beginAt = s.getBeginAt();
        fitness = s.getFitness();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(int beginAt) {
        this.beginAt = beginAt;
    }

    public ArrayList<Job> getSequenceOfJobs() {
        return sequenceOfJobs;
    }

    public void setSequenceOfJobs(ArrayList<Job> sequenceOfJobs) {
        this.sequenceOfJobs=new ArrayList<>(sequenceOfJobs);
    }

    public String getOrderAsString() {
        String order = "";
        for (Job j : this.sequenceOfJobs) {
            order += " " + j.getOrderId();
        }
        return order.trim();
    }
}
