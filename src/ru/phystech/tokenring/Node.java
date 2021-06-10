package ru.phystech.tokenring;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public abstract class Node extends Thread {
    protected int nodeId;
    protected volatile boolean turnOn;
    protected Node next;
    protected Processor processor;
    protected int counter;
    protected long workTime;
    protected List<Long> latencies;
    protected List<Long> times;

    public Node(int nodeId, Processor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
        this.times = new ArrayList<>();
    }

    abstract public void receivePackage(DataPackage dataPackage);

    public void setNext(Node next) {
        this.next = next;
    }


    public long getId() {
        return nodeId;
    }

    public void off() {
        turnOn = false;
    }

    protected void addTime(String data) {
        if (data.equals("0")) times.add(System.nanoTime());
    }

    protected void calculateLatencies() {
        for (int i = 1; i < times.size(); i++) {
            latencies.add(times.get(i) - times.get(i - 1));
        }
    }

    public double getAvgLatency(int nodeAmount) {
        if (turnOn) throw new IllegalStateException("Node must be off");
        List<Double> dividedLatencies = new ArrayList<>();
        if (latencies.size() > 0) {
            for (Long latency : latencies) {
                dividedLatencies.add(latency / (double) nodeAmount);
            }
            OptionalDouble result = dividedLatencies.stream().mapToDouble(x -> x).average();
            if (result.isPresent()) return Math.round(result.getAsDouble());
            else return 0;
        } else {
            return 0;
        }
    }

    public double getAvgThroughput() {
        if (turnOn) throw new IllegalStateException("Node must be off");
        return counter / (double) workTime * 1000;
    }

    public List<Long> getLatencies() {
        return latencies;
    }
}
