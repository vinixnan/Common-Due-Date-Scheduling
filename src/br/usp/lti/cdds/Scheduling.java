package br.usp.lti.cdds;

import br.usp.lti.cdds.util.FileUtils;
import java.util.ArrayList;
import java.util.Arrays;

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

    protected int size;
    protected ArrayList<Job> baseJobs;
    private double h;
    private Job[][] data;
    int k;

    public Scheduling(int size, double h) {
        this.size = size;
        this.baseJobs = null;
        this.h = h;
        this.data = null;
    }

    public void readDataFromFile() {
        this.data = FileUtils.getDataFromTextFile("data/sch" + this.size + ".txt");
        this.k = 0;
    }
    
    public boolean readNextProblem() {
        if(this.k < this.data.length){
            this.baseJobs=new ArrayList<Job>(Arrays.asList(this.data[k++]));
            return true;
        }
        else{
            return false;
        }
    }

    public void printAllData() {
        for (int i = 0; i < this.data.length; i++) {
            System.out.println("K=" + (i + 1));
            for (int j = 0; j < this.data[i].length; j++) {
                Job tempJob = this.data[i][j];
                System.out.println("Tempo do job " + tempJob.getOrderId() + ": " + tempJob.getProcessingTime() + ", " + tempJob.getEarliness() + ", " + tempJob.getTardiness());
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Job> getBaseJobs() {
        return baseJobs;
    }

    public void setBaseJobs(ArrayList<Job> baseJobs) {
        this.baseJobs = baseJobs;
    }

    public int getSum_P(ArrayList<Job> jobs) {
        int sum_p = 0;
        for (Job j : jobs) {
            sum_p += j.getProcessingTime();
        }
        return sum_p;
    }

    public int[][] generateTimes(int d, int idle, ArrayList<Job> jobs) {
        int previousTime = idle;
        int[][] allvalues = new int[3][jobs.size()];
        Arrays.fill(allvalues[0], 0);//completionTime
        Arrays.fill(allvalues[1], 0);//earliness
        Arrays.fill(allvalues[2], 0);//tardiness
        for (int i = 0; i < jobs.size(); i++) {
            allvalues[0][i] = previousTime + jobs.get(i).getProcessingTime();
            allvalues[1][i] = Math.max(d - allvalues[0][i], 0);
            allvalues[2][i] = Math.max(allvalues[0][i] - d, 0);
            previousTime = allvalues[0][i];
        }
        return allvalues;
    }

    public double getPenalty(int d, int idle, ArrayList<Job> jobs) {
        int[][] allvalues = this.generateTimes(d, idle, jobs);
        double sum = 0;
        for (int i = 0; i < jobs.size(); i++) {
            Job j = jobs.get(i);
            sum += j.getEarliness() * allvalues[1][i] + j.getTardiness() * allvalues[2][i];
        }
        return sum;
    }

    public int getSum_P() {
        return this.getSum_P(baseJobs);
    }

    public int[][] generateTimes(int d) {
        return this.generateTimes(d, 0, baseJobs);
    }

    public double getPenalty(int d) {
        return this.getPenalty(d, 0, baseJobs);
    }

    public String getOrderAsString(ArrayList<Job> jobs) {
        String order = "";
        for (Job j : jobs) {
            order += " " + j.getOrderId();
        }
        return order.trim();
    }

    public void printStatus(ArrayList<Job> jobs) {
        double SUM_P = this.getSum_P(jobs);
        int d = (int) Math.round(SUM_P * h);
        double minSum = this.getPenalty(d, 0, jobs); //a fazer: mtodo que calcula a penalidade dos jobs
        System.err.println("Sum " + SUM_P + " D=" + d + " Fitness=" + minSum);
        System.err.println(this.getOrderAsString(jobs));

    }
}
