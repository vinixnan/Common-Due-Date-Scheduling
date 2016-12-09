/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.CrossoverBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class TwoPointCrossover extends CrossoverBase {

    public TwoPointCrossover(Problem problem) {
        super(problem);
    }

    @Override
    public Solution[] method(Solution p1, Solution p2) {
        Random rdn = new Random();
        Solution[] toReturn = new Solution[2];
        ArrayList<Job> parent1Sequence = p1.getSequenceOfJobs();
        ArrayList<Job> parent2Sequence = p2.getSequenceOfJobs();
        int cutPoint1 = rdn.nextInt(parent1Sequence.size());
        int cutPoint2 = rdn.nextInt(parent1Sequence.size());
        int aux=cutPoint1;
        cutPoint1=Math.min(cutPoint1, cutPoint2);
        cutPoint2=Math.max(aux, cutPoint2);
        
        ArrayList<Job> offspring1= new ArrayList<>();
        ArrayList<Job> offspring2 = new ArrayList<>();
        for (int i = 0; i < cutPoint1; i++) {
            offspring1.add(parent1Sequence.get(i));
            offspring2.add(parent2Sequence.get(i));
        }
        for (int i = cutPoint1; i < cutPoint2; i++) {
            offspring1.add(parent2Sequence.get(i));
            offspring2.add(parent1Sequence.get(i));
        }
        for (int i = cutPoint2; i < parent1Sequence.size(); i++) {
            offspring1.add(parent1Sequence.get(i));
            offspring2.add(parent2Sequence.get(i));
        }
        this.problem.repairSolution(offspring1);
        toReturn[0] = new Solution(offspring1);
        this.problem.repairSolution(offspring2);
        toReturn[1] = new Solution(offspring2);
        return toReturn;
    }

}
