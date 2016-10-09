package br.usp.lti.cdds;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
public class FileUtils {

    public static ArrayList<Job> getDataFromTextFile(String filename) {
        ArrayList<Job> jobs = new ArrayList<Job>();
        ArrayList<Integer> intFromText = new ArrayList<Integer>();
        String[] valuesFromText;
        String linesFromFile = "";
        try {
            linesFromFile = readFile(filename);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        linesFromFile = linesFromFile.trim().replaceAll("\n", " ");
        linesFromFile = linesFromFile.replaceAll("\\s+", ",");
        valuesFromText = linesFromFile.split(",");
        for (String s : valuesFromText) {
            intFromText.add(Integer.parseInt(s));
        }
        intFromText.remove(0); // Remove primeiro valor = n de problemas
        int numberOfJobs = intFromText.get(0);
        System.out.println();
        intFromText.remove(0); // Remove o valor = n de jobs
        for (int j = 0; j < numberOfJobs; j++) {
            Job tempJob = new Job();
            tempJob.setProcessingTime(intFromText.get(0)); 	//* 
            tempJob.setEarliness(intFromText.get(1));  	//Copia valores
            tempJob.setTardiness(intFromText.get(2));; 	//*
            intFromText.remove(0);                 			//*
            intFromText.remove(0);                 			//Depois os remove
            intFromText.remove(0);                 			//*
            tempJob.setOrderId(j + 1);
            jobs.add(tempJob);
            System.out.println("Tempo do job " + tempJob.getOrderId() + ": " + tempJob.getProcessingTime() + ", " + tempJob.getEarliness() + ", " + tempJob.getTardiness());
        }
        return jobs;
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
