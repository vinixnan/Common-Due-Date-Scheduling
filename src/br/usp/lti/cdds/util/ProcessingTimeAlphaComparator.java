/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.util;

import br.usp.lti.cdds.Job;
import java.util.Comparator;

/**
 *
 * @author vinicius
 */
public class ProcessingTimeAlphaComparator implements Comparator<Job> {

    @Override
    public int compare(Job o1, Job o2) {
        if(o1.processingAlpha()> o2.processingAlpha())
            return -1;
        else if(o1.processingAlpha()< o2.processingAlpha())
            return 1;
        return 0;
    }
    
}
