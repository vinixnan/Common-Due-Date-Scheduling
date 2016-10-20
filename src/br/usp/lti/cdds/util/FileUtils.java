package br.usp.lti.cdds.util;

import br.usp.lti.cdds.Job;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static String[] readFormatted(String filename) {
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
        return valuesFromText;
    }

    public static Job[][] getDataFromTextFile(String filename) {
        String[] valuesFromText = FileUtils.readFormatted(filename);
        ArrayList<Integer> intFromText = new ArrayList<Integer>();
        for (String s : valuesFromText) {
            intFromText.add(Integer.parseInt(s));
        }
        int qtdTests = intFromText.remove(0); // Remove primeiro valor = n de problemas
        int numberOfJobs = intFromText.get(0);
        Job[][] toReturn = new Job[qtdTests][numberOfJobs];
        for (int i = 0; i < qtdTests; i++) {
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
                toReturn[i][j]=tempJob;
            }
        }
        return toReturn;
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
    
    public static int[][] readBenchMark(String benchmarkdir){
        int[][] toReturn=new int[10][4];
        try {
            String readed=FileUtils.readFile(benchmarkdir);
            String[] lines=readed.split("\n");
            int k=0;
            for(String line : lines){
                String[] numbers=line.split(";");
                for(int h=0; h < numbers.length; h++){
                    toReturn[k][h]=Integer.parseInt(numbers[h]);
                }
                k++;
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toReturn;
    }
}
