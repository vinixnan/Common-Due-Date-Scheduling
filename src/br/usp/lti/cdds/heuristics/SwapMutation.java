/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.MutationBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class SwapMutation extends MutationBase {

    public SwapMutation(Problem problem) {
        super(problem);
    }

    @Override
    public void method(Solution s) {
        ArrayList<ArrayList<Job>> splitted = this.split(s.getSequenceOfJobs(), problem.getD(), s.getBeginAt());
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        if (beforeD.size() > 0 && afterD.size() > 0) {
            Random rdn = new Random();
            int pos1 = rdn.nextInt(beforeD.size());
            int pos2 = rdn.nextInt(afterD.size());
            Job beforeElement = beforeD.get(pos1);
            Job afterElement = afterD.get(pos2);
            beforeD.set(pos1, afterElement);
            afterD.set(pos2, beforeElement);
            //this.vshapedSort(beforeD, afterD);
            ArrayList<Job> joined=new ArrayList<>(beforeD);
            joined.addAll(afterD);
            //int begin = this.findBetterBegin(problem.getD(), joined, s.getBeginAt());
            s.setSequenceOfJobs(joined);
            //s.setBeginAt(begin);
        }
    }

}
