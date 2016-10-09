package br.usp.lti.cdds;


public class Job {

    private int processingTime;
    private int earliness;
    private int tardiness;
    private int orderId;

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
}
