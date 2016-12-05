/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.util;

import br.usp.lti.cdds.core.Solution;
import java.util.Comparator;

/**
 *
 * @author vinicius
 */
public class SolutionComparator implements Comparator<Solution> {

    @Override
    public int compare(Solution o1, Solution o2) {
        if (o1.getFitness() > o2.getFitness()) {
            return 1;
        } else if (o1.getFitness() < o2.getFitness()) {
            return -1;
        }
        return 0;
    }

}
