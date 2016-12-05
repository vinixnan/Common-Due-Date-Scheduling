/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.core;

/**
 *
 * @author vinicius
 */
public abstract class CrossoverBase extends HeuristicBase{
    
    public CrossoverBase(Problem problem) {
        super(problem);
    }
    
    public abstract Solution[] method(Solution p1, Solution p2);
}
