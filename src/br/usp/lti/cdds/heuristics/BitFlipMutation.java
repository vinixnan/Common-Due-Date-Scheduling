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
public class BitFlipMutation extends MutationBase {

    public BitFlipMutation(Problem problem) {
        super(problem);
    }

    @Override
    public void method(Solution s) {
        ArrayList<ArrayList<Job>> splitted = this.split(s.getSequenceOfJobs(), problem.getD(), s.getBeginAt());
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        Random rdn = new Random();
        int chosen = rdn.nextInt(2);
        if (beforeD.size() > 0 && chosen == 1) {
            BitFlipMutationFoward bff = new BitFlipMutationFoward(problem);
            bff.method(s);
        }
        if (afterD.size() > 0 && chosen == 1) {
            BitFlipMutationBackward bfb = new BitFlipMutationBackward(problem);
            bfb.method(s);
        }
        if (afterD.size() > beforeD.size()) {
            BitFlipMutationBackward bfb = new BitFlipMutationBackward(problem);
            bfb.method(s);
        } else {
            BitFlipMutationFoward bff = new BitFlipMutationFoward(problem);
            bff.method(s);
        }

    }

}
