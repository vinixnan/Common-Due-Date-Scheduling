/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.OneSolutionImprovementHeuristic;
import br.usp.lti.cdds.core.Problem;
import java.util.ArrayList;

/**
 *
 * @author vinicius
 */
public class LocalSearch extends OneSolutionImprovementHeuristic {

    public LocalSearch(Problem problem, ArrayList<Job> toOrder) {
        super(problem, toOrder);
    }

    private Object[] localSearch(ArrayList<Job> base, int d, int begin) {
        //Split
        ArrayList<ArrayList<Job>> splitted = this.split(base, d, begin);
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        return this.localSearch(base, beforeD, afterD, d, begin);
    }

    private Object[] localSearch(ArrayList<Job> base, ArrayList<Job> beforeD, ArrayList<Job> afterD, int d, int begin) {
        ArrayList<ArrayList<Job>> allgenerated = new ArrayList<>();
        ArrayList<Integer> allbegins = new ArrayList<>();
        //foward
        for (int i = 0; i < beforeD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            //MOVEMENT
            Job j = myBefore.remove(i);
            myAfter.add(j);
            //MOVEMENT
            Object[] returned = this.vshapedSort(myBefore, myAfter, d);
            allgenerated.add((ArrayList<Job>) returned[0]);
            allbegins.add((Integer) returned[1]);
        }

        //back
        for (int i = 0; i < afterD.size(); i++) {
            ArrayList<Job> myBefore = new ArrayList<>(beforeD);
            ArrayList<Job> myAfter = new ArrayList<>(afterD);
            //MOVEMENT
            Job j = myAfter.remove(i);
            myBefore.add(j);
            //movement
            Object[] returned = this.vshapedSort(myBefore, myAfter, d);
            allgenerated.add((ArrayList<Job>) returned[0]);
            allbegins.add((Integer) returned[1]);
        }

        //find best
        double fitness = problem.getPenalty(d, begin, base);
        ArrayList<Job> best = base;
        for (int i = 0; i < allgenerated.size(); i++) {
            ArrayList<Job> aux = allgenerated.get(i);
            int auxBegin = allbegins.get(i);
            double auxFitness = problem.getPenalty(d, auxBegin, aux);
            if (auxFitness < fitness) {
                best = aux;
                fitness = auxFitness;
                begin = auxBegin;
            }
        }
        Object[] toRet = new Object[2];
        toRet[0] = best;
        toRet[1] = begin;
        return toRet;
    }

    @Override
    public void method(int d) {
        ConstructionHeuristic ch = new ConstructionHeuristic(problem, solution.getSequenceOfJobs());
        Object[] toRet = ch.sortUsingConstructionMethod(solution.getSequenceOfJobs(), d);
        ArrayList<Job> currentSet = (ArrayList<Job>) toRet[0];
        int currentBegin = (int) toRet[1];
        Object[] searchResult = this.localSearch(currentSet, d, currentBegin);
        ArrayList<Job> resultFromSearch = (ArrayList<Job>) searchResult[0];
        int searchBegin = (int) searchResult[1];

        double currentFitness = problem.getPenalty(d, currentBegin, currentSet);
        double searchFitness = problem.getPenalty(d, searchBegin, resultFromSearch);

        while (searchFitness < currentFitness) {
            currentSet = resultFromSearch;
            currentFitness = searchFitness;
            currentBegin = searchBegin;
            searchResult = this.localSearch(currentSet, d, currentBegin);
            resultFromSearch = (ArrayList<Job>) searchResult[0];
            searchBegin = (int) searchResult[1];
        }
        solution.setSequenceOfJobs(currentSet);
        solution.setBeginAt(currentBegin);
        problem.evaluate(solution);
    }
}
