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
public class OnePointCrossoverInternalBefore extends CrossoverBase {

    public OnePointCrossoverInternalBefore(Problem problem) {
        super(problem);
    }

    @Override
    public Solution[] method(Solution p1, Solution p2) {
        Random rdn = new Random();
        Solution[] toReturn = new Solution[2];
        ArrayList<Job> parent1Sequence = new ArrayList<>(p1.getSequenceOfJobs());
        ArrayList<ArrayList<Job>> separedP1 = this.getOrderedTwoSet(parent1Sequence, problem.getD(), p1.getBeginAt());
        ArrayList<Job> parent1Before = separedP1.get(0);
        ArrayList<Job> parent1After = separedP1.get(1);

        ArrayList<Job> parent2Sequence = new ArrayList<>(p2.getSequenceOfJobs());
        ArrayList<ArrayList<Job>> separedP2 = this.getOrderedTwoSet(parent2Sequence, problem.getD(), p2.getBeginAt());
        ArrayList<Job> parent2Before = separedP2.get(0);
        ArrayList<Job> parent2After = separedP2.get(1);

        
        ArrayList<Job> offspring1 = new ArrayList<>();
        ArrayList<Job> offspring2 = new ArrayList<>();
        
        int size=Math.min(parent1Before.size(), parent2Before.size());
        if(size > 0){
            int cutPoint = rdn.nextInt(size);
            for (int i = 0; i < cutPoint; i++) {
                offspring1.add(parent2Before.get(i));
                offspring2.add(parent1Before.get(i));
            }
            for (int i = cutPoint; i < parent1Before.size(); i++) {
                offspring2.add(parent1Before.get(i));
            }
            for (int i = cutPoint; i < parent2Before.size(); i++) {
                offspring1.add(parent2Before.get(i));
            }

            offspring1.addAll(parent1After);
            offspring2.addAll(parent2After);
        }
        else{
            offspring1 = parent1Sequence;
            offspring2 = parent1Sequence;
        }
        
        System.out.println(offspring1.size()+" "+offspring2.size());

        this.problem.repairSolution(offspring1);
        toReturn[0] = new Solution(offspring1);
        this.solutionVshapedSort(toReturn[0]);
        this.problem.repairSolution(offspring2);
        toReturn[1] = new Solution(offspring2);
        this.solutionVshapedSort(toReturn[1]);
        return toReturn;
    }

}
