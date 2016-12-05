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
public class OnePointCrossover extends CrossoverBase {

    public OnePointCrossover(Problem problem) {
        super(problem);
    }

    @Override
    public Solution[] method(Solution p1, Solution p2) {
        Random rdn = new Random();
        Solution[] toReturn = new Solution[2];
        ArrayList<Job> parent1Sequence = p1.getSequenceOfJobs();
        ArrayList<Job> parent2Sequence = p2.getSequenceOfJobs();
        int cutPoint = rdn.nextInt(parent1Sequence.size());
        ArrayList<Job> offspring1= new ArrayList<>();
        ArrayList<Job> offspring2 = new ArrayList<>();
        for (int i = 0; i < cutPoint; i++) {
            offspring1.add(parent1Sequence.get(i));
            offspring2.add(parent2Sequence.get(i));
        }
        for (int i = cutPoint; i < parent1Sequence.size(); i++) {
            offspring1.add(parent2Sequence.get(i));
            offspring2.add(parent1Sequence.get(i));
        }
        //offspring 1
        this.repairSolution(offspring1, parent1Sequence);
        this.repairSolution(offspring2, parent2Sequence);
        System.err.println(offspring1.size()+" "+offspring2.size());
        Object[] returned = this.vshapedSort(offspring1, problem.getD());
        toReturn[0] = new Solution((ArrayList<Job>) returned[0]);
        toReturn[0].setBeginAt((int) returned[1]);
        //ofspring 2
        returned = this.vshapedSort(offspring2, problem.getD());
        toReturn[1] = new Solution((ArrayList<Job>) returned[0]);
        toReturn[1].setBeginAt((int) returned[1]);
        return toReturn;
    }

}
