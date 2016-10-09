package br.usp.lti.cdds;



public class Main {

    public static void main(String[] args) {
        Scheduling sdh = new Scheduling(10);
        int SUM_P = sdh.getSum_P();
        double h = 0.2;
        double d = SUM_P * h;
        System.out.println(SUM_P + " " + d);
        double minSum = sdh.getPenalty(); //a fazer: mtodo que calcula a penalidade dos jobs

    }

}
