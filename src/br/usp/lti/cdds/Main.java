package br.usp.lti.cdds;



public class Main {

    public static void main(String[] args) {
        
        int size=10;
        double h=0.2;
        if(args.length==2){
            size=Integer.parseInt(args[0]);
            h=Double.parseDouble(args[1]);
        }
        HeuristicII sdh=new HeuristicII(size,h);
        sdh.readDataFromFile();
        while(sdh.readNextProblem()){
            int d = (int) Math.round(sdh.getSum_P() * h);
            //sdh.printStatus(sdh.getBaseJobs());
            sdh.method(d);
            sdh.printStatus(sdh.getOrderedSet());
        }
        
        
        
        
        
        
        
        
        
        

    }

}
