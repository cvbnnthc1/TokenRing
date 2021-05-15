package ru.phystech.tokenring.blockingQueue;

import ru.phystech.tokenring.DataPackage;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueNode extends Thread {
    private final int nodeId;
    private volatile boolean turnOn;
    private ArrayBlockingQueueNode next;
    private final ArrayBlockingQueueRingProcessor processor;
    private int counter;
    private long workTime;
    private List<Long> latencies;
    private Queue<DataPackage> bufferStack = new ArrayBlockingQueue<>(100);

    ArrayBlockingQueueNode(int nodeId, ArrayBlockingQueueRingProcessor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
    }

    public void setNext(ArrayBlockingQueueNode next) {
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
