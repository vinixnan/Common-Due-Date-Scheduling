/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

import java.util.ArrayList;

/**
 *
 * @author vinicius
 */
public abstract class OneSolutionImprovementHeuristic extends HeuristicBase {

    protected Solution solution;

    public OneSolutionImprovementHeuristic(Problem problem, ArrayList<Job> toOrder) {
        super(problem);
        this.solution = new Solution(toOrder);
        this.problem.evaluate(solution);
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public abstract void method(int d);

}
