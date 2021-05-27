package ru.phystech.tokenring;

import ru.phystech.tokenring.blockingQueue.ArrayBlockingQueueRingProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class Node extends Thread {
    protected int nodeId;
    protected volatile boolean turnOn;
    protected Node next;
    protected Processor processor;
    protected int counter;
    protected long workTime;
    protected List<Long> latencies;

    public Node(int nodeId, Processor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
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

    public double getAvgLatency() {
        if (turnOn) throw new IllegalStateException("Node must be off");
        if (latencies.size() > 0) {
            double result = 0d;
            for (int i = 0; i < latencies.size(); i++) {
                result += latencies.get(i) / (double) latencies.size();
            }
            return result;
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
