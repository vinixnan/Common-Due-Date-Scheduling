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
public class BitFlipMutationFoward  extends MutationBase{

    public BitFlipMutationFoward(Problem problem) {
        super(problem);
    }

    @Override
    public void method(Solution s) {
        ArrayList<ArrayList<Job>> splitted = this.split(s.getSequenceOfJobs(), problem.getD(), s.getBeginAt());
        ArrayList<Job> beforeD = splitted.get(0);
        ArrayList<Job> afterD = splitted.get(1);
        Random rdn=new Random();
        int pos1=rdn.nextInt(beforeD.size());
        Job one=beforeD.remove(pos1);
        afterD.add(one);
        Object[] returned=this.vshapedSort(beforeD, afterD, problem.getD());
        s.setSequenceOfJobs((ArrayList<Job>) returned[0]);
        s.setBeginAt((int) returned[1]);
    }
    
}
