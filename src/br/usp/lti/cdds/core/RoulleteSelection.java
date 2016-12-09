/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class RoulleteSelection {
    public static Solution select(ArrayList<Solution> solutionSet){
        //solutionSet must arrive sort
        double[] prob=new double[solutionSet.size()];
        double sum=0.0;
        int[] roullete=new int[100];
        //taking values and sum
        for(int i=0; i < prob.length; i++){
            sum+=solutionSet.get(i).getFitness();
            prob[i]=solutionSet.get(i).getFitness();
        }
        //prepare roullete
        int roulletepos=0;
        for(int i=0; i < prob.length; i++){
            prob[i]=1.0-(prob[i]/sum);
            int roulletepart= (int) (prob[i]*100);
            for(int k=0; k < roulletepart && roulletepos < roullete.length;k++){
                roullete[roulletepos++]=i;
            }
        }
        
        int truepos;
        //do{
            Random rdn=new Random();
            int selected=rdn.nextInt(roullete.length);
            truepos=roullete[selected];
        //}while(truepos==previousSelected);
        
        return solutionSet.get(truepos);    
    }
}
