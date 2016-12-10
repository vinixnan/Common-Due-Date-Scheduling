/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.metaheuristics;

import br.usp.lti.cdds.core.BinaryTournament;
import br.usp.lti.cdds.core.CrossoverBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.MutationBase;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import br.usp.lti.cdds.heuristics.BitFlipMutationBackward;
import br.usp.lti.cdds.heuristics.BitFlipMutationFoward;
import br.usp.lti.cdds.heuristics.ConstructionHeuristic;
import br.usp.lti.cdds.heuristics.OnePointCrossover;
import br.usp.lti.cdds.heuristics.OnePointCrossoverInternalAfter;
import br.usp.lti.cdds.heuristics.OnePointCrossoverInternalBefore;
import br.usp.lti.cdds.heuristics.PMXCrossover;
import br.usp.lti.cdds.heuristics.RandomHeuristic;
import br.usp.lti.cdds.heuristics.SwapMutation;
import br.usp.lti.cdds.heuristics.SwapMutationInternalAfter;
import br.usp.lti.cdds.heuristics.SwapMutationInternalBefore;
import br.usp.lti.cdds.heuristics.TwoPointCrossover;
import br.usp.lti.cdds.hyperheuristic.LowLevelHeuristic;
import br.usp.lti.cdds.hyperheuristic.MAB;
import br.usp.lti.cdds.util.SolutionComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class GeneticAlgorithm {

    private ArrayList<Solution> population;
    private final Problem problem;
    private final int populationSize;
    private final int maxGenerations;
    private final double crossProbability;
    private final double mutationProbability;
    private final double percentTaking;
    private final String crossType;
    private final String mutaType;
    private MAB hh;

    public GeneticAlgorithm(Problem problem, int populationSize, int maxGenerations, double crossProbability, double mutationProbability, String crossType, String mutaType, double percentTaking) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossProbability = crossProbability;
        this.mutationProbability = mutationProbability;
        this.crossType = crossType;
        this.mutaType = mutaType;
        this.percentTaking = percentTaking;
        this.hh=new MAB(5, 150);
        this.hh.generateLLH(problem);
    }

    private void initPopulation(ArrayList<Job> toOrder) {
        ConstructionHeuristic ch = new ConstructionHeuristic(problem, toOrder);
        ch.method(problem.getD());
        Solution s0 = ch.getSolution();
        problem.evaluate(s0);
        this.population = new ArrayList<>();
        this.population.add(s0);
        RandomHeuristic rh = new RandomHeuristic(problem, toOrder);
        for (int i = 1; i < this.populationSize; i++) {
            Solution s = rh.method();
            rh.solutionVshapedSort(s);
            problem.evaluate(s);
            this.population.add(s);
        }
    }

    private CrossoverBase getCrossover() {
        switch (this.crossType) {
            case "PMXCrossover":
                return new PMXCrossover(problem);
            case "OnePointCrossover":
                return new OnePointCrossover(problem);
            case "TwoPointCrossover":
                return new TwoPointCrossover(problem);
            case "OnePointCrossoverInternalBefore":
                return new OnePointCrossoverInternalBefore(problem);
            case "OnePointCrossoverInternalAfter":
                return new OnePointCrossoverInternalAfter(problem);

        }
        return null;
    }

    private CrossoverBase getCrossover(int id) {
        switch (id) {
            case 0:
                return new PMXCrossover(problem);
            case 1:
                return new OnePointCrossover(problem);
            case 2:
                return new TwoPointCrossover(problem);
            case 3:
                return new OnePointCrossoverInternalBefore(problem);
            case 4:
                return new OnePointCrossoverInternalAfter(problem);

        }
        return null;
    }

    private MutationBase getMutation() {
        switch (this.mutaType) {
            case "MAB":
                //call MAB method
                break;
            case "SwapMutation":
                return new SwapMutation(problem);
            case "BitFlipMutationBackward":
                return new BitFlipMutationBackward(problem);
            case "BitFlipMutationFoward":
                return new BitFlipMutationFoward(problem);
            case "SwapMutationInternalBefore":
                return new SwapMutationInternalBefore(problem);
            case "SwapMutationInternalAfter":
                return new SwapMutationInternalAfter(problem);
        }
        return null;
    }

    private MutationBase getMutation(int id) {
        switch (id) {
            case 0:
                return new SwapMutation(problem);
            case 3:
                return new SwapMutationInternalBefore(problem);
            case 4:
                return new SwapMutationInternalAfter(problem);
            case 1:
                return new BitFlipMutationBackward(problem);
            case 2:
                return new BitFlipMutationFoward(problem);
        }
        return null;
    }
    
    private double calcQuality(Solution parent1, Solution parent2, Solution[] offspring){
        Solution bestParent=parent1;
        if(parent2.getFitness() < bestParent.getFitness()){
            bestParent=parent2;
        }
        Solution bestOffspring=offspring[0];
        if(offspring[1].getFitness() < bestOffspring.getFitness()){
            bestOffspring=offspring[1];
        }
        return (bestParent.getFitness()-bestOffspring.getFitness())/bestParent.getFitness();
    }

    public Solution execute(ArrayList<Job> toOrder) {
        this.initPopulation(toOrder);
        int gen = 1;
        CrossoverBase crossover = this.getCrossover();
        MutationBase mutation = this.getMutation();
        RandomHeuristic rd=new RandomHeuristic(problem, toOrder);
        Random rdn = new Random();
        ArrayList<Solution> offspringPopulation;
        Collections.sort(this.population, new SolutionComparator());
        while (gen < this.maxGenerations) {
            //int cross = rdn.nextInt(1);
            //crossover = this.getCrossover(cross);
            offspringPopulation = new ArrayList<>();
            offspringPopulation.add(new Solution(population.get(0)));
            offspringPopulation.add(new Solution(population.get(1)));
            for (int i = 0; i < (populationSize / 2 - 1); i++) {
                Solution parent1 = BinaryTournament.select(population);
                Solution parent2 = BinaryTournament.select(population);
                Solution[] offspring;
                LowLevelHeuristic llh=this.hh.selectionMethod();
                crossover=llh.getCross();
                if (rdn.nextDouble() <= this.crossProbability && crossover!=null) {
                    offspring = crossover.method(parent1, parent2);
                    //crossover.solutionVshapedSort(offspring[0]);
                    //crossover.solutionVshapedSort(offspring[1]);
                } else {
                    offspring = new Solution[2];
                    offspring[0] = parent1;
                    offspring[1] = parent2;
                }
                for (Solution s : offspring) {
                    mutation=llh.getMutation();
                    if (rdn.nextDouble() <= this.mutationProbability && mutation!=null) {
                        mutation.method(s);
                        //crossover.solutionVshapedSort(s);
                    }
                    if(!(mutation instanceof SwapMutationInternalAfter || mutation instanceof SwapMutationInternalBefore)){
                        rd.solutionVshapedSort(s);
                    }
                    problem.evaluate(s);
                    offspringPopulation.add(s);
                }
                double quality=this.calcQuality(parent1, parent2, offspring);
                this.hh.addMetric(llh, quality);
            }
            /*
            ArrayList<Solution> joined = new ArrayList<>(this.population);
            joined.addAll(offspringPopulation);
            Collections.sort(joined, new SolutionComparator());
            this.population = new ArrayList<>();
            int max = (int) (populationSize * percentTaking);
            for (int i = 0; i < max; i++) {
                this.population.add(joined.get(i));
            }
            for (int i = 0; i < max; i++) {
                joined.remove(0);
            }
            while (max < populationSize) {
                int sort = rdn.nextInt(joined.size());

                this.population.add(joined.remove(sort));
                max++;

            }
             */
            this.population = offspringPopulation;
            Collections.sort(this.population, new SolutionComparator());
            Solution s = this.population.get(0);
            //add to sliding window
            gen++;
            //System.out.println(gen);
        }
        Solution s = this.population.get(0);
        //crossover.fixBeginOfSolution(s);
        //problem.evaluate(s);
        //LocalSearch ls=new LocalSearch(problem, s.getSequenceOfJobs());
        //ls.method(problem.getD());
        //s=ls.getSolution();
        return s;
    }
}
