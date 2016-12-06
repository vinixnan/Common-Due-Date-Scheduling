/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import br.usp.lti.cdds.util.JobIDComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author vinicius
 */
public class Problem {

    private int d;
    private double h;
    private int size;
    private ArrayList<Job> listOfJobs;//just a list of jobs to fix solutions

    public Problem(int d, double h, ArrayList<Job> listOfJobs) {
        this.d = d;
        this.h = h;
        this.listOfJobs = new ArrayList<>(listOfJobs);
        Collections.sort(listOfJobs, new JobIDComparator());
        this.size = this.listOfJobs.size();
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public static int getSum_P(ArrayList<Job> jobs) {
        int sum_p = 0;
        for (Job j : jobs) {
            sum_p += j.getProcessingTime();
        }
        return sum_p;
    }

    private int[][] generateTimes(int d, int idle, ArrayList<Job> jobs) {
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

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void evaluate(Solution s) {
        double fitness = this.getPenalty(d, s.getBeginAt(), s.getSequenceOfJobs());
        s.setFitness(fitness);
    }

    public void repairSolution(Solution s) {
        this.repairSolution(s.getSequenceOfJobs());
    }

    public void repairSolution(ArrayList<Job> sJobs) {
        HashMap<Job, Integer> qtds = new HashMap<>();
        for (int i = 0; i < listOfJobs.size(); i++) {
            Job j = listOfJobs.get(i);
            qtds.put(j, Collections.frequency(sJobs, j));
        }

        for (int i = 0; i < listOfJobs.size(); i++) {
            Job currentPresent = listOfJobs.get(i);
            while (qtds.get(currentPresent) > 1) {
                boolean stop = false;
                for (int j = 0; !stop && j < listOfJobs.size(); j++) {
                    Job currentAbsent = listOfJobs.get(j);
                    if (qtds.get(currentAbsent) == 0) {
                        int pos = sJobs.lastIndexOf(currentPresent);
                        sJobs.set(pos, currentAbsent);
                        qtds.put(currentAbsent, 1);
                        qtds.put(currentPresent, qtds.get(currentPresent) - 1);
                        stop = true;
                        //System.out.println(getOrderAsString(sJobs));
                    }
                }

            }
        }
        qtds = new HashMap<>();
        for (int i = 0; i < listOfJobs.size(); i++) {
            Job j = listOfJobs.get(i);
            qtds.put(j, Collections.frequency(sJobs, j));
        }
    }

    public double getSum_P(Solution s) {
        return this.getSum_P(s.getSequenceOfJobs());
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Job> getListOfJobs() {
        return listOfJobs;
    }

    public void setListOfJobs(ArrayList<Job> listOfJobs) {
        this.listOfJobs = listOfJobs;
    }

    public String getOrderAsString(ArrayList<Job> sequenceOfJobs) {
        String order = "";
        for (Job j : sequenceOfJobs) {
            order += " " + j.getOrderId();
        }
        return order.trim();
    }

}
