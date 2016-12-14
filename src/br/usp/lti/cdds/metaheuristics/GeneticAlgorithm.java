/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.metaheuristics;

import br.usp.lti.cdds.core.CrossoverBase;
import br.usp.lti.cdds.core.Job;
import br.usp.lti.cdds.core.MutationBase;
import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.core.Solution;
import br.usp.lti.cdds.heuristics.BitFlipMutation;
import br.usp.lti.cdds.heuristics.BitFlipMutationBackward;
import br.usp.lti.cdds.heuristics.BitFlipMutationFoward;
import br.usp.lti.cdds.heuristics.ConstructionHeuristic;
import br.usp.lti.cdds.heuristics.OnePointCrossover;
import br.usp.lti.cdds.heuristics.PMXCrossover;
import br.usp.lti.cdds.heuristics.RandomHeuristic;
import br.usp.lti.cdds.heuristics.SwapMutation;
import br.usp.lti.cdds.heuristics.SwapMutationInternalAfter;
import br.usp.lti.cdds.heuristics.SwapMutationInternalBefore;
import br.usp.lti.cdds.heuristics.TwoPointCrossover;
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
    private final String crossType;
    private final String mutaType;

    public GeneticAlgorithm(Problem problem, int populationSize, int maxGenerations, double crossProbability, double mutationProbability, String crossType, String mutaType) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossProbability = crossProbability;
        this.mutationProbability = mutationProbability;
        this.crossType = crossType;
        this.mutaType = mutaType;
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
            case "BitFlipMutation":
                return new BitFlipMutation(problem);
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

    public Solution execute(ArrayList<Job> toOrder) {
        this.initPopulation(toOrder);
        int gen = 1;
        CrossoverBase crossover = this.getCrossover();
        MutationBase mutation = this.getMutation();
        RandomHeuristic rd = new RandomHeuristic(problem, toOrder);
        Random rdn = new Random();
        ArrayList<Solution> offspringPopulation;
        Collections.sort(this.population, new SolutionComparator());
        while (gen < this.maxGenerations) {
            offspringPopulation = new ArrayList<>();
            offspringPopulation.add(new Solution(population.get(0)));
            offspringPopulation.add(new Solution(population.get(1)));
            //offspringPopulation.add(new Solution(population.get(2)));
            //offspringPopulation.add(new Solution(population.get(3)));
            for (int i = 0; i < (populationSize / 2 - 1); i++) {
                Solution parent1 = RoulleteSelection.select(population);
                Solution parent2 = RoulleteSelection.select(population);
                Solution[] offspring;
                if (rdn.nextDouble() <= this.crossProbability && crossover != null) {
                    offspring = crossover.method(parent1, parent2);
                } else {
                    offspring = new Solution[2];
                    offspring[0] = parent1;
                    offspring[1] = parent2;
                }
                for (Solution s : offspring) {
                    if (rdn.nextDouble() <= this.mutationProbability && mutation != null) {
                        mutation.method(s);
                    }
                    if (!(mutation instanceof SwapMutationInternalAfter || mutation instanceof SwapMutationInternalBefore)) {
                        rd.solutionVshapedSort(s);
                    } else {
                        rd.fixBeginOfSolution(s);
                    }
                    problem.evaluate(s);
                    offspringPopulation.add(s);
                }
            }
            this.population = offspringPopulation;
            Collections.sort(this.population, new SolutionComparator());
            gen++;
        }
        Solution s = this.population.get(0);
        return s;
    }
}
