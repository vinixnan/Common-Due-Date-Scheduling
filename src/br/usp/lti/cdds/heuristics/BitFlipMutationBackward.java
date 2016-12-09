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
public class BitFlipMutationBackward extends MutationBase {

    public BitFlipMutationBackward(Problem problem) {
        super(problem);
    }

    @Override
    public void method(Solution s) {
        ArrayList<ArrayList<Job>> splitted = this.split(s.getSequenceOfJobs(), problem.getD(), s.getBeginAt());
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        Random rdn = new Random();
        if(afterD.size() > 0){
            int pos2 = rdn.nextInt(afterD.size());
            Job other = afterD.remove(pos2);
            beforeD.add(other);
            this.vshapedSort(beforeD, afterD);
            ArrayList<Job> joined = new ArrayList<>(beforeD);
            joined.addAll(afterD);
            int begin = s.getBeginAt();
            begin = this.findBetterBegin(problem.getD(), joined, begin);
            s.setSequenceOfJobs(joined);
            s.setBeginAt(begin);
        }

    }

}
