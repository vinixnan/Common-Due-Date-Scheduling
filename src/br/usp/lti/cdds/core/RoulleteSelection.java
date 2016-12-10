/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class RoulleteSelection {
    public static Solution select(ArrayList<Solution> solutionSet){
        double[] prob=new double[solutionSet.size()];
        for(int i=0; i < prob.length; i++){
            prob[i]=solutionSet.get(i).getFitness();
        }
        double min=Doubles.min(prob);
        double max=Doubles.max(prob);
        double sum=0;
        for(int i=0; i < prob.length; i++){
           prob[i]=(max-prob[i])/(max-min);
           sum+=prob[i];
        }
        
        for(int i=0; i < prob.length; i++){
           prob[i]=prob[i]/sum;
        }
        
        //prepare roullete
        int[] roullete=new int[100];
        int roulletepos=0;
        
        
        for(int i=0; i < prob.length; i++){
            double roulletepart=Math.round(prob[i]*100.0);
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
