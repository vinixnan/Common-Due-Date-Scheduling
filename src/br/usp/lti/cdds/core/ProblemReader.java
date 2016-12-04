/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import br.usp.lti.cdds.util.FileUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vinicius
 */
public class ProblemReader {

    private Job[][] data;
    private int size;
    private ArrayList<Job> currentProblem;
    protected int k;

    public ProblemReader(int size) {
        this.size = size;
    }
    
    public void readDataFromFile() {
        this.data = FileUtils.getDataFromTextFile("data/sch" + this.size + ".txt");
        this.k = 0;
    }

    public boolean readNextProblem() {
        if (this.k < this.data.length) {
            this.currentProblem = new ArrayList<Job>(Arrays.asList(this.data[k++]));
            return true;
        } else {
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

    public Job[][] getData() {
        return data;
    }

    public void setData(Job[][] data) {
        this.data = data;
    }

    public ArrayList<Job> getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(ArrayList<Job> currentProblem) {
        this.currentProblem = currentProblem;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    
}
