/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.lti.cdds.hyperheuristic;

import br.usp.lti.cdds.core.Problem;
import br.usp.lti.cdds.hyperheuristic.mab.SlidingWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class MAB {

    protected HashMap<String, Integer> nt;
    protected double C;
    protected SlidingWindow sl;
    private boolean alltried;
    protected ArrayList<LowLevelHeuristic> listAgents;

    public MAB(double C, int slSize) {
        this.C = C;
        this.alltried = false;
        this.sl = new SlidingWindow(slSize);
    }

    public void generateLLH(Problem problem) {

        this.listAgents = new ArrayList<>();
        int qtdcross = 3;//no null no internal
        int qtdmuta = 6;
        for (int i = 0; i < qtdcross; i++) {
            for (int j = 0; j < qtdmuta; j++) {
                LowLevelHeuristic llh = new LowLevelHeuristic(i, j, problem);
                this.listAgents.add(llh);
            }
        }
    }

    protected double sumNt(HashMap<String, Integer> nt) {
        int qtd = 0;
        for (LowLevelHeuristic op : this.listAgents) {
            qtd += nt.get(op.getAgentName());
        }
        return qtd;
    }

    protected double equation(LowLevelHeuristic op, HashMap<String, Double> rewards, HashMap<String, Integer> nt, double sumNt) {
        /*  KE LI code
         http://www.cs.cityu.edu.hk/~51888309/code/bandits.zip
         temp1 = 2 * Math.log(total_usage);
         temp2 = temp1 / strategy_usage[i];
         temp3 = Math.sqrt(temp2);
         quality[i] = rewards[i] + scale_ * temp3;
         */

        double numerator = 2 * Math.log(((int) sumNt));
        double denominator = nt.getOrDefault(op.getAgentName(), 0);
        double fraction = numerator / denominator;
        double sqrt = Math.sqrt(fraction);
        double ucb_value = rewards.get(op.getAgentName());
        return ucb_value + this.C * sqrt;
    }

    public int selectOperator(HashMap<String, Double> rewards, HashMap<String, Integer> nt) {
        if (!this.alltried) {
            for (int i = 0; i < this.listAgents.size(); i++) {
                LowLevelHeuristic op = this.listAgents.get(i);
                if (nt.get(op.getAgentName()) == 0) {
                    //never tried
                    return i;
                }
            }
            this.alltried = true;
        }

        ArrayList<Integer> selected = new ArrayList<>();
        double biggervalue = Double.NEGATIVE_INFINITY;
        double sumNt = this.sumNt(nt);

        for (int i = 0; i < this.listAgents.size(); i++) {
            LowLevelHeuristic op = this.listAgents.get(i);
            double value = this.equation(op, rewards, nt, sumNt);
            if (value > biggervalue) {
                biggervalue = value;
                selected = new ArrayList<>();
                selected.add(i);
            } else if (value == biggervalue) {
                selected.add(i);
            }
        }
        Random rand = new Random();
        int p = rand.nextInt(selected.size());
        int select = selected.get(p);
        return select;
    }

    protected HashMap<String, Double> calcRewardSum() {
        HashMap<String, Double> rewards = new HashMap<>();
        this.nt = new HashMap<>();
        for (LowLevelHeuristic op : this.listAgents) {
            rewards.put(op.getAgentName(), 0.0);
            this.nt.put(op.getAgentName(), 0);
        }
        for (int i = 0; i < sl.getLength(); i++) {
            String opName = sl.getIndexOp(i);
            rewards.put(opName, rewards.get(opName) + sl.getFIR(i));
            this.nt.put(opName, this.nt.get(opName) + 1);
        }
        return rewards;
    }

    protected HashMap<String, Double> calcRewardExtreme() {
        HashMap<String, Double> rewards = new HashMap<>();
        this.nt = new HashMap<>();
        for (LowLevelHeuristic op : this.listAgents) {
            rewards.put(op.getAgentName(), 0.0);
            this.nt.put(op.getAgentName(), 0);
        }
        for (int i = 0; i < sl.getLength(); i++) {
            String opName = sl.getIndexOp(i);
            double slReward = sl.getFIR(i);
            double currentReward = rewards.get(opName);
            if (slReward > currentReward) {
                rewards.put(opName, slReward);
            }
            this.nt.put(opName, this.nt.get(opName) + 1);
        }
        return rewards;
    }

    public void addMetric(LowLevelHeuristic llh, double newMetricValue) {
        String currentLLHName = llh.getAgentName();
        this.sl.addItem(currentLLHName, newMetricValue, nt);
    }

    public LowLevelHeuristic selectionMethod() {
        HashMap<String, Double> rewards = this.calcRewardExtreme();
        int chosen = this.selectOperator(rewards, nt);
        LowLevelHeuristic llh = this.listAgents.get(chosen);
        return llh;
    }
}
