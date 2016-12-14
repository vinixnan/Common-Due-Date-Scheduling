/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.heuristics;

import br.usp.lti.cdds.core.CrossoverBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class PMXCrossover extends CrossoverBase {

    public PMXCrossover(Problem problem) {
        super(problem);
    }

    @Override
    public Solution[] method(Solution p1, Solution p2) {
        Random rdn = new Random();

        Solution[] offspring = new Solution[2];

        offspring[0] = new Solution(p1);
        offspring[1] = new Solution(p2);

        int permutationLength = p1.getSequenceOfJobs().size();

        ArrayList<Job> parent1Vector = p1.getSequenceOfJobs();
        ArrayList<Job> parent2Vector = p2.getSequenceOfJobs();
        ArrayList<Job> offspring1Vector = offspring[0].getSequenceOfJobs();
        ArrayList<Job> offspring2Vector = offspring[1].getSequenceOfJobs();

        int cuttingPoint1;
        int cuttingPoint2;

        //      STEP 1: Get two cutting points
        cuttingPoint1 = rdn.nextInt(permutationLength - 1);
        cuttingPoint2 = rdn.nextInt(permutationLength - 1);
        while (cuttingPoint2 == cuttingPoint1) {
            cuttingPoint2 = rdn.nextInt(permutationLength - 1);
        }

        if (cuttingPoint1 > cuttingPoint2) {
            int swap;
            swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        } // if
        //      STEP 2: Get the subchains to interchange
        HashMap<Integer, Job> replacement1 = new HashMap<>();
        HashMap<Integer, Job> replacement2 = new HashMap<>();
        for (int i = 1; i < permutationLength+1; i++) {
            replacement1.put(i, null);
            replacement2.put(i, null);
        }

        //      STEP 3: Interchange
        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
            //offspring1Vector[i] = parent2Vector[i];
            offspring1Vector.set(i, parent2Vector.get(i));
            //offspring2Vector[i] = parent1Vector[i];
            offspring2Vector.set(i, parent1Vector.get(i));
            //replacement1[parent2Vector[i]] = parent1Vector[i];
            replacement1.put(parent2Vector.get(i).getOrderId(), parent1Vector.get(i));
            //replacement2[parent1Vector[i]] = parent2Vector[i];
            replacement2.put(parent1Vector.get(i).getOrderId(), parent2Vector.get(i));
        } // for

        //      STEP 4: Repair offsprings
        for (int i = 0; i < permutationLength; i++) {
            if ((i >= cuttingPoint1) && (i <= cuttingPoint2)) {
                continue;
            }

            //int n1 = parent1Vector[i];//que job esta em i
            Job n1 = parent1Vector.get(i);
            //int m1 = replacement1[n1];
            Job m1 = replacement1.get(n1.getOrderId());

            //int n2 = parent2Vector[i];
            Job n2 = parent2Vector.get(i);
            //int m2 = replacement2[n2];
            Job m2 = replacement2.get(n2.getOrderId());

            while (m1 != null) {
                n1 = m1;
                m1 = replacement1.get(n1.getOrderId());
            } // while
            while (m2 != null) {
                n2 = m2;
                m2 = replacement2.get(n2.getOrderId());
            } // while
            //offspring1Vector[i] = n1;
            offspring1Vector.set(i, n1);
            //offspring2Vector[i] = n2;
            offspring2Vector.set(i, n2);
        } // for
        //int begin = this.findBetterBegin(problem.getD(), offspring1Vector, offspring[0].getBeginAt());
        //offspring[0].setBeginAt(begin);
        //begin = this.findBetterBegin(problem.getD(), offspring1Vector, offspring[1].getBeginAt());
        //offspring[1].setBeginAt(begin);
        
        return offspring;
    }

}
