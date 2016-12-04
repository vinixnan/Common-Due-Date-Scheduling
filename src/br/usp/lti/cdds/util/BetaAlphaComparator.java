/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.util;

import br.usp.lti.cdds.core.Job;
import java.util.Comparator;

/**
 *
 * @author vinicius
 */
public class BetaAlphaComparator implements Comparator<Job> {

    @Override
    public int compare(Job o1, Job o2) {
        
        if(o1.betaAlpha() == o2.betaAlpha()){
            if(o1.getTardiness() > o2.getTardiness()){
                return -1;
            }
            else if(o1.getTardiness() < o2.getTardiness()){
                return 1;
            }
        }
        else if(o1.betaAlpha() > o2.betaAlpha())
            return -1;
        else if(o1.betaAlpha() < o2.betaAlpha())
            return 1;
        return 0;
    }
    
}
