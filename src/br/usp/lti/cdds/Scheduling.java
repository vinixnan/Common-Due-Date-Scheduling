package br.usp.lti.cdds;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vinicius
 */
public class Scheduling {

    private int size;
    private ArrayList<Job> jobs;

    public Scheduling(int size) {
        this.size = size;
        this.jobs = FileUtils.getDataFromTextFile("data/sch" + this.size + ".txt");
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public int getSum_P() {
        int sum_p = 0;
        for (Job j : jobs) {
            sum_p += j.getProcessingTime();
        }
        return sum_p;
    }

    public double getPenalty() {
        double sum = 0;
        for (Job j : jobs) {

        }
        return sum;
    }
}
