package br.usp.lti.cdds.core;

public class Job {

    private int processingTime;
    private int earliness;
    private int tardiness;
    private int orderId;

    public Job(int orderId, int processingTime, int earliness, int tardiness) {
        this.processingTime = processingTime;
        this.earliness = earliness;
        this.tardiness = tardiness;
        this.orderId = orderId;
    }

    public Job() {
        this.processingTime = 0;
        this.earliness = 0;
        this.tardiness = 0;
        this.orderId = 0;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getEarliness() {
        return earliness;
    }

    public int getTardiness() {
        return tardiness;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public void setEarliness(int earliness) {
        this.earliness = earliness;
    }

    public void setTardiness(int tardiness) {
        this.tardiness = tardiness;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public double betaAlpha(){
        return ((double)this.tardiness)/((double)this.earliness);
    }
    
    public double processingAlpha(){
        return ((double)this.processingTime)/((double)this.earliness);
    }
    
    public double processingBeta(){
        return ((double)this.processingTime)/((double)this.tardiness);
    }

    @Override
    public String toString() {
        return "Job{" + "processingTime=" + processingTime + ", earliness=" + earliness + ", tardiness=" + tardiness + ", orderId=" + orderId + '}';
    }
    
    
}
