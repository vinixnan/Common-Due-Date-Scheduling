/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.HeuristicBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vinicius
 */
public class RandomHeuristic extends HeuristicBase {

    private ArrayList<Job> toOrder;

    public RandomHeuristic(Problem problem, ArrayList<Job> toOrder) {
        super(problem);
        this.toOrder = toOrder;
    }

    public Solution method() {
        Solution s = new Solution(this.toOrder);
        Collections.shuffle(s.getSequenceOfJobs());
        //this.fixBeginOfSolution(s);
        //this.solutionVshapedSort(s);
        return s;
    }

}
