package br.usp.lti.cdds;

import java.util.ArrayList;



public class TestMain {

    public static void main(String[] args) {
        
        ArrayList<Job> stubJobs=new ArrayList<>();
        double h=0.2;
        HeuristicII sdh=new HeuristicII(8,h);
        
        //EXAMPLE FROM ARTICLE ORDERED
        stubJobs=new ArrayList<>();
        stubJobs.add(new Job(1, 7,  2, 14));
        stubJobs.add(new Job(8, 6,  4, 14));
        stubJobs.add(new Job(2, 1,  8, 7));
        stubJobs.add(new Job(4, 6,  9, 9));
        stubJobs.add(new Job(7, 5,  7, 5));
        stubJobs.add(new Job(6, 14, 5, 9));
        stubJobs.add(new Job(5, 13, 5, 7));
        stubJobs.add(new Job(3, 18, 4, 8));
        System.out.println("Resultados da Heuristica II segundo artigo");
        sdh.printStatus(stubJobs);
        System.out.println("");
        
        
        //EXAMPLE FROM ARTICLE ORDERED 2
        stubJobs=new ArrayList<>();
        stubJobs.add(new Job(6, 14, 5, 9));
        stubJobs.add(new Job(2, 1,  8, 7));
        stubJobs.add(new Job(8, 6,  4, 14));
        stubJobs.add(new Job(1, 7,  2, 14));
        stubJobs.add(new Job(4, 6,  9, 9));
        stubJobs.add(new Job(7, 5,  7, 5));
        stubJobs.add(new Job(5, 13, 5, 7));
        stubJobs.add(new Job(3, 18, 4, 8));
        System.out.println("Resultados da Heuristica I segundo artigo");
        sdh.printStatus(stubJobs);
        System.out.println("");
        
        
        //EXAMPLE FROM ARTICLE NON ORDERED
        stubJobs=new ArrayList<>();
        stubJobs.add(new Job(1, 7,  2, 14));
        stubJobs.add(new Job(2, 1,  8, 7));
        stubJobs.add(new Job(3, 18, 4, 8));
        stubJobs.add(new Job(4, 6,  9, 9));
        stubJobs.add(new Job(5, 13, 5, 7));
        stubJobs.add(new Job(6, 14, 5, 9));
        stubJobs.add(new Job(7, 5,  7, 5));
        stubJobs.add(new Job(8, 6,  4, 14));
        System.out.println("Resultados Sem Ordem");
        sdh.printStatus(stubJobs);
        
        
        sdh.setBaseJobs(stubJobs);
        int d = (int) Math.round(sdh.getSum_P() * h);
        sdh.method(d);
        System.out.println("Resultados depois da Ordem");
        sdh.printStatus(sdh.getOrderedSet());
        
        
        
        
        
        
        
        
        

    }

}
