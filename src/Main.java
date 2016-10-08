import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static int numberOfJobs;
    public static ArrayList<Job> jobs = new ArrayList<Job>();
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double minSum;
		
		getDataFromTextFile();
		int SUM_P = getSum_P();
		double h = 0.2;
		double d = SUM_P * h;
		System.out.println(SUM_P + " " + d);
		
		minSum = getPenalty(d); //a fazer: método que calcula a penalidade dos jobs
		
		
		
	}
	
	public static double getPenalty(double d){
		double sum=0;
		for(Job j: jobs) {
			
		}
		return sum;
	}
	
	public static void getDataFromTextFile() {
		ArrayList<Integer> intFromText = new ArrayList<Integer>();
        String[] valuesFromText;
        String linesFromFile = "";
        try {
			linesFromFile = readFile ("sch10.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        linesFromFile = linesFromFile.trim().replaceAll("\n", " ");
        linesFromFile = linesFromFile.replaceAll("\\s+", ",");
        valuesFromText = linesFromFile.split(",");
        for (String s: valuesFromText){
        	intFromText.add(Integer.parseInt(s));
        }
        intFromText.remove(0); // Remove primeiro valor = nº de problemas
        numberOfJobs = intFromText.get(0);
        System.out.println();
        intFromText.remove(0); // Remove o valor = nº de jobs
        for (int j = 0; j < numberOfJobs; j++){
            Job tempJob = new Job();
            tempJob.setProcessingTime(intFromText.get(0)); 	//* 
            tempJob.setEarliness(intFromText.get(1));  	//Copia valores
            tempJob.setTardiness(intFromText.get(2));; 	//*
            intFromText.remove(0);                 			//*
            intFromText.remove(0);                 			//Depois os remove
            intFromText.remove(0);                 			//*
            tempJob.setOrderId(j+1);
            jobs.add(tempJob);
            System.out.println("Tempo do job " + tempJob.getOrderId() + ": " + tempJob.getProcessingTime() + ", " + tempJob.getEarliness() + ", " + tempJob.getTardiness());
        }
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
        } 
        finally {
            br.close();
        }
    }
	
	public static int getSum_P(){
		int sum_p=0;
		for(Job j: jobs){
			sum_p += j.getProcessingTime();
		}
		return sum_p;
	}
}
	

