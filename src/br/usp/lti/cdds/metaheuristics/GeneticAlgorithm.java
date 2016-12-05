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
import br.usp.lti.cdds.heuristics.ConstructionHeuristic;
import br.usp.lti.cdds.heuristics.OnePointCrossover;
import br.usp.lti.cdds.heuristics.RandomHeuristic;
import br.usp.lti.cdds.heuristics.SwapMutation;
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
    private Problem problem;
    private int populationSize;
    private int maxEvaluations;
    private double crossProbability;
    private double mutationProbability;

    public GeneticAlgorithm(Problem problem, int populationSize, int maxEvaluations, double crossProbability, double mutationProbability) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.maxEvaluations = maxEvaluations;
        this.crossProbability = crossProbability;
        this.mutationProbability = mutationProbability;
    }

    private void initPopulation(ArrayList<Job> toOrder) {
        ConstructionHeuristic ch = new ConstructionHeuristic(problem, toOrder);
        ch.method(problem.getD());
        RandomHeuristic rh = new RandomHeuristic(problem, toOrder);
        Solution s = ch.getSolution();
        problem.evaluate(s);
        this.population = new ArrayList<>();
        this.population.add(s);
        for (int i = 1; i < this.populationSize; i++) {
            s = rh.method();
            problem.evaluate(s);
            this.population.add(s);
        }
    }

    public Solution execute(ArrayList<Job> toOrder) {
        this.initPopulation(toOrder);
        int eval = populationSize;
        CrossoverBase crossover;
        MutationBase mutation;
        crossover = new OnePointCrossover(problem);
        mutation = new SwapMutation(problem);
        Random rdn = new Random();
        ArrayList<Solution> offspringPopulation;
        Collections.sort(this.population, new SolutionComparator());
        while (eval < this.maxEvaluations) {
            offspringPopulation = new ArrayList<>();
            offspringPopulation.add(new Solution(population.get(0)));
            offspringPopulation.add(new Solution(population.get(1)));
            for (int i = 0; i < (populationSize / 2 - 1); i++) {
                Solution parent1 = BinaryTournament.select(population);
                Solution parent2 = BinaryTournament.select(population);
                Solution[] offspring;
                if (rdn.nextDouble() <= this.crossProbability) {
                    offspring = crossover.method(parent1, parent2);
                } else {
                    offspring = new Solution[2];
                    offspring[0] = parent1;
                    offspring[1] = parent2;
                }
                for (Solution s : offspring) {
                    if (rdn.nextDouble() <= this.mutationProbability) {
                        mutation.method(s);
                    }
                    problem.evaluate(s);
                    offspringPopulation.add(s);
                    //System.out.println(s.getSequenceOfJobs().size());
                }
            }
            this.population = offspringPopulation;
            Collections.sort(this.population, new SolutionComparator());
            eval += 2;
        }
        return this.population.get(0);
    }
}
