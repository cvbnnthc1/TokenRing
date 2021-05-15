package ru.phystech.tokenring.concurrentLinkedQueue;

import ru.phystech.tokenring.DataPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueNode extends Thread {
    private final int nodeId;
    private volatile boolean turnOn;
    private ConcurrentLinkedQueueNode next;
    private final ConcurrentLinkedQueueProcessor processor;
    private int counter;
    private long workTime;
    private List<Long> latencies;
    private ConcurrentLinkedQueue<DataPackage> bufferStack = new ConcurrentLinkedQueue<>();

    ConcurrentLinkedQueueNode(int nodeId, ConcurrentLinkedQueueProcessor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
    }

    public void setNext(ConcurrentLinkedQueueNode next) {
        this.next = next;
    }


    public long getId() {
        return nodeId;
    }

    public void off() {
        turnOn = false;
        //processor.getLogger().log(Level.INFO, LocalTime.now() + " id: " + nodeId + " off");

    }


    public void receivePackage(DataPackage dataPackage) {
        dataPackage.time = System.nanoTime();
        bufferStack.add(dataPackage);
    }

    @Override
    public void run() {
        workTime = System.currentTimeMillis();
        while (turnOn) {
            if (!bufferStack.isEmpty()) {
                DataPackage curPackage = bufferStack.poll();
                latencies.add(System.nanoTime() - curPackage.time);
                next.receivePackage(curPackage);
                counter++;
                //processor.getLogger().log(Level.INFO, LocalTime.now() + " node: " + nodeId + " sent package " + curPackage.getData());
            }
        }
        workTime = System.currentTimeMillis() - workTime;
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
