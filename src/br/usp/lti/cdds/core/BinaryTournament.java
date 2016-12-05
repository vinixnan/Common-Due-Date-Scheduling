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
public class BinaryTournament {
    public static Solution select(ArrayList<Solution> solutionSet){
        Random rdn=new Random();
        Solution solution1, solution2;
        solution1 = solutionSet.get(rdn.nextInt(solutionSet.size()));   
        solution2 = solutionSet.get(rdn.nextInt(solutionSet.size()));   

    if (solutionSet.size() >= 2)
    	while (solution1 == solution2){
            solution2 = solutionSet.get(rdn.nextInt(solutionSet.size()));  
        }
        if (solution1.getFitness() < solution2.getFitness())
            return solution1;
        else if(solution2.getFitness() < solution1.getFitness())
            return solution2;
        else
        if (rdn.nextDouble() < 0.5)
            return solution1;
        else
            return solution2;                       
    }
}
