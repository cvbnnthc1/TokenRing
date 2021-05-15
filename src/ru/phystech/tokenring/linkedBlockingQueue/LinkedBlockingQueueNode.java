package ru.phystech.tokenring.linkedBlockingQueue;

import ru.phystech.tokenring.DataPackage;
import ru.phystech.tokenring.concurrentLinkedQueue.ConcurrentLinkedQueueProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class LinkedBlockingQueueNode extends Thread {
    private final int nodeId;
    private volatile boolean turnOn;
    private LinkedBlockingQueueNode next;
    private final LinkedBlockingQueueProcessor processor;
    private int counter;
    private long workTime;
    private List<Long> latencies;
    private Queue<DataPackage> bufferStack = new ArrayBlockingQueue<>(100);

    LinkedBlockingQueueNode(int nodeId, LinkedBlockingQueueProcessor processor) {
        this.nodeId = nodeId;
        this.processor = processor;
        this.turnOn = true;
        this.latencies = new ArrayList<>();
    }

    public void setNext(LinkedBlockingQueueNode next) {
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
                DataPackage curPackage = bufferStack.remove();
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
