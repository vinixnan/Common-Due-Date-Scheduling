/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.hyperheuristic;

import br.usp.lti.cdds.core.CrossoverBase;
import br.usp.lti.cdds.core.MutationBase;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.heuristics.*;

/**
 *
 * @author vinicius
 */
public class LowLevelHeuristic {

    protected CrossoverBase cross;
    protected MutationBase mutation;
    protected Problem problem;
    protected String name;

    public LowLevelHeuristic(int crossId, int mutaId, Problem problem) {
        this.name="";
        this.problem = problem;
        this.cross = this.getCrossover(crossId);
        this.mutation = this.getMutation(mutaId);
        
    }

    private CrossoverBase getCrossover(int id) {
        switch (id) {
            case 0:
                name+="PMXCrossover";
                return new PMXCrossover(problem);
            case 1:
                name+="OnePointCrossover";
                return new OnePointCrossover(problem);
            case 2:
                name+="TwoPointCrossover";
                return new TwoPointCrossover(problem);
            case 3:
                name+="OnePointCrossoverInternalBefore";
                return new OnePointCrossoverInternalBefore(problem);
            case 4:
                name+="OnePointCrossoverInternalAfter";
                return new OnePointCrossoverInternalAfter(problem);
            case 5:
                return null;

        }
        return null;
    }

    private MutationBase getMutation(int id) {
        switch (id) {
            case 0:
                name+="SwapMutation";
                return new SwapMutation(problem);
            case 3:
                name+="SwapMutationInternalBefore";
                return new SwapMutationInternalBefore(problem);
            case 4:
                name+="SwapMutationInternalAfter";
                return new SwapMutationInternalAfter(problem);
            case 1:
                name+="BitFlipMutationBackward";
                return new BitFlipMutationBackward(problem);
            case 2:
                name+="BitFlipMutationFoward";
                return new BitFlipMutationFoward(problem);
            case 5:
                return null;
        }
        return null;
    }

    public CrossoverBase getCross() {
        return cross;
    }

    public void setCross(CrossoverBase cross) {
        this.cross = cross;
    }

    public MutationBase getMutation() {
        return mutation;
    }

    public void setMutation(MutationBase mutation) {
        this.mutation = mutation;
    }

    String getAgentName() {
        return name;
    }
}
