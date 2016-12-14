/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.OneSolutionImprovementHeuristic;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.util.BetaAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeAlphaComparator;
import br.usp.lti.cdds.util.ProcessingTimeBetaComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class ConstructionHeuristic extends OneSolutionImprovementHeuristic {

    public ConstructionHeuristic(Problem problem, ArrayList<Job> toOrder) {
        super(problem, toOrder);
    }

    protected ArrayList<Job> findOrderInConstruction(ArrayList<Job> toOrder, int d, int begin) {
        ArrayList<ArrayList<Job>> twoSet = this.getOrderedTwoSet(toOrder, d, begin);
        ArrayList<Job> ordered = twoSet.get(0);
        ArrayList<Job> auxSet = twoSet.get(1);
        while (auxSet.size() > 0) {
            Job j = auxSet.remove(0);
            ordered.add(j);
        }
        return ordered;
    }

    

    public Object[] sortUsingConstructionMethod(ArrayList<Job> currentOrder, int d) {
        ArrayList<Job> nowOrder = currentOrder;
        int currentBegin = this.findBetterBegin(d, nowOrder, 0);
        int nowBegin = currentBegin;
        double currentFitness = problem.getPenalty(d, currentBegin, currentOrder);
        double nowFitness = currentFitness;

        do {
            if (nowFitness < currentFitness) {
                currentFitness = nowFitness;
                currentOrder = nowOrder;
                currentBegin = nowBegin;
            }
            nowOrder = this.findOrderInConstruction(currentOrder, d, currentBegin);
            nowBegin = this.findBetterBegin(d, nowOrder, currentBegin);
            nowFitness = problem.getPenalty(d, nowBegin, nowOrder);

        } while (nowFitness < currentFitness);
        //currentBegin=this.hardfindBetterBegin(currentOrder, d, currentBegin);
        Object[] toRet = new Object[2];
        toRet[0] = currentOrder;
        toRet[1] = currentBegin;
        return toRet;
    }
    
    @Override
    public void method(int d) {
        Object[] returned = this.sortUsingConstructionMethod(solution.getSequenceOfJobs(), d);
        solution.setSequenceOfJobs((ArrayList<Job>) returned[0]);
        solution.setBeginAt((int) returned[1]);
        problem.evaluate(solution);
    }

}
